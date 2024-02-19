package hjs.mall.controller;

import hjs.mall.controller.dto.*;
import hjs.mall.domain.Basket;
import hjs.mall.domain.Member;
import hjs.mall.domain.OrderItems;
import hjs.mall.exception.DataNotExistException;
import hjs.mall.repository.BasketRepository;
import hjs.mall.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final BasketRepository basketRepository;

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
        MemberService.ResponseAuthToken responseAcessToken = memberService.generateAccessTokenWithRefreshToken(refreshToken);
        BasicResponse basicResponse = new BasicResponse("success", responseAcessToken, "Access Token is created");
        return ResponseEntity.status(HttpStatus.CREATED).body(basicResponse);
    }

    @GetMapping("/v1/members")
    public ResponseEntity<?> loginMember() {

        List<Member> allMember = memberService.findAllMember();
        List<MemberResponseDto> list = allMember.stream()
                .map(m -> new MemberResponseDto(m.getUserName(),
                        m.getRole(),
                        m.getBasket().getId()))
                .toList();

        BasicResponse basicResponse = new BasicResponse("success", list, "");
        return ResponseEntity.status(HttpStatus.OK).body(basicResponse);
    }

    @GetMapping("/v1/members/basket")
    public ResponseEntity<?> getBasket(@RequestBody BasketRequestDto basketRequestDto) {
        Basket basket = basketRepository
                .findByMemberId(basketRequestDto.getMember_id())
                .orElseThrow(() -> new DataNotExistException("member does not exist"));
        BasketResponseDto basketResponseDto = new BasketResponseDto(basket);

        BasicResponse basicResponse = new BasicResponse("success", basketResponseDto, "");
        return ResponseEntity.status(HttpStatus.OK).body(basicResponse);
    }

    @GetMapping("/v1/members/stats")
    public ResponseEntity<?> getMemberOrderStats(@RequestBody MemberRequestDto basketRequestDto) {

        MemberStatsResponse memberAllStats = memberService.getMemberAllStats(basketRequestDto.member_id);

        BasicResponse basicResponse = new BasicResponse("success", memberAllStats, "");
        return ResponseEntity.status(HttpStatus.OK).body(basicResponse);
    }

    @PostMapping("/v1/members/logout")
    public ResponseEntity<?> logoutMember(@RequestBody MemberRequestDto memberRequestDto) {

        memberService.logout(memberRequestDto.member_id);

        BasicResponse basicResponse = new BasicResponse("success", null, "");
        return ResponseEntity.status(HttpStatus.OK).body(basicResponse);
    }

    @Data
    static class BasketRequestDto {
        private String member_id;
    }

    @Data
    static class MemberRequestDto {
        private Long member_id;
    }

    @Data
    static class BasketResponseDto{
        private Long basket_id;
        private List<OrderItemDto> orderItems;

        public BasketResponseDto(Basket basket) {
            this.basket_id = basket.getId();
            this.orderItems = basket.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .toList();
        }
    }

    @Data
    static class OrderItemDto {
        private int count;
        private int orderPrice;
        private String item_name;

        public OrderItemDto(OrderItems orderItems) {
            this.count = orderItems.getCount();
            this.orderPrice = orderItems.getOrderPrice();
            this.item_name = orderItems.getItem().getName();
        }

    }

}
