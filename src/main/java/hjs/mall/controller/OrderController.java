package hjs.mall.controller;

import hjs.mall.controller.dto.OrderResponseDto;
import hjs.mall.domain.Orders;
import hjs.mall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private OrderRepository orderRepository;

    @GetMapping("/v1/orders")
    public List<OrderResponseDto> getAllOrders(@RequestBody Long member_id) {
        return orderRepository.findAll(member_id)
                .stream()
                .map(o -> new OrderResponseDto())
                .collect(Collectors.toList());
    }

//    @GetMapping("/v1/orders/items")
//    public List<OrderResponseDto> getAllOrdersWithItems(@RequestBody Long member_id) {
//
//    }
}
