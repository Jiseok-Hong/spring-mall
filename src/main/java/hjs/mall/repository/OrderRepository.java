package hjs.mall.repository;

import hjs.mall.domain.Orders;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Orders order) {
        em.persist(order);
    }

    public Optional<Orders> findById(Long id){
        Orders order = em.find(Orders.class, id);
        return Optional.of(order);
    }

    public List<Orders> findAll(String member_id) {
        return em.createQuery("select o from Orders o" +
                        " where o.member.id = :member_id", Orders.class)
                .setParameter("member_id", member_id)
                .getResultList();
    }

    public List<Orders> findAllWithItems(String member_id) {
        return em.createQuery("select o from Orders o" +
                " join fetch o.member m" +
                " join fetch o.orderItems oi" +
                " join fetch oi.item i" +
                " where o.member.id = :member_id", Orders.class)
                .setParameter("member_id", member_id)
                .getResultList();

    }
}
