package hjs.mall.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@NoArgsConstructor
public class Basket {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "basket_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "basket")
    List<OrderItems> orderItems = new ArrayList<>();

    public void setMember(Member member) {
        this.member = member;
    }
}
