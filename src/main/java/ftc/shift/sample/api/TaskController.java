package ftc.shift.sample.api;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import ftc.shift.sample.models.FioRequest;
import ftc.shift.sample.models.FioResponse;
import ftc.shift.sample.repositories.FireRepository;
import ftc.shift.sample.services.Push;
//import org.json.simple.JSONObject;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

@RestController
public class TaskController {

  private static final String TASK_PATH = Resources.API_PREFIX ;
  Integer buf =2;
  Random random = new Random();
  FireRepository fireRepository = new FireRepository();


  public TaskController() throws IOException {
  }


  @PostMapping(TASK_PATH + "/check/") // Отправка на ML от оператора
  public @ResponseBody
  BaseResponse<FioResponse> checkFIO(@RequestBody FioRequest fioRequest)  {
      BaseResponse<FioResponse> response = new BaseResponse<>();
      FioResponse fioResponse = new FioResponse();
      System.out.println("Запрос!");

    //  buf = random.nextInt(3); //Отправка на ML


        //Пример GET запросв=а
//      ClientConfig config = new DefaultClientConfig();
//      Client client = Client.create(config);
//      WebResource service = client.resource(UriBuilder.fromUri("https://api.hh.ru/").build());
//      // getting JSON data
//      System.out.println(service.path("vacancies").path("26664176").accept(MediaType.APPLICATION_JSON).get(String.class));

//POST запрос

      ClientConfig config = new DefaultClientConfig();
      Client client = Client.create(config);
      WebResource webResource = client.resource(UriBuilder.fromUri("http://127.0.0.1:5000/test").build());
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("first", fioRequest.getFirst());
      jsonObject.put("second", fioRequest.getSecond());
      jsonObject.put("third", fioRequest.getThird());
      jsonObject.put("country", fioRequest.getCountry());
      ClientResponse responseML = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonObject.toString());
      String str = responseML.getEntity(String.class);
      jsonObject = new JSONObject(str);

      System.out.println("Response " + jsonObject.getInt("result"));
      buf = jsonObject.getInt("result");

      if (buf < 2){
        fioResponse.setResult(buf);
        response.setData(fioResponse);
      } else {
          String idNewTask = fireRepository.createTask(fioRequest.getCountry(), fioRequest.getFirst(), fioRequest.getSecond(), fioRequest.getThird());
          fioResponse.setResult(buf);
          fioResponse.setIdNewTask(idNewTask);
          new Push(idNewTask, fioRequest).createPush();
      }
      response.setData(fioResponse);
      return response;
  }

    @PostMapping(TASK_PATH + "/check_cur_task/") //Проверка готовности задачи
    public @ResponseBody
    BaseResponse<FioResponse> checkCurTask(@RequestBody FioResponse fioResponse){
        BaseResponse<FioResponse> response = new BaseResponse<>();
        fireRepository.checkCurTask(fioResponse);
        while (!fioResponse.getReceived()) { //Ждем пока придет ответ от FireBase

        }
        fioResponse.setReceived(null);
        response.setData(fioResponse);
        return response;
    }
}