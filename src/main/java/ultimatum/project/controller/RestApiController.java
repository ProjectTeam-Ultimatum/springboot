package ultimatum.project.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.config.auth.PrincipalDetails;
import ultimatum.project.repository.MemberRepository;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class RestApiController {


    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public String join(@RequestBody Member member) {
        if (member.getMemberName() == null || member.getMemberPassword() == null || member.getMemberEmail() == null) {
            throw new IllegalArgumentException("회원 정보를 모두 입력하세요.");
        }

        if (member.getMemberPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력하세요.");
        }

        member.setMemberPassword(bCryptPasswordEncoder.encode(member.getMemberPassword()));
        member.setMemberRole("ROLE_USER");
        memberRepository.save(member);
        return "회원가입완료";
    }




}

