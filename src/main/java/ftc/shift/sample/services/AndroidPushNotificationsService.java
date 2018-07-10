package ftc.shift.sample.services;

import com.fasterxml.jackson.databind.util.JSONPObject;


import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;



@Service
public class AndroidPushNotificationsService {
	private static final String FIRE_TOKEN ="key=AAAA3G-H6MY:APA91bEqmEpqJcnEMqXIFaFNtvqNp_XMMtGfEAj7F6eUjx9wtareEKplWT62bZe9PbwaQqZbrSEBv8ucVOoSu_BqvGbHbhZltAyITbqQofmD-PmYyUrn5GVtC4WTS0oW6Oqovoxx53B7IlvOb5xGqzKEvUtKLIVeDQ" ;
	private static final String FIREBASE_SERVER_KEY = FIRE_TOKEN;
	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";


	@Async
	public CompletableFuture<String> send (String body) {
        System.out.println(body);
		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", FIREBASE_SERVER_KEY);
        HttpEntity<String> httpEntity = new HttpEntity<String>(body, headers);
        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
		return CompletableFuture.completedFuture(firebaseResponse);
	}
}
