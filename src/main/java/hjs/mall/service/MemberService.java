package hjs.mall.service;

import hjs.mall.domain.Member;
import hjs.mall.exception.DuplicatedMemberIdException;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberRepository;

    @Transactional
    public Long join(Member member) {

        checkDuplicatedMemberId(member);

        return memberRepository.save(member);
    }

    private void checkDuplicatedMemberId(Member member) {
        Optional<Member> byUserId = memberRepository.findByUserId(member.getUserId());
        if (byUserId.isPresent()) {
            throw new DuplicatedMemberIdException("The Member Id is already occupied by other member");
        }
    }
}
