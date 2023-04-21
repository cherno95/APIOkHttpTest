package model.put;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRs {
    private String name;
    private String job;
    private String createAt;
}

