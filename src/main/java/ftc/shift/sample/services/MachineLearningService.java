package ftc.shift.sample.services;

import ftc.shift.sample.models.FioRequest;
import org.json.JSONObject;

import static ftc.shift.sample.api.Resources.ML_ADDRESS;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class MachineLearningService {
    private String server = ML_ADDRESS + "/test";
    private RestTemplate rest;
    private HttpHeaders headers;
    private HttpStatus status;

    public int getResult(FioRequest fioRequest) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first", fioRequest.getFirst());
        jsonObject.put("second", fioRequest.getSecond());
        jsonObject.put("third", fioRequest.getThird());
        jsonObject.put("country", fioRequest.getCountry());
        rest = new RestTemplate();
        headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), headers);
        ResponseEntity<String> responseEntity = rest.exchange(server, HttpMethod.POST, requestEntity, String.class);
        this.setStatus(responseEntity.getStatusCode());

        jsonObject = new JSONObject(responseEntity.getBody());
        System.out.println(jsonObject.getInt("result"));
        return jsonObject.getInt("result");
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
