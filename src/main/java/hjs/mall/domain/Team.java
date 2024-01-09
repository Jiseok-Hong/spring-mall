package hjs.mall.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
    }
}
