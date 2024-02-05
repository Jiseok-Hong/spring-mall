package hjs.mall.controller;

import hjs.mall.controller.dto.*;
import hjs.mall.domain.*;
import hjs.mall.repository.ItemRepository;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.repository.OrderItemsRepository;
import hjs.mall.repository.OrderRepository;
import hjs.mall.service.OrderService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final MemberJpaRepository memberJpaRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/v1/orders")
    public ResponseEntity<?> getAllOrders(@RequestBody(required = false) MemberRequest memberRequest) {
        List<OrderResponseDto> collect = orderRepository.findAll(memberRequest.member_id).stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());

        BasicResponse basicResponse = new BasicResponse("success", collect, "");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(basicResponse);
    }

    @PostMapping("/v1/orders")
    public ResponseEntity<?> createOrder(@RequestBody @Validated OrderCreateRequest orderCreateRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new ErrorDto(errors));
        }

        List<OrderItems> collect = orderCreateRequest.getOrderItem().stream().map(oi -> {
            Optional<Item> byId = itemRepository.findById(oi.getItem_id());
            return byId.map(item -> OrderItems.builder()
                    .item(item)
                    .orderPrice(oi.getOrderPrice())
                    .count(oi.getCount())
                    .build()).orElse(null);
        }).toList();

        orderService.createOrderWithItems(
                        memberJpaRepository.findById(orderCreateRequest.member_id),
                        orderCreateRequest.address,
                        collect);

        BasicResponse basicResponse = new BasicResponse("success", null, "Order Created");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(basicResponse);
    }

    @GetMapping("/v1/orders/items")
    public ResponseEntity<?> getAllOrdersWithItems(@RequestBody(required = false) MemberRequest memberRequest) {
        List<OrderItemsResponseDto> collect = orderRepository.findAllWithItems(memberRequest.member_id)
                .stream()
                .map(OrderItemsResponseDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(collect);
    }

    @PostMapping("/v1/orders/add-basket")
    public ResponseEntity<?> addOrderItemToBasket(@RequestBody(required = false) BasketAddRequest basketAddRequest) {

        orderService.addItemsToBasket(
                memberJpaRepository.findById(basketAddRequest.memberId),
                itemRepository.findById(basketAddRequest.itemId),
                basketAddRequest.count,
                basketAddRequest.orderPrice);

        BasicResponse basicResponse = new BasicResponse("success", null, "Items added to basket");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(basicResponse);
    }

    @Data
    static class MemberRequest {
        private String member_id;
    }

    @Data
    static class OrderCreateRequest {
        @NotNull(message = "member id is required")
        Long member_id;
        Address address;
        List<OrderItemDto> orderItem;
    }

    @Data
    static class BasketAddRequest {
        private Long memberId;
        private Long itemId;
        private int count;
        private int orderPrice;
    }
}
