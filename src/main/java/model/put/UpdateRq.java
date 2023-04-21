package model.put;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateRq {
    private String name;
    private String job;
}

