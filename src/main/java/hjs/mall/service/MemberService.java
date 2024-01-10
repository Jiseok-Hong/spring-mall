package hjs.mall.service;

import hjs.mall.domain.Member;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public String join(Member member) {
        return memberRepository.save(member);
    }
}
