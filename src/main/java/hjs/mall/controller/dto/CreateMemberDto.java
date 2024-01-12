package hjs.mall.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class CreateMemberDto {
    @NotEmpty(message = "Id is required")
    private String userId;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "User name is required")
    private String userName;

}
