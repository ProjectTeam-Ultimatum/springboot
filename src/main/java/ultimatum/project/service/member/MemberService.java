package ultimatum.project.service.member;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.logInDTO.*;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.member.MemberImage;
import ultimatum.project.global.config.Security.jwt.JwtProperties;
import ultimatum.project.global.config.Security.oauth2.Oauth2UserService;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.member.MemberRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberImageService memberImageService;
    private final JwtProperties jwtProperties;

    @Autowired
    private JavaMailSender javaMailSender;

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

    public MemberWithKakaoResponseDto registerOrLoginWithKakao(@AuthenticationPrincipal OAuth2User principal,
                                                               MemberWithKakaoRequestDto additionalInfo) {
        // OAuth2User에서 카카오 유저 정보 추출
        String email = principal.getAttribute("email");
        String name = principal.getAttribute("name");
        log.info("카카오 유저 information : {} ", email);

        // 데이터베이스에서 사용자 조회
        Member member = memberRepository.findByMemberEmail(email);
        boolean isNewMember = member == null;

        if (isNewMember) {
            // 사용자 새로 생성
            member = createUserWithKakao(principal, additionalInfo);
            // 이미지 처리
            List<MemberImage> memberImages = memberImageService.createMemberImages(additionalInfo.getFiles(), member);
            if (memberImages.isEmpty() && !additionalInfo.getFiles().isEmpty()) {
                throw new CustomException(ErrorCode.FILE_PROCESSING_ERROR);

            }
            member.setMemberImages(memberImages);
            memberRepository.save(member);
            log.info("새로운 회원 등록 : {}", member.getMemberEmail());
            log.info("소셜 로그인 멤버 가입 완료 !!!"); // 멤버 저장 완료 로그

        } else {
            log.info("기존회원로그인 : {}", member.getMemberEmail());
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
        String jwtToken = jwtProperties.generateToken(member);
        // 이미지 리스트 준비
        List<MemberImage> memberImages = isNewMember ? member.getMemberImages() : Collections.emptyList();
        // 응답 DTO 생성
        MemberWithKakaoResponseDto responseDto = createResponseDto(member, memberImages, jwtToken, isNewMember);

        return responseDto;

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

    public ResponseEntity<String> processKakaoLogin(KakaoUserInfoDto userInfo) {
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

    public MemberWithKakaoResponseDto createResponseDto(Member member, List<MemberImage> memberImages, String jwtToken, boolean isNewMember) {
        // MemberImage 엔티티 목록을 MemberImageResponseDto 목록으로 변환
        List<MemberImageResponseDto> imageResponseDtoList = memberImages.stream()
                .map(this::convertToImageResponseDto)
                .collect(Collectors.toList());

        // Member 엔티티의 정보를 MemberWithKakaoResponseDto 객체로 설정
        return new MemberWithKakaoResponseDto(
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberAge(),
                member.getMemberGender(),
                member.getMemberAddress(),
                member.getMemberFindPasswordAnswer(),
                member.getMemberStyle(),
                member.getMemberRole(),
                jwtToken,
                imageResponseDtoList, // 이미지 정보 설정
                isNewMember
        );
    }

    private MemberImageResponseDto convertToImageResponseDto(MemberImage memberImage) {
        return new MemberImageResponseDto(memberImage.getMemberImageId(),memberImage.getMemberImageName(), memberImage.getMemberImageUrl());

    }

    private Member createUserWithKakao(OAuth2User oAuth2User,
                                      MemberWithKakaoRequestDto additionalInfo) {
        Member member = Member.builder()
                .memberEmail(oAuth2User.getAttribute("email"))
                .memberName(oAuth2User.getAttribute("name"))
                .memberAge(additionalInfo.getMemberAge())
                .memberGender(additionalInfo.getMemberGender())
                .memberAddress(additionalInfo.getMemberAddress())
                .memberFindPasswordAnswer(additionalInfo.getMemberFindPasswordAnswer())
                .memberPassword("") // 패스워드는 임의로 설정
                .memberRole("ROLE_USER")
                .build();
        memberRepository.save(member);

        return member;
    }

    public Member registerOrUpdateUser(OAuth2User oauthUser) {
        String email = oauthUser.getAttribute("email");
        if (email == null) {
            throw new IllegalArgumentException("Email not found from OAuth2User");
        }

        Member member = memberRepository.findByMemberEmail(email);
        if (member == null) {
            // New user registration
            member = new Member();
            member.setMemberEmail(email);
            member.setMemberName(oauthUser.getAttribute("name"));
            // Further attributes can be set here
            memberRepository.save(member);
            log.info("New user registered: {}", email);
        } else {
            // Update existing user details
            member.setMemberName(oauthUser.getAttribute("name"));
            memberRepository.save(member);
            log.info("User details updated: {}", email);
        }

        return member;
    }
}