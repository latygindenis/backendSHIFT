package ftc.shift.sample.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class FioRequest {
    String first;
    String second;
    String third;
    String country;
}
