package ultimatum.project.service.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.logInDTO.KakaoUserInfoDto;
import ultimatum.project.domain.dto.logInDTO.MemberRequestDto;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.member.MemberImage;
import ultimatum.project.global.config.Security.jwt.JwtProperties;
import ultimatum.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberImageService memberImageService;

    public String createMember(MemberRequestDto memberRequestDto) {
        // 회원 정보 생성
        Member member = Member.builder()
                .memberName(memberRequestDto.getMemberName())
                .memberEmail(memberRequestDto.getMemberEmail())
                .memberPassword(bCryptPasswordEncoder.encode(memberRequestDto.getMemberPassword()))
                .memberAge(memberRequestDto.getMemberAge())
                .memberGender(memberRequestDto.getMemberGender())
                .memberAddress(memberRequestDto.getMemberAddress())
                .memberRole("ROLE_USER")
                .build();

        memberRepository.save(member);

        if (memberRequestDto.getFiles() != null && !memberRequestDto.getFiles().isEmpty()) {
            List<MemberImage> images = memberImageService.createMemberImages(memberRequestDto.getFiles(), member);
            member.setMemberImages(images);
            memberRepository.save(member);
        }

        // 회원가입 완료 메시지 반환
        return "회원가입 완료: " + member.getMemberName();
    }

//    public Member findMemberByEmail(String email) {
//        return memberRepository.findByMemberEmail(email);
//    }

//    public ResponseEntity<String> processKakaoLogin(KakaoUserInfoDto kakaoUserInfoDto, MemberRequestDto memberRequestDto) {
//        String code = kakaoUserInfoDto.getCode();
//        String name = kakaoUserInfoDto.getName();
//        String email = kakaoUserInfoDto.getEmail();
//
//        Member existingMember = memberRepository.findByMemberEmail(email);
//
//        if (existingMember != null) {
//            // 이미 존재하는 회원인 경우: 로그인 성공
//            return ResponseEntity.ok("Welcome back, " + existingMember.getMemberName() + "! Email: " + email);
//        } else {
//            // 존재하지 않는 회원인 경우: 회원가입 처리
//            return ResponseEntity.badRequest().body("회원 정보가 부족합니다. 추가 정보를 입력해주세요.");
//        }
//    }

    public ResponseEntity<String> signUpWithKakao(KakaoUserInfoDto kakaoUserInfoDto, MemberRequestDto memberRequestDto) {
        String name = kakaoUserInfoDto.getName();
        String email = kakaoUserInfoDto.getEmail();

        // 사용자 정보로 회원가입 처리
        Member newMember = Member.builder()
                .memberName(name)
                .memberEmail(email)
                .memberPassword("") // 카카오 소셜 로그인은 비밀번호 필요 없음
                .memberRole("ROLE_USER") // 기본 역할 설정
                .memberAge(memberRequestDto.getMemberAge())
                .memberGender(memberRequestDto.getMemberGender())
                .memberAddress(memberRequestDto.getMemberAddress())
                .build();

        memberRepository.save(newMember);

        return ResponseEntity.ok("Welcome, " + newMember.getMemberName() + "! Email: " + email);
    }

    public Member findOrCreateUser(KakaoUserInfoDto userInfo) {
        Member member = memberRepository.findByMemberEmail(userInfo.getEmail());
        if (member == null) {
            // 회원가입 로직
            member = Member.builder()
                    .memberEmail(userInfo.getEmail())
                    .memberName(userInfo.getName())
                    .memberPassword("") // 패스워드는 임의로 설정
                    .memberRole("ROLE_USER")
                    .build();
            memberRepository.save(member);
        }
        return member;
    }

    public ResponseEntity<String> processKakaoLogin(KakaoUserInfoDto userInfo, MemberRequestDto memberRequestDto) {
        Member member = findOrCreateUser(userInfo);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                member.getMemberEmail(), null, member.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);

        String jwtToken = JWT.create()
                .withSubject(member.getMemberEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", member.getMemberId())
                .withClaim("username", member.getMemberName())
                .withClaim("userid", member.getMemberEmail())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return ResponseEntity.ok()
                .header(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken)
                .body("Login successful with Kakao account");
    }

}
