package hjs.mall.controller.dto;

import hjs.mall.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginMemberDto {

    @NotEmpty(message = "Id is required")
    private String userId;

    @NotEmpty(message = "Password is required")
    private String password;

}
