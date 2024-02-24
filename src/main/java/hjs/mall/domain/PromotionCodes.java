package hjs.mall.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;

public class PromotionCodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_code_id")
    private Long id;

    private String promotionCode;
    private int discountRate;
    private LocalDate expiryDate;
}
