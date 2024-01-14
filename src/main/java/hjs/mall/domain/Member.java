package hjs.mall.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String userName;
    private String password;
    private String salt;
    private String accessToken;
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String userId, String userName, String password, String salt) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.salt = salt;
    }
}
