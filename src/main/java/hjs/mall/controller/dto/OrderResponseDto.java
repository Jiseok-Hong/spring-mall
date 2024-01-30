package hjs.mall.controller.dto;

import hjs.mall.domain.Address;
import hjs.mall.domain.Member;
import hjs.mall.domain.OrderItems;
import hjs.mall.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponseDto {

    private String name;
    private Address address;
    private List<OrderItems> orderItems = new ArrayList<>();
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
}
