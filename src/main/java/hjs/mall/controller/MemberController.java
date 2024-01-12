package hjs.mall.controller;

import hjs.mall.controller.dto.CreateMemberDto;
import hjs.mall.controller.dto.ErrorDto;
import hjs.mall.controller.dto.Response;
import hjs.mall.domain.Member;
import hjs.mall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/v1/members")
    public ResponseEntity<?> createMember(@RequestBody @Validated CreateMemberDto createMemberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new ErrorDto(errors));
        }

        memberService.join(createMemberDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new Response("success", null, "Member Created"));
    }
}
