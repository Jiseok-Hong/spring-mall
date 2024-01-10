package hjs.mall.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class CreateMemberDto {
    @NotEmpty(message = "Name is required")
    private String userId;

    @NotEmpty(message = "Name is required")
    private String userName;
}
