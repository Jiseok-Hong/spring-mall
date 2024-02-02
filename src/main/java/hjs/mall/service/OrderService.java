package hjs.mall.service;

import hjs.mall.domain.Basket;
import hjs.mall.domain.Item;
import hjs.mall.domain.Member;
import hjs.mall.domain.OrderItems;
import hjs.mall.repository.BasketRepository;
import hjs.mall.repository.ItemRepository;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.repository.OrderItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final OrderItemsRepository orderItemsRepository;
    public void addItemsToBasket(Long member_id, Long item_id, int count, int orderPrice) {
        Member basketOwner = memberJpaRepository.findById(member_id);
        Basket basket = basketOwner.getBasket();

        Item itemIsNotExisted = itemRepository.findById(item_id).orElseThrow(() -> new IllegalStateException("item is not existed"));

        Optional<OrderItems> byBasketAndItem = orderItemsRepository.findByBasketAndItem(basket.getId(), itemIsNotExisted.getId());

        if (byBasketAndItem.isPresent()) {
            System.out.println(byBasketAndItem.get().getCount());
            byBasketAndItem.get().addCount(count);
            return;
        }

        OrderItems orderItems = OrderItems.builder()
                .item(itemIsNotExisted)
                .count(count)
                .orderPrice(orderPrice)
                .build();
        orderItems.addBasket(basket);

        orderItemsRepository.save(orderItems);
    }
}
