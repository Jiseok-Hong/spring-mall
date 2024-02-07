package hjs.mall.repository;

import hjs.mall.domain.Basket;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BasketRepository {

    private final EntityManager em;

    public Optional<Basket> findByMemberId(String member_id) {
        List<Basket> basket = em.createQuery("select b from Basket b where b.member.id = :member_id", Basket.class)
                .setParameter("member_id", member_id)
                .getResultList();
        return basket.stream().findFirst();
    }

}
