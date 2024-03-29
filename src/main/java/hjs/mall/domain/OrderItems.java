package hjs.mall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    private int count;
    private int orderPrice;

    public void cancelOrder() {
        getItem().addStock(count);
    }

    public int getTotalPrice() {
        return count * orderPrice;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public void addBasket(Basket basket) {
        this.basket = basket;
        basket.getOrderItems().add(this);
    }

    public void addCount(int count) {
        this.count += count;
    }
}
