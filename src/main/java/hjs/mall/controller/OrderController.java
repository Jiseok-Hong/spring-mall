package hjs.mall.controller;

import hjs.mall.controller.dto.OrderResponseDto;
import hjs.mall.domain.Orders;
import hjs.mall.repository.OrderRepository;
import hjs.mall.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

    private OrderService orderService;
    private OrderRepository orderRepository;
    @GetMapping("/v1/orders")
    public ResponseEntity<?> getAllOrders(@RequestBody String member_id) {

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
    }

//    @GetMapping("/v1/orders/items")
//    public List<OrderResponseDto> getAllOrdersWithItems(@RequestBody Long member_id) {
//
//    }
}
