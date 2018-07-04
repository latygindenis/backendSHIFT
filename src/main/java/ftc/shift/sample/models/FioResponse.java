package ftc.shift.sample.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class FioResponse {
    Integer result;

    public FioResponse(Integer result) {
        this.result = result;
    }

    public FioResponse() {
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

}
