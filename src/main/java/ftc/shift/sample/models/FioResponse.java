package ftc.shift.sample.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class FioResponse {
    String first;
    String second;
    String third;
    String idNewTask;
    Integer result = -1;
    Boolean received = false;
    public String toString(){
        return first + " " + second + " " + third;
    }
}
