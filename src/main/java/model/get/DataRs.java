package model.get;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataRs {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String avatar;
}
