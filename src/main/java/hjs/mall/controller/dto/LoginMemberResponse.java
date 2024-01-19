package hjs.mall.controller.dto;

import hjs.mall.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginMemberResponse {
    private String userId;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
