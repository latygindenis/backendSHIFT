package ftc.shift.sample.repositories;

public interface FireBaseRepository{
    String createTask(String country, String first, String second, String third); // Возвращает id
    void checkCurTask(String id, FireRepository.Callback callback); //
}
