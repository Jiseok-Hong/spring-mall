package hjs.mall.controller;

import hjs.mall.controller.dto.OrderResponseDto;
import hjs.mall.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    @GetMapping("/v1/orders")
    public ResponseEntity<?> getAllOrders(@RequestBody(required = false) MemberRequest memberRequest) {
        List<OrderResponseDto> collect = orderRepository.findAll(memberRequest.member_id).stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(collect);
    }

//    @GetMapping("/v1/orders/items")
//    public List<OrderResponseDto> getAllOrdersWithItems(@RequestBody Long member_id) {
//
//    }

    @Data
    static class MemberRequest {
        private String member_id;
    }
}
