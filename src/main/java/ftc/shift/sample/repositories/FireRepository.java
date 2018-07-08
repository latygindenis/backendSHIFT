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

public class FireRepository implements FireBaseRepository {

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
                if ( dataSnapshot.getChildrenCount() < 5 ){
                    fioResponse.setResult(2);
                } else {
                    for(DataSnapshot i:dataSnapshot.getChildren()){
                        if (i.getValue(Integer.class) == 0){
                            badFIOcount++;
                        } else {
                            goodFIOcount++;
                        }
                    }
                    if (badFIOcount > goodFIOcount){
                        fioResponse.setResult(0);
                        //updateBlackList();
                    } else {
                        fioResponse.setResult(1);
                       // updateWhiteList();
                    }

                    updateStaticstics();
                    DatabaseReference delReef = FirebaseDatabase.getInstance().getReference().child("tasks").child(fioResponse.getIdNewTask());
                    delReef.removeValue((error, ref1) -> { }); // Удаление записи
                }



                fioResponse.setReceived(true);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Error!");
            }
        });
    }

    public void checkBlackWhiteLists(FioRequest fioRequest){ //Проверка в черно-белых списках
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("first");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
    private void updateStaticstics(){};
    private void updateBlackList(FioResponse fioResponse)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks").child(fioResponse.getIdNewTask()).child("accs");



    };
    private void updateWhiteList(){};
}
