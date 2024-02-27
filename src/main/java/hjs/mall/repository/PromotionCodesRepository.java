package hjs.mall.repository;

import hjs.mall.domain.PromotionCodes;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionCodesRepository {
    private final EntityManager em;

    public Optional<PromotionCodes> findByPromotionCode(String promotionCode) {
        List<PromotionCodes> promotion = em.createQuery("select p from PromotionCodes p where p.promotionCode = :promotionCode", PromotionCodes.class)
                .setParameter("promotionCode", promotionCode)
                .getResultList();

        return promotion.stream().findFirst();
    }

}
