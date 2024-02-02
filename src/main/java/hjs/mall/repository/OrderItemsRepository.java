package hjs.mall.repository;

import hjs.mall.domain.Member;
import hjs.mall.domain.OrderItems;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemsRepository {
    private final EntityManager em;

    @Transactional
    public Long save(OrderItems orderItems) {
        em.persist(orderItems);
        return orderItems.getId();
    }

    @Transactional
    public Optional<OrderItems> findByBasketAndItem(Long basket_id, Long item_id) {
        List<OrderItems> resultList = em.createQuery("select oi from OrderItems oi" +
                        " where oi.item.id = :item_id AND" +
                        " oi.basket.id = :basket_id", OrderItems.class)
                .setParameter("basket_id", basket_id)
                .setParameter("item_id", item_id)
                .getResultList();

        return resultList.stream().findAny();
    }
}
