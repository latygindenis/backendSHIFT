package ftc.shift.sample.repositories;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import ftc.shift.sample.models.FioRequest;
import ftc.shift.sample.models.FioResponse;
import ftc.shift.sample.models.Task;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FireRepository implements FireBaseRepository {
    HashMap<String, Integer> listOfCurTaskAccessors;

    public FireRepository() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src\\main\\resources\\shift-d39fd-firebase-adminsdk-yvm27-01e0791aa1.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://shift-d39fd.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

    @Override
    public String createTask(String country, String first, String second, String third) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");
        DatabaseReference taskId = ref.push();
        Task task = new Task(0, country, first, second, third, taskId.getKey());
        taskId.setValueAsync(task);
        return taskId.getKey();
    }


    @Override
    public void checkCurTask(FioResponse fioResponse) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks").child(fioResponse.getIdNewTask()).child("accs");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int badFIOcount = 0, goodFIOcount = 0;
                listOfCurTaskAccessors = new HashMap<>();
                if (dataSnapshot.getChildrenCount() < 3) {
                    fioResponse.setResult(2);
                } else {
                    for (DataSnapshot i : dataSnapshot.getChildren()) {
                        listOfCurTaskAccessors.put(i.getKey(), i.getValue(Integer.class));
                        if (i.getValue(Integer.class) == 0) {
                            badFIOcount++;
                        } else {
                            goodFIOcount++;
                        }
                    }

                    if (badFIOcount > goodFIOcount) {
                        fioResponse.setResult(0);
                        updateRate(0);
                    } else {
                        fioResponse.setResult(1);
                        updateRate(1);
                    }

                    DatabaseReference delReef = FirebaseDatabase.getInstance().getReference().child("tasks").child(fioResponse.getIdNewTask());
                    delReef.removeValue((error, ref1) -> {
                    }); // Удаление записи
                }
                fioResponse.setReceived(true);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error!");
            }
        });
    }

    private void updateRate(Integer rightAnswer) {
        DatabaseReference accRef = FirebaseDatabase.getInstance().getReference().child("accs");
        accRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot rateSnapshot:snapshot.getChildren()){
                    if (listOfCurTaskAccessors.get(rateSnapshot.getKey()).equals(rightAnswer)){
                        Integer wins, amountOfTasks;
                        System.out.println("id" + rateSnapshot.getKey());
                        System.out.println("wins:" + rateSnapshot.child("wins").getValue());
                        wins = rateSnapshot.child("wins").getValue(Integer.class);
                        amountOfTasks = rateSnapshot.child("amountOfTasks").getValue(Integer.class);
                        accRef.child(rateSnapshot.getKey()).child("wins").setValueAsync( wins + 1);
                        accRef.child(rateSnapshot.getKey()).child("amountOfTasks").setValueAsync(amountOfTasks + 1);
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }


    public void checkBlackWhiteLists(FioRequest fioRequest) { //Проверка в черно-белых списках
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("blackwhitelist");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(fioRequest.toString()).getValue() != null){
                    fioRequest.setInListResult(snapshot.child(fioRequest.toString()).getValue(Integer.class));
                    System.out.println(snapshot.child(fioRequest.toString()).getKey());
                }
                fioRequest.setCheckedInList(true);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
