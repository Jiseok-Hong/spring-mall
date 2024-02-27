package hjs.mall.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderItems> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name="promotion_code_id")
    private PromotionCodes promotionCodes;

    @Embedded
    private Address address;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;

    public void setPromotionCodes(PromotionCodes promotionCodes) {
        this.promotionCodes = promotionCodes;
    }

    public void setMember(Member member) {
        member.getOrders().add(this);
        this.member = member;
    }

    public void setOrderItems(OrderItems orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrders(this);
    }

    public int getTotalPrice() {
        return orderItems
                .stream()
                .mapToInt(OrderItems::getTotalPrice)
                .sum() * promotionCodes.getDiscountRate() / 100;
    }
}
