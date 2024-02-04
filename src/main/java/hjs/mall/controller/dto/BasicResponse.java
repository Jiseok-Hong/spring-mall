package hjs.mall.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicResponse {
    private String status;
    private Object data;
    private String message;
}
