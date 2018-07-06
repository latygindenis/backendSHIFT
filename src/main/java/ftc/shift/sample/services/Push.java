package ftc.shift.sample.services;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Push {

    private final String TOPIC = "JavaSampleApproach";
    String name="Латыгин Денис Сергеевич";
    String title = "Check!";

    public ResponseEntity<String> createPush() {

        JSONObject body = new JSONObject();
        body.put("to", "/topics/" + TOPIC);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("body",  name);

        JSONObject data = new JSONObject();
        data.put("Key-1", "Denis");
        data.put("Key-2", "Sergeevich");
        data.put("Key-3", "Latygin");

        body.put("notification", notification);
        body.put("data", data);
        System.out.println(body);

        AndroidPushNotificationsService androidPushNotificationsService = new AndroidPushNotificationsService();
        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(body.toString());
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();
            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}


