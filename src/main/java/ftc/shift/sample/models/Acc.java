package ftc.shift.sample.models;




import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Acc {
    String email;
    String country;
    String id;
    int amountOfTasks;
    int wins;

    public void updateAcc(Acc newAcc) {
        this.email = newAcc.getEmail();
        this.country = newAcc.getCountry();
        this.id = newAcc.getId();
        this.amountOfTasks = newAcc.getAmountOfTasks();
        this.wins = newAcc.getWins();
    }
}
