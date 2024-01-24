package hjs.mall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String userId;
    private String userName;
    private String password;
    private String salt;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = LAZY, mappedBy = "member", cascade = ALL)
    private Basket basket;

    @OneToMany(mappedBy = "member")
    private List<Orders> orders = new ArrayList<>();

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setInitialBasket(Basket basket) {
        this.basket = basket;
        basket.setMember(this);
    }
}