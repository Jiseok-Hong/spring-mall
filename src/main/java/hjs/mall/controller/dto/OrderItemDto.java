package hjs.mall.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class OrderItemDto {
    private Long item_id;
    private int count;
    private int orderPrice;
}
