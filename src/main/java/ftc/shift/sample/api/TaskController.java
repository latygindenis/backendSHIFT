package ftc.shift.sample.api;


import com.google.firebase.auth.FirebaseAuthException;
import ftc.shift.sample.models.*;
import ftc.shift.sample.repositories.FireRepository;
import ftc.shift.sample.services.MachineLearningService;
import ftc.shift.sample.services.Push;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        fireRepository.checkBlackWhiteLists(fioRequest);

        while(!fioRequest.getCheckedInList()){ //Ждем проверки в черном/белом списке

        }


        if (fioRequest.getInListResult() != null){
            result = fioRequest.getInListResult();
        } else {
            result = machineLearningService.getResult(fioRequest);
        }


        if (result == 0 || result == 1) {
            fioResponse.setResult(result);
            response.setData(fioResponse);
        } else if (result == 2){
            String idNewTask = fireRepository.createTask(fioRequest.getCountry(), fioRequest.getFirst(), fioRequest.getSecond(), fioRequest.getThird());
            System.out.println("idNewTask " + idNewTask);
            fioResponse.setResult(result);
            fioResponse.setIdNewTask(idNewTask);
            fioResponse.setFirst(fioRequest.getFirst());
            fioResponse.setSecond(fioRequest.getSecond());
            fioResponse.setThird(fioRequest.getThird());
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
        if(fioResponse.getResult() == -1){
            response.setMessage("Нет такой задачи!");
        }
        response.setData(fioResponse);
        return response;
    }

    @PostMapping(TASK_PATH + "/tasks/")
    public @ResponseBody
    BaseResponse<List<Task>> getNotAnsweredTasks(@RequestBody String accId) {
        BaseResponse<List<Task>> response = new BaseResponse<>();
        JSONObject jsonObject = new JSONObject(accId);
        String id = jsonObject.getString("id");
        ArrayList<Task> listOfNotAsweredTasks = new ArrayList<>();

        for(Task task:fireRepository.getListOfTasks()){

            if (task.getAccs()!=null){
                if (!task.getAccs().containsKey(id)){
                    listOfNotAsweredTasks.add(task);
                }

            } else {
                listOfNotAsweredTasks.add(task);
            }
        }
        response.setData(listOfNotAsweredTasks);
        return response;
    }


    @PostMapping(TASK_PATH + "/auth/sign_up/")
    public @ResponseBody
    BaseResponse<String> signUp (@RequestBody SignUp signUp) throws FirebaseAuthException {
        BaseResponse<String> response = new BaseResponse<>();
        response.setData(fireRepository.signUp(signUp));
        return response;
    }

    @PostMapping(TASK_PATH + "/result/")
    public @ResponseBody
    BaseResponse <String> updateResult(@RequestBody DoneTaskByAcc doneTaskByAcc){
        BaseResponse<String> response = new BaseResponse<>();
        fireRepository.updateResult(doneTaskByAcc);
//        response.setData("OK");
        return response;
    }
    @PostMapping(TASK_PATH + "/profile/")
    public @ResponseBody
    BaseResponse <Acc> getProfile(@RequestBody String accId) throws InterruptedException {
        BaseResponse<Acc> response = new BaseResponse<>();
        String id = new JSONObject(accId).getString("accId");
        Acc acc = fireRepository.getAccProfile(id);
        response.setData(acc);
        return response;
    }




//    @PostMapping(TASK_PATH + "/auth/sign_in/")
//    public @ResponseBody
//    BaseResponse<String> signIn (@RequestBody SignIn signIn) throws FirebaseAuthException {
//        BaseResponse<String> response = new BaseResponse<>();
//        response.setData(fireRepository.signIn(signIn));
//        return response;
//    }



//    @PostMapping(TASK_PATH + "/auth/sign_in/")
//    public @ResponseBody

/*
    @PostMapping(TASK_PATH + "/auth/sign_in/")
    public @ResponseBody

    @PostMapping(TASK_PATH + "/auth/sign_up/")
    public @ResponseBody


    @PostMapping(TASK_PATH + "/tasks/") done
    public @ResponseBody
    BaseResponse<Task> allTask(@RequestBody Task task) {}

    @PostMapping(TASK_PATH + "/result/")
    public @ResponseBody

    @PostMapping(TASK_PATH + "/profile/")
    public @ResponseBody

*/

}