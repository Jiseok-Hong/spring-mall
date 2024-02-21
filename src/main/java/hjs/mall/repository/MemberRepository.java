package hjs.mall.repository;

import hjs.mall.domain.Member;
import org.springframework.data.jpa.repository.*;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
