package model.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRs {
    private String name;
    private String job;
    private String id;
    private String createAt;
}

