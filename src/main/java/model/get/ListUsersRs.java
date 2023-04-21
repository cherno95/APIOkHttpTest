package model.get;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListUsersRs {
    private int page;
    private int per_page;
    private int total;
    private int total_pages;
    private List<DataRs> data;
    private SupportRs support;
}

