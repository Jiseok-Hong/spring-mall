package hjs.mall.controller;

import hjs.mall.controller.dto.*;
import hjs.mall.domain.Member;
import hjs.mall.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/v1/members/register")
    public ResponseEntity<?> createMember(@RequestBody @Validated CreateMemberDto createMemberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new ErrorDto(errors));
        }

        memberService.join(createMemberDto);

        BasicResponse basicResponse = new BasicResponse("success", null, "Member Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(basicResponse);
    }

    @PostMapping("/v1/members/login")
    public ResponseEntity<?> loginMember(@RequestBody @Validated LoginMemberDto loginMemberDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new ErrorDto(errors));
        }

        LoginMemberResponse login = memberService.login(loginMemberDto);
        BasicResponse basicResponse = new BasicResponse("success", login, "Member Log in");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(basicResponse);
    }

    @PostMapping("/v1/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        MemberService.ResponseAcessToken responseAcessToken = memberService.generateAccessTokenWithRefreshToken(refreshToken);
        BasicResponse basicResponse = new BasicResponse("success", responseAcessToken, "Access Token is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(basicResponse);
    }

    @GetMapping("/v1/members")
    public ResponseEntity<?> loginMember() {

        List<Member> allMember = memberService.findAllMember();
        List<MemberResponseDto> list = allMember.stream().map(m -> new MemberResponseDto(m.getUserName(), m.getRole())).toList();

        BasicResponse basicResponse = new BasicResponse("success", list, "");
        return ResponseEntity.status(HttpStatus.OK).body(basicResponse);
    }
}
