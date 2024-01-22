package hjs.mall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String userName;
    private String password;
    private String salt;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}