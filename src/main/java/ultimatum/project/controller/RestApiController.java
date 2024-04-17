package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.logInDTO.KakaoUserInfoDto;
import ultimatum.project.domain.dto.logInDTO.MemberRequestDto;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.service.member.KakaoService;
import ultimatum.project.service.member.MemberService;
import ultimatum.project.repository.MemberRepository;
import ultimatum.project.global.config.Security.auth.PrincipalDetails;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RestApiController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final KakaoService kakaoService;

    // 모든 사람이 접근 가능
    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "logout";
    }

    @GetMapping("/user")
    public PrincipalDetails member(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principal : " + principal.getUser().getMemberId());
        System.out.println("principal : " + principal.getUser().getMemberName());
        System.out.println("principal : " + principal.getUser().getMemberPassword());

        return principal;
    }

    // 어드민이 접근 가능
    @GetMapping("/admin/users")
    public List<Member> members() {
        return memberRepository.findAll();
    }

    @PostMapping("/join")
    public String join(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        String resultMessage = memberService.createMember(memberRequestDto);
        return resultMessage; // 회원가입 결과 메시지 반환
    }

    @PostMapping("/accessToken")
    public String getKakaoAccessToken(@RequestParam String code) {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        return "Access Token: " + accessToken;
    }

    @PostMapping("/kakao-login")
    public ResponseEntity<String> kakaoLogin(@RequestBody KakaoUserInfoDto kakaoUserInfoDto) {
        String accessToken = kakaoService.getKakaoAccessToken(kakaoUserInfoDto.getCode());
        if (accessToken != null && !accessToken.isEmpty()) {
            // 카카오 사용자 정보 가져오기
            String name = kakaoUserInfoDto.getName();
            String email = kakaoUserInfoDto.getEmail();

            // 사용자 정보로 로그인 또는 회원가입 처리
            MemberRequestDto memberRequestDto = new MemberRequestDto();
            memberRequestDto.setMemberName(name);
            memberRequestDto.setMemberEmail(email);

            return memberService.processKakaoLogin(kakaoUserInfoDto, memberRequestDto);
        } else {
            return ResponseEntity.badRequest().body("카카오 액세스 토큰을 가져오지 못했습니다.");
        }
    }

    @GetMapping("/user/info")
    @SecurityRequirement(name = "bearerAuth") // 토큰이 필요한 API
    @ResponseBody
    public String getUserInfo() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // 인증되지 않았거나, Authentication 객체가 null인 경우 처리
            return "현재 로그인된 사용자 정보를 가져올 수 없습니다.";
        }


        if (authentication != null && authentication.isAuthenticated()) {
            // 로그인된 사용자 이름 가져오기
            String username = authentication.getName();

            // 여기서는 단순히 예시로 사용자 이름을 반환합니다.
            return "현재 로그인된 사용자: " + username;
        } else {
            // 인증되지 않은 경우 또는 사용자 정보를 가져오지 못한 경우
            return "현재 로그인된 사용자 정보를 가져올 수 없습니다.";
        }
    }

    @GetMapping("/user/info/detail")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseBody
    public ResponseEntity<String> getUserInfoDetail(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }

        String memberEmail = authentication.getName();

        Member member = memberRepository.findByMemberEmail(memberEmail);

        if (member == null) {
            return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
        }

        String userDetails =
                "사용자 이름: " + member.getMemberName() +
                        ", 이메일: " + member.getMemberEmail() +
                        ", 성별: " + member.getMemberGender() +
                        ", 나이: " + member.getMemberAge() +
                        ", 주소: " + member.getMemberAddress();

        return ResponseEntity.ok(userDetails);
    }

}