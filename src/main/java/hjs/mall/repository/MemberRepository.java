package hjs.mall.repository;

import hjs.mall.domain.Member;
import org.springframework.data.jpa.repository.*;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserName(String userName);
}
