package ftc.shift.sample.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Task {
    HashMap<String, Integer> accs;
    int count;
    String country;
    String first;
    String second;
    String third;
    String id;
}
