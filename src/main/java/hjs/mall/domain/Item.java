package hjs.mall.domain;

import hjs.mall.exception.NotEnoughStockException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @OneToMany(mappedBy = "item")
    private List<OrderItems> orderItems = new ArrayList<>();

    private String name;
    private int price;
    private int stockQuantity;

    public int addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
        return this.stockQuantity;
    }

    public int removeStock(int stockQuantity) {
        int restStock = this.stockQuantity - stockQuantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("Stock is not enough");
        }
        this.stockQuantity = restStock;

        return restStock;
    }
}
