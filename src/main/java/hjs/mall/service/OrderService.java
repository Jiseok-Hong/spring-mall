package hjs.mall.service;

import hjs.mall.controller.dto.OrderItemDto;
import hjs.mall.domain.*;
import hjs.mall.exception.DataNotExistException;
import hjs.mall.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderItemsRepository orderItemsRepository;
    private final OrderRepository orderRepository;
    private final PromotionCodesRepository promotionCodesRepository;
    public void createOrderWithItems(Member member, Address address, String promotionCode, List<OrderItems> orderItemDtoList) {


        Orders order = Orders.builder()
                .member(member)
                .address(address)
                .orderStatus(OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        PromotionCodes byPromotionCode = promotionCodesRepository.findByPromotionCode(promotionCode)
                .orElseThrow(() -> new DataNotExistException("Promotion Code is not existed"));

        order.setPromotionCodes(byPromotionCode);

        for (OrderItems orderItem : orderItemDtoList) {
            order.setOrderItems(orderItem);
            orderItem.getItem().removeStock(orderItem.getCount());
        }

        orderRepository.save(order);
    }

    public void addItemsToBasket(Member member, Optional<Item> item, int count, int orderPrice) {
        Basket basket = member.getBasket();

        if (item.isEmpty()) {
            throw new IllegalStateException("item is not existed");
        }

        Optional<OrderItems> byBasketAndItem = orderItemsRepository.findByBasketAndItem(basket.getId(), item.get().getId());

        if (byBasketAndItem.isPresent()) {
            byBasketAndItem.get().addCount(count);
            return;
        }

        OrderItems orderItems = OrderItems.builder()
                .item(item.get())
                .count(count)
                .orderPrice(orderPrice)
                .build();
        orderItems.addBasket(basket);

        orderItemsRepository.save(orderItems);
    }
}
