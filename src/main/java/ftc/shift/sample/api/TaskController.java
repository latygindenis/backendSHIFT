package ftc.shift.sample.api;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import ftc.shift.sample.models.FioRequest;
import ftc.shift.sample.models.FioResponse;
import ftc.shift.sample.repositories.FireRepository;
import ftc.shift.sample.services.MachineLearningService;
import ftc.shift.sample.services.Push;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Random;

@RestController
public class TaskController {

    private static final String TASK_PATH = Resources.API_PREFIX;
    private Integer result = -1;
    private FireRepository fireRepository = new FireRepository();
    private MachineLearningService machineLearningService = new MachineLearningService();


    public TaskController() throws IOException {
    }


    @PostMapping(TASK_PATH + "/check/") // Отправка на ML от оператора
    public @ResponseBody
    BaseResponse<FioResponse> checkFIO(@RequestBody FioRequest fioRequest) {
        BaseResponse<FioResponse> response = new BaseResponse<>();
        FioResponse fioResponse = new FioResponse();
        System.out.println("Запрос!");

        result = machineLearningService.getResult(fioRequest);

        if (result < 2) {
            fioResponse.setResult(result);
            response.setData(fioResponse);
        } else {
            String idNewTask = fireRepository.createTask(fioRequest.getCountry(), fioRequest.getFirst(), fioRequest.getSecond(), fioRequest.getThird());
            fioResponse.setResult(result);
            fioResponse.setIdNewTask(idNewTask);
            new Push(idNewTask, fioRequest).createPush();
        }
        response.setData(fioResponse);
        return response;
    }

    @PostMapping(TASK_PATH + "/check_cur_task/") //Проверка готовности задачи
    public @ResponseBody
    BaseResponse<FioResponse> checkCurTask(@RequestBody FioResponse fioResponse) {
        BaseResponse<FioResponse> response = new BaseResponse<>();
        fireRepository.checkCurTask(fioResponse);
        while (!fioResponse.getReceived()) { //Ждем пока придет ответ от FireBase

        }
        fioResponse.setReceived(null);
        response.setData(fioResponse);
        return response;
    }
}