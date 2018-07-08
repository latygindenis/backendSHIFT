package ftc.shift.sample.services;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import ftc.shift.sample.api.Resources;
import ftc.shift.sample.models.FioRequest;
import org.json.JSONObject;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import static ftc.shift.sample.api.Resources.ML_ADDRESS;

public class MachineLearningService {
    private ClientConfig config = new DefaultClientConfig();
    private Client client = Client.create(config);
    private WebResource webResource = client.resource(UriBuilder.fromUri(ML_ADDRESS + "/test").build());

    public int getResult(FioRequest fioRequest) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first", fioRequest.getFirst());
        jsonObject.put("second", fioRequest.getSecond());
        jsonObject.put("third", fioRequest.getThird());
        jsonObject.put("country", fioRequest.getCountry());
        ClientResponse responseML = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonObject.toString());
        String str = responseML.getEntity(String.class);
        jsonObject = new JSONObject(str);
        return jsonObject.getInt("result");
    }
}
