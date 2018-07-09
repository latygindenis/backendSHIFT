package ftc.shift.sample.repositories;

import ftc.shift.sample.models.FioRequest;
import ftc.shift.sample.models.FioResponse;

public interface FireBaseRepository {
    String createTask(String country, String first, String second, String third); // Возвращает id
    void checkCurTask(FioResponse fioResponse);
    void checkBlackWhiteLists(FioRequest fioRequest);
}
