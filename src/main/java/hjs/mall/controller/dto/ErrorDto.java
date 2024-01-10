package hjs.mall.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorDto {
    private List<String> errors;

    public ErrorDto() {
        // Empty constructor
    }

    public ErrorDto(List<String> errors) {
        this.errors = errors;
    }
}