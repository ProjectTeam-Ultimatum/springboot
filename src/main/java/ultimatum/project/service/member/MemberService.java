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
import ultimatum.project.domain.dto.logInDTO.MemberUpdateRequestDto;
import ultimatum.project.domain.dto.logInDTO.*;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.member.MemberImage;
import ultimatum.project.global.config.Security.jwt.JwtProperties;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ultimatum.project.repository.member.MemberImageRepository;
import ultimatum.project.repository.member.MemberRepository;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberImageService memberImageService;
    private final JwtProperties jwtProperties;
    private final KakaoService kakaoService;
    @Autowired
    private JavaMailSender javaMailSender;

    private final MemberImageRepository memberImageRepository; // MemberImageRepository 주입

    @Transactional
    public String createMember(MemberRequestDto memberRequestDto) {

        Member existingMember = memberRepository.findByMemberEmail(memberRequestDto.getMemberEmail());
        if (existingMember != null) {
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXISTS);
        }

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

    public ResponseEntity<?> registerOrLoginWithKakao(String accessToken,
                                                      MemberWithKakaoRequestDto additionalInfo) {
        // 카카오 API로부터 사용자 정보를 조회
        KakaoUserInfoDTO1 kakaoUserInfo = kakaoService.getKakaoUserInfo(accessToken);

        // 데이터베이스에서 사용자 조회
        Member member = memberRepository.findByMemberEmail(kakaoUserInfo.getMemberEmail());
        if (member == null) {
            log.info("사용자가 존재하지 않습니다 : {}", kakaoUserInfo.getMemberEmail());
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }
        member.setMemberAge(additionalInfo.getMemberAge());  // 예: 나이 정보 업데이트
        member.setMemberGender(additionalInfo.getMemberGender());  // 예: 성별 정보 업데이트
        member.setMemberAddress(additionalInfo.getMemberAddress());
        member.setMemberFindPasswordAnswer(additionalInfo.getMemberFindPasswordAnswer());
        member.setMemberPassword("");
        member.setMemberRole("ROLE_USER");

        memberRepository.save(member);

        // 이미지 파일 처리를 담당하는 서비스 메소드 호출
        List<MemberImage> newMemberImages = memberImageService.createMemberImages(additionalInfo.getFiles(), member);

// 기존 컬렉션에 새로운 이미지 객체들을 추가하기 전에 컬렉션의 모든 기존 요소를 삭제
        if (member.getMemberImages() != null) {
            member.getMemberImages().clear(); // 기존 이미지들 삭제
            member.getMemberImages().addAll(newMemberImages); // 새로운 이미지들 추가
        } else {
            member.setMemberImages(new ArrayList<>(newMemberImages)); // 새 컬렉션 할당
        }
        // 변경된 회원 객체를 다시 저장 (이미지 정보 포함)
        memberRepository.save(member);
        log.info("회원정보 업데이트 완료 : {}", member.getMemberEmail());

        return ResponseEntity.ok("회원정보가 업데이트 되었습니다.");

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

//    public ResponseEntity<String> processKakaoLogin(KakaoUserInfoDto userInfo) {
//        Member member = findOrCreateUser(userInfo);
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                member.getMemberEmail(), null, member.getAuthorities());
//
//        SecurityContextHolder.getContext().setAuthentication(token);
//
//        String jwtToken = JWT.create()
//                .withSubject(member.getMemberEmail())
//                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
//                .withClaim("id", member.getMemberId())
//                .withClaim("username", member.getMemberName())
//                .withClaim("userid", member.getMemberEmail())
//                .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
//
//        return ResponseEntity.ok()
//                .header(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken)
//                .body("Login successful with Kakao account");
//    }



    public String changePassword(String userEmail, String currentPassword, String newPassword) {
        Member member = memberRepository.findByMemberEmail(userEmail);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (member == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        // 현재 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(currentPassword, member.getMemberPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 설정
        member.setMemberPassword(bCryptPasswordEncoder.encode(newPassword));
        memberRepository.save(member);

        return "비밀번호 변경 성공";
    }

    @Transactional
    public String updateMemberStyle(String userEmail, String memberStyle) {
        Member member = memberRepository.findByMemberEmail(userEmail);
        if (member == null) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        member.setMemberStyle(memberStyle); // 스타일 업데이트
        memberRepository.save(member);
        return "회원 스타일 정보가 업데이트 되었습니다.";
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

    public ResponseEntity<String> deleteMember(String userEmail, String password, String answer) {
        Member member = memberRepository.findByMemberEmail(userEmail);

        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 비밀번호 확인
        if (!bCryptPasswordEncoder.matches(password, member.getMemberPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        // 특정 질문 답변 확인
        if (!answer.equals(member.getMemberFindPasswordAnswer())) {
            throw new CustomException(ErrorCode.INVALID_ANSWER);
        }

        // 회원 삭제
        memberRepository.delete(member);

        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    public String getUserAccessTokenFromKaKao(String code) {

        // 카카오 서비스에서 액세스 토큰 가져오기
        String accessToken = kakaoService.getKakaoAccessToken(code);
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("액세스 토큰을 받아오지 못했습니다.");
        }
        return accessToken;
    }

    public KakaoUserInfoDTO1 getUserInfoFromKakao(String accessToken) {
        KakaoUserInfoDTO1 kakaoUserInfoDTO1 = kakaoService.getKakaoUserInfo(accessToken);
        if (kakaoUserInfoDTO1 == null) {
            throw new IllegalArgumentException("사용자 정보를 받아오지 못했습니다.");
        }
        return kakaoUserInfoDTO1;
    }

    public Member registerOrLookupMember(KakaoUserInfoDTO1 kakaoUserInfoDTO1) {
        Member member = new Member();
        member.setMemberName(kakaoUserInfoDTO1.getMemberName());
        member.setMemberEmail(kakaoUserInfoDTO1.getMemberEmail());
        member.setMemberPassword(null);
        member.setMemberAge(null);
        member.setMemberGender(null);
        member.setMemberAddress(null);
        member.setMemberFindPasswordAnswer(null);
        member.setMemberRole("ROLE_USER");
        member.setMemberStyle(null);
        member.setMemberImages(null);
        memberRepository.save(member);
        return member;
    }

    // 사용자 정보 업데이트 메서드
    public ResponseEntity<String> updateMemberInfo(String userEmail, MemberUpdateRequestDto updateRequestDto) {
        Member member = memberRepository.findByMemberEmail(userEmail);

        if (member == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        // 요청된 업데이트 필드를 검사하여 업데이트
        if (updateRequestDto.getMemberAge() != null ) {
            member.setMemberAge(updateRequestDto.getMemberAge());
        }

        if (updateRequestDto.getMemberAddress() != null && !updateRequestDto.getMemberAddress().isEmpty()) {
            member.setMemberAddress(updateRequestDto.getMemberAddress());
        }

        // 변경된 회원 정보 저장
        memberRepository.save(member);

        return ResponseEntity.ok("회원 정보가 성공적으로 업데이트되었습니다.");
    }



}