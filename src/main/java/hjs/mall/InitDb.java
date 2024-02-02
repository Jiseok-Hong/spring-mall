package hjs.mall;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.domain.*;
import hjs.mall.service.MemberService;
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
        private final MemberService memberService;
        public void dbInit1() {

            CreateMemberDto createMemberDto = new CreateMemberDto();
            createMemberDto.setUserName("john");
            createMemberDto.setPassword("1234");
            createMemberDto.setUserId("john");
            createMemberDto.setRole(Role.ADMIN);

            memberService.join(createMemberDto);

            Item item1 = Item.builder()
                    .name("iphone")
                    .price(100)
                    .stockQuantity(1000)
                    .build();
            em.persist(item1);

            Item item2 = Item.builder()
                    .name("galaxy")
                    .price(200)
                    .stockQuantity(1000)
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

            Member singleResult = em.createQuery("select m from Member m" +
                                    " where m.userName = :userName"
                            , Member.class)
                    .setParameter("userName", "john")
                    .getSingleResult();


            Orders orders = Orders.builder()
                    .address(new Address("Hong Kong", "Hong Kong", "123"))
                    .orderDate(LocalDateTime.now())
                    .orderStatus(OrderStatus.PENDING)
                    .member(singleResult)
                    .build();

            orders.setOrderItems(orderItems1);
            orderItems1.getItem().removeStock(orderItems1.getCount());
            orders.setOrderItems(orderItems2);
            orderItems2.getItem().removeStock(orderItems2.getCount());

            em.persist(orders);
        }

    }
}


