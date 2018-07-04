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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    String emailCo;

    public FioRequest(String first, String second, String third, String emailCo) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.emailCo = emailCo;
    }

    public FioRequest() {
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getThird() {
        return third;
    }

    public void setThird(String third) {
        this.third = third;
    }

    public String getEmailCo() {
        return emailCo;
    }

    public void setEmailCo(String emailCo) {
        this.emailCo = emailCo;
    }
}
