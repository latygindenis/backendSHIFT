package ftc.shift.sample.api;


import ftc.shift.sample.models.FioRequest;
import ftc.shift.sample.models.FioResponse;
import ftc.shift.sample.repositories.FireRepository;
import ftc.shift.sample.services.AndroidPushNotificationsService;
import ftc.shift.sample.services.BookService;
import ftc.shift.sample.models.Book;
import ftc.shift.sample.services.Push;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

@RestController
public class BooksController {

  private static final String BOOKS_PATH = Resources.API_PREFIX + "fio";
  Integer buf =0;
  Random random = new Random();

  @Autowired
  private BookService service;

  @GetMapping(BOOKS_PATH + "/{id}")
  public @ResponseBody
  BaseResponse<Book> readBook(@PathVariable String id) {
    BaseResponse<Book> response = new BaseResponse<>();
    Book book = service.provideBook(id);

    if (null == book) {
      response.setStatus("BOOK_NOT_EXIST");  //для статусов  можно сделать отдельные Enum-ы или вынести как строковые константы
      response.setMessage("Книга не найдена!");
    } else {
      response.setData(book);
    }
    return response;
  }

  @GetMapping(BOOKS_PATH)
  public @ResponseBody
  BaseResponse<Collection<Book>> listBooks() {
    BaseResponse<Collection<Book>> response = new BaseResponse<>();
    Collection<Book> result = service.provideBooks();
    response.setData(result);
    return response;
  }

  @PostMapping("api/check")
  public @ResponseBody
  BaseResponse<FioResponse> checkFIO(@RequestBody FioRequest fioRequest) throws IOException {
      System.out.println(buf++);
      BaseResponse<FioResponse> response = new BaseResponse<>();
      FioResponse fioResponse = new FioResponse();
      buf = random.nextInt(3); //Отправка на ML

      if (buf < 2){
        fioResponse.setResult(buf);
        response.setData(fioResponse);
        new FireRepository().createTask();
        new Push().createPush();

      } else {
        //Место для пуша на ассесоров
        new FireRepository().createTask();
        new Push().createPush();
      }
      return response;
  }

//  @PostMapping("/api/checkcurtask")  // Проверка каждые 5 секунд
//  public @ResponseBody
//  BaseResponse<FioResponse> checkCurTask(@RequestBody FioRequest fioRequest){
//
//      //Проверка счетчика на firebase
//      //Если счетчик в базе равен пяти то ответить
//
//  }

//  @PostMapping("api/sendres")
//  public @ResponseBody
//  BaseResponse<
//          >




  @DeleteMapping(BOOKS_PATH + "/{id}")
  public @ResponseBody
  BaseResponse deleteBook(@PathVariable String id) {
    service.deleteBook(id);
    return new BaseResponse(); //нет тела, только статус
  }

  @PatchMapping(BOOKS_PATH + "/{id}")
  public @ResponseBody
  BaseResponse<Book> updateBook(@PathVariable String id, @RequestBody Book book) {
    BaseResponse<Book> response = new BaseResponse<>();
    Book result = service.updateBook(book);
    response.setData(result);
    return response;
  }

}