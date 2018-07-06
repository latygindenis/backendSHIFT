package ftc.shift.sample.repositories;

public interface FireBaseRepository{
    String createTask(); // Возвращает id
    int checkCurTask(String id, FireRepository.Callback callback); //
}
