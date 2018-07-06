package ftc.shift.sample.repositories;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import ftc.shift.sample.models.Task;

import java.io.FileInputStream;
import java.io.IOException;

public class FireRepository implements FireBaseRepository {
    public FireRepository() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("C:\\Users\\denis\\Desktop\\backend\\src\\main\\resources\\shift-d39fd-firebase-adminsdk-yvm27-01e0791aa1.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://shift-d39fd.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }

    @Override
    public String createTask() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tasks");
        DatabaseReference taskId = ref.push();
        Task task = new Task(1, "Russia", "Латыгин", "Денис", "Сергеевич", taskId.getKey());
        taskId.setValueAsync(task);
        return null;
    }

    @Override
    public Object checkCurTask() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks");
        Object curTasks = null;
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object document = dataSnapshot.getValue();
                System.out.println(document);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        System.out.println(curTasks);
        return curTasks;
    }


}
