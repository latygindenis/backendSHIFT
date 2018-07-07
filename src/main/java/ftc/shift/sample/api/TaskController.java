package ftc.shift.sample.api;


import ftc.shift.sample.models.FioRequest;
import ftc.shift.sample.models.FioResponse;
import ftc.shift.sample.repositories.FireRepository;
import ftc.shift.sample.services.Push;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        while (!fioResponse.getReceived()) {

        }
        fioResponse.setReceived(null);
        response.setData(fioResponse);
        return response;
    }

    @PostMapping(TASK_PATH + "/update_cur_task/") //Проверка готовности задачи
    public @ResponseBody
    BaseResponse<FioResponse> updateCurTask(@RequestBody FioRequest fioRequest)  {



        BaseResponse<FioResponse> response = new BaseResponse<>();
        FioResponse fioResponse = new FioResponse();

        return response;
    }




//  @PostMapping("/api/checkcurtask")  // Проверка каждые 5 секунд
//  public @ResponseBody
//  BaseResponse<FioResponse> checkCurTask(@RequestBody FioRequest fioRequest){
//
//      //Проверка счетчика на firebase
//      //Если счетчик в базе равен пяти то ответить
//    fireRepository.checkCurTask("-LGhpXA--bDaB_ZBN-f9", new FireRepository.Callback() {
//        @Override
//        public void onSucess(Object count) {
//            System.out.println("Осталось: " + count);
//        }
//
//        @Override
//        public void onError(Throwable ex) {
//
//        }
//    });
//
//  }

//  @PostMapping("api/sendres")
//  public @ResponseBody
//  BaseResponse<
//          >


}