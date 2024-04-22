package ultimatum.project.service.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.logInDTO.KakaoUserInfoDto;
import ultimatum.project.domain.dto.logInDTO.MemberFindPasswordRequestDto;
import ultimatum.project.domain.dto.logInDTO.MemberRequestDto;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.member.MemberImage;
import ultimatum.project.global.config.Security.jwt.JwtProperties;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
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
    private final JwtProperties jwtProperties;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public String createMember(MemberRequestDto memberRequestDto) {
        // 회원 정보 생성
        Member member = Member.builder()
                .memberName(memberRequestDto.getMemberName())
                .memberEmail(memberRequestDto.getMemberEmail())
                .memberPassword(bCryptPasswordEncoder.encode(memberRequestDto.getMemberPassword()))
                .memberAge(memberRequestDto.getMemberAge())
                .memberGender(memberRequestDto.getMemberGender())
                .memberAddress(memberRequestDto.getMemberAddress())
                .memberFindPasswordAnswer(memberRequestDto.getMemberFindPasswordAnswer())
                .memberRole("ROLE_USER")
                .build();

        // 초기 회원 정보 저장
        member = memberRepository.save(member);

        // 파일 처리 및 MemberImage 객체 리스트 생성
        List<MemberImage> memberImages = memberImageService.createMemberImages(memberRequestDto.getFiles(), member);

        // 이미지 객체들을 회원 엔티티에 설정
        member.setMemberImages(memberImages);

        // 변경된 회원 객체를 다시 저장 (이미지 정보 포함)
        memberRepository.save(member);

        // 회원가입 완료 메시지 반환
        return "회원가입 완료: " + member.getMemberName();
    }

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
                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));

        return ResponseEntity.ok()
                .header(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken)
                .body("Login successful with Kakao account");
    }

    public String changePassword(String userEmail, String currentPassword, String newPassword) {
        Member member = memberRepository.findByMemberEmail(userEmail);

        if (member == null) {
            return "사용자를 찾을 수 없습니다.";
        }

        // 현재 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(currentPassword, member.getMemberPassword())) {
            return "현재 비밀번호가 일치하지 않습니다.";
        }

        // 새로운 비밀번호 설정
        member.setMemberPassword(bCryptPasswordEncoder.encode(newPassword));
        memberRepository.save(member);

        return "비밀번호 변경 성공";
    }

    public ResponseEntity<String> memberCheck(MemberFindPasswordRequestDto memberFindPasswordRequestDto) {
        String memberName = memberFindPasswordRequestDto.getMemberName();
        String memberEmail = memberFindPasswordRequestDto.getMemberEmail();
        String memberAnswer = memberFindPasswordRequestDto.getMemberFindPasswordAnswer();

        Member member = memberRepository.findByMemberNameAndMemberEmail(memberName, memberEmail);

        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        if (!memberAnswer.equals(member.getMemberFindPasswordAnswer())) {
            throw new CustomException(ErrorCode.INVALID_ANSWER_VALUE);
        }

        String newPassword = generateRandomPassword();

        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        member.setMemberPassword(encodedPassword);
        memberRepository.save(member);

        sendTemporaryPasswordByEmail(memberEmail, newPassword);

        return ResponseEntity.ok("임시 비밀번호가 이메일로 전송되었습니다.");
    }

    private String generateRandomPassword() {

        /**
         * StringBuilder: 문자열을 효율적으로 처리하기 위한 클래스로, 비밀번호를 생성할 때 사용됩니다.
         * randomIndex: 0부터 characters 문자열 길이 사이의 난수를 생성하여 무작위로 문자를 선택합니다.
         * sb.append(characters.charAt(randomIndex)): 무작위로 선택된 문자를 StringBuilder에 추가합니다.
         */

        int length = 10;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

    // 이메일로 임시 비밀번호 전송
    private void sendTemporaryPasswordByEmail(String recipientEmail, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("임시 비밀번호 안내");
        message.setText("안녕하세요. 임시 비밀번호는 " + temporaryPassword + "입니다. 로그인 후 비밀번호를 변경해주세요.");
        javaMailSender.send(message);
    }

}
