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
    private EntityManager em;

    public void save(Orders order) {
        em.persist(order);
    }

    public Optional<Orders> findById(Long id){
        Orders order = em.find(Orders.class, id);
        return Optional.of(order);
    }

    public List<Orders> findAll(Long member_id) {
        List<Orders> memberId = em.createQuery("select o from Orders o"
                        , Orders.class)
                .getResultList();
        System.out.println(memberId);
        return memberId;
    }
}
