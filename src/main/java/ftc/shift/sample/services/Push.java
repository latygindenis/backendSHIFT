package ftc.shift.sample.services;

import ftc.shift.sample.models.FioRequest;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Push {

    private final String TOPIC = "JavaSampleApproach";
    String name;
    String title = "Check!";
    String idNewTask;
    FioRequest fioRequest;

    public Push(String idNewTask, FioRequest fioRequest) {
        this.fioRequest = fioRequest;
        name = fioRequest.getFirst() + " " + fioRequest.getSecond() + " " + fioRequest.getThird();
        this.idNewTask = idNewTask;
    }

    public ResponseEntity<String> createPush() {

        JSONObject body = new JSONObject();
        body.put("to", "/topics/" + TOPIC);
        body.put("priority", "high");

        JSONObject notification = new JSONObject();
        notification.put("title", title);
        notification.put("body",  name);

        JSONObject data = new JSONObject();
        data.put("first", fioRequest.getFirst());
        data.put("second", fioRequest.getSecond());
        data.put("third", fioRequest.getThird());
        data.put("id", idNewTask);

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


