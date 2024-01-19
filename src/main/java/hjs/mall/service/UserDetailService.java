package hjs.mall.service;

import hjs.mall.domain.Member;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailService {

    private final MemberJpaRepository memberJpaRepository;

    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberJpaRepository.findByUserId(username).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication!")
        );

        return new CustomUserDetail(member);
    }
}
