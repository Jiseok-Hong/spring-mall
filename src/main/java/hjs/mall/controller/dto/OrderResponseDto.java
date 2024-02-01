package hjs.mall.controller.dto;

import hjs.mall.domain.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponseDto {

    private String name;
    private Address address;
//    private List<OrderItems> orderItems = new ArrayList<>();
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;

    public OrderResponseDto(Orders orders) {
        this.name = orders.getMember().getUserName();
        this.address = orders.getAddress();
//        this.orderItems = orders.getOrderItems();
        this.orderStatus = orders.getOrderStatus();
        this.orderDate = orders.getOrderDate();

    }
}
