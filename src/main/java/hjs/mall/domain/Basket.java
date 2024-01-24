package hjs.mall.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
public class Basket {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "basket_id")
    private Long id;

    @OneToOne(fetch = LAZY, mappedBy = "basket")
    private Member member;

    @OneToMany(mappedBy = "basket")
    List<OrderItems> orderItems = new ArrayList<>();
}
