package hjs.mall.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberStatsResponse {
    private String userName;
    private int sum;
    private int orderCount;
    private int itemCount;
}
