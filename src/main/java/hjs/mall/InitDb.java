package hjs.mall;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.domain.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Basket basket = new Basket();

            Member member = Member.builder()
                    .role(Role.ADMIN)
                    .userId("john")
                    .password("1234")
                    .userName("john")
                    .build();

            member.setInitialBasket(basket);
            em.persist(member);
            Item item1 = Item.builder()
                    .name("iphone")
                    .price(100)
                    .stockQuantity(100)
                    .build();
            em.persist(item1);

            Item item2 = Item.builder()
                    .name("galaxy")
                    .price(200)
                    .stockQuantity(100)
                    .build();
            em.persist(item2);

            OrderItems orderItems1 = OrderItems.builder()
                    .item(item1)
                    .count(3)
                    .orderPrice(100)
                    .build();

            OrderItems orderItems2 = OrderItems.builder()
                    .item(item2)
                    .count(2)
                    .orderPrice(200)
                    .build();

            Orders orders = Orders.builder()
                    .address(new Address("Hong Kong", "Hong Kong", "123"))
                    .orderDate(LocalDateTime.now())
                    .orderStatus(OrderStatus.PENDING)
                    .member(member)
                    .build();

            orders.setOrderItems(orderItems1);
            orders.setOrderItems(orderItems2);

            em.persist(orders);
        }

    }
}


