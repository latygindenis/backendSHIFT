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
    public void checkCurTask(String id, Callback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tasks/" + id + "/count");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onSucess(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
}

    public interface Callback {
        void onSucess (Object count);
        void onError (Throwable ex);
    }


}
