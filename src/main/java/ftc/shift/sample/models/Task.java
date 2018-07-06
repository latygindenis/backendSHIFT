package ftc.shift.sample.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Task {
    int count;
    String country;
    String first;
    String second;
    String third;
    String id;
}
