package hjs.mall.repository;

import hjs.mall.domain.Basket;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BasketRepository {

    private EntityManager em;

    public Optional<Basket> findByMemberId(Long member_id) {
        Basket basket = em.createQuery("select b from Basket b where b.member = :member_id", Basket.class)
                .setParameter("member_id", member_id)
                .getSingleResult();

        return Optional.of(basket);
    }

}
