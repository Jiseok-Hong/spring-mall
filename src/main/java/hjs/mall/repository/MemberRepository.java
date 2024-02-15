package hjs.mall.repository;

import hjs.mall.domain.Member;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUserName(String userName);

    List<Member> findByUserNameAndAgeGreaterThan(String userName, int age);

    @Query("select m from Member m where m.userName = :userName and m.age= :age")
    List<Member> findUsers(@Param("userName") String userName, @Param("age") int age);

    @Query("select m.userName from Member m")
    List<String> findUserNames();

//    @Query("select new study.datajpa.dto.MemberDto(m.userName, m.age, t.name) from Member m join m.team t")
//    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.userName in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUserName(String userName);

    Member findMemberByUserName(String userName);

    Optional<Member> findOptionalByUserName(String userName);

    //Slice<Member>
    //List<Member>
    @Query(value = "select m from Member m left join m.team",
            countQuery = "select count(m) from Member  m")
    Page<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //clearAutomatically is to flush entity manager
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgeUpdate(@Param("age") int age);


    @Query("select m from Member m join fetch m.team") //->@Override @EntityGraph(attributePaths = {"team"})
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
//    @EntityGraph("Member.all")
    List<Member> findAll();

    //optimize to get the data for read: does not make snapshot
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value="true"))
    Member findReadOnlyByUserName(String userName);
}
