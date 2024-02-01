package hjs.mall.controller.dto;

import hjs.mall.domain.Address;
import hjs.mall.domain.OrderItems;
import hjs.mall.domain.OrderStatus;
import hjs.mall.domain.Orders;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderItemsResponseDto {
    private Address address;

    private String name;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private List<OrderItemsDto> orderItems;

    public OrderItemsResponseDto(Orders orders) {
        this.name = orders.getMember().getUserName();
        this.address = orders.getAddress();
        this.orderStatus = orders.getOrderStatus();
        this.orderDate = orders.getOrderDate();
        this.orderItems = orders.getOrderItems()
                .stream()
                .map(OrderItemsDto::new)
                .collect(Collectors.toList());

    }

    @Data
    static class OrderItemsDto{
        private String name;
        private int count;
        private int orderPrice;

        public OrderItemsDto(OrderItems orderItems) {
            this.name = orderItems.getItem().getName();
            this.count = orderItems.getCount();
            this.orderPrice = orderItems.getOrderPrice();
        }
    }
}
