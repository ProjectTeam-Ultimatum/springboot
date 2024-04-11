package ultimatum.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.dto.MemberRequestDto;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.config.auth.PrincipalDetails;
import ultimatum.project.repository.MemberRepository;
import ultimatum.project.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestApiController {


    private final MemberRepository memberRepository;

    private final MemberService memberService;

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
        System.out.println("principal : "+principal.getUser().getMemberId());
        System.out.println("principal : "+principal.getUser().getMemberName());
        System.out.println("principal : "+principal.getUser().getMemberPassword());

        return principal;
    }

    // 어드민이 접근 가능
    @GetMapping("/admin/users")
    public List<Member> members() {
        return memberRepository.findAll();
    }

    @PostMapping("/join")
    public String join(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        // MemberService의 createMember 메서드 호출
        String resultMessage = memberService.createMember(memberRequestDto);
        return resultMessage; // 회원가입 결과 메시지 반환
    }

}