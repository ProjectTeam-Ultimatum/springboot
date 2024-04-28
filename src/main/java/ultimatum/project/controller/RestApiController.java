package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.dto.logInDTO.*;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.member.MemberImage;
import ultimatum.project.global.config.Security.auth.PrincipalDetails;
import ultimatum.project.global.config.Security.jwt.JwtProperties;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.member.MemberRepository;
import ultimatum.project.service.member.KakaoService;
import ultimatum.project.service.member.MemberService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Log4j2
public class RestApiController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtProperties jwtProperties;
    private final KakaoService kakaoService;

    // 모든 사람이 접근 가능
    @GetMapping("/home")
    public String home() {
        return "<h1>home</h1>";
    }

    // 로그아웃은 localstorage를 비워버리면 로그아웃 가능.
    // vue에서 Controller 없이도 가능하다.
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
    public ResponseEntity<String> join(@RequestPart("member") MemberRequestDto memberRequestDto, @RequestPart("files") List<MultipartFile> files) {
        memberRequestDto.setFiles(files);
        String result = memberService.createMember(memberRequestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/accessToken")
    public ResponseEntity<?> getKakaoAccessToken(@RequestParam String code) {
        try {
            // 카카오 서비스에서 액세스 토큰 가져오기
            String accessToken = kakaoService.getKakaoAccessToken(code);
            if (accessToken == null || accessToken.isEmpty()) {
                throw new IllegalArgumentException("액세스 토큰을 받아오지 못했습니다.");
            }

            KakaoUserInfoDTO1 kakaoUserInfoDTO1 = kakaoService.getKakaoUserInfo(accessToken);
            if (kakaoUserInfoDTO1 == null) {
                throw new IllegalArgumentException("사용자 정보를 받아오지 못했습니다.");
            }
            // 데이터베이스에서 사용자 조회 또는 저장 로직
            Member member = memberRepository.findByMemberEmail(kakaoUserInfoDTO1.getMemberEmail());
            boolean isNewMember = member == null;
            if (isNewMember) {
                member = new Member();
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
            }

            // JWT 토큰 생성
            String jwtToken = jwtProperties.generateToken(member);

            // 응답 헤더에 JWT 토큰 추가
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
log.info("jwtToken : "+jwtToken);
            // 응답 데이터 구성 및 반환
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("jwtToken", jwtToken);
            responseData.put("userName", member.getMemberName());
            responseData.put("email", member.getMemberEmail());
            responseData.put("isNewMember", isNewMember);

            return ResponseEntity.ok().headers(headers).body(responseData);

        } catch (Exception e) {
            // 로그로 더 많은 정보를 기록하면 추적이 용이해집니다.
            log.error("Error during Kakao login process: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed at step [Specific Step]: " + e.getMessage());
        }
    }

//    @PostMapping("/kakao-login")
//    public ResponseEntity<String> kakaoLogin(@AuthenticationPrincipal OAuth2User principal, @RequestParam KakaoUserInfoDto kakaoUserInfoDto) {
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자 인증에 실패했습니다.");
//        }
//
//        // OAuth2User에서 사용자 정보 가져오기
//        String name = principal.getAttribute("name");
//        String email = principal.getAttribute("email");
//
//        // DTO 준비 및 설정
//        MemberWithKakaoRequestDto memberRequestDto = new MemberWithKakaoRequestDto();
//        memberRequestDto.setMemberName(name);
//        memberRequestDto.setMemberEmail(email);
//
//        return memberService.processKakaoLogin(kakaoUserInfoDto);
//
//    }

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

//    @GetMapping("/user/info/detail")
//    @SecurityRequirement(name = "bearerAuth")
//    @ResponseBody
//    public ResponseEntity<String> getUserInfoDetail(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
//        }
//
//        String memberEmail = authentication.getName();
//
//        Member member = memberRepository.findByMemberEmail(memberEmail);
//
//        if (member == null) {
//            return ResponseEntity.badRequest().body("사용자 정보를 찾을 수 없습니다.");
//        }
//
//        String userDetails =
//                "사용자 이름: " + member.getMemberName() +
//                        ", 이메일: " + member.getMemberEmail() +
//                        ", 성별: " + member.getMemberGender() +
//                        ", 나이: " + member.getMemberAge() +
//                        ", 주소: " + member.getMemberAddress();
//
//        return ResponseEntity.ok(userDetails);
//    }


    @GetMapping("/user/info/detail")
    @SecurityRequirement(name = "bearerAuth")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserInfoDetail(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }

        String memberEmail = authentication.getName();
        // 멤버 정보와 함께 멤버 이미지 정보도 함께 로드합니다.
        Member member = memberRepository.findByMemberEmail(memberEmail);

        if (member == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "사용자 정보를 찾을 수 없습니다."));
        }

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("userName", member.getMemberName());
        userDetails.put("email", member.getMemberEmail());
        userDetails.put("gender", member.getMemberGender());
        userDetails.put("age", member.getMemberAge());
        userDetails.put("address", member.getMemberAddress());

        // 이미지 정보 추가: 멤버의 이미지 URL 리스트
        List<String> imageUrls = member.getMemberImages().stream().map(MemberImage::getMemberImageUrl).collect(Collectors.toList());
        userDetails.put("images", imageUrls);

        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/user/password")
    public ResponseEntity<String> changePassword(@RequestParam String currentPassword, @RequestParam String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().body("인증되지 않은 사용자입니다.");
        }

        String userEmail = authentication.getName();
        String result = memberService.changePassword(userEmail, currentPassword, newPassword);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/user/findpassword")
    public String findPassword(@RequestBody MemberFindPasswordRequestDto memberFindPasswordRequestDto) {
        memberService.memberCheck(memberFindPasswordRequestDto);
        return "success!";
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteMember(@RequestParam String password, @RequestParam String answer) {

        // 현재 인증된 사용자 정보 가져오기ㅌ
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(ErrorCode.BAD_REQUSET_USER);
        }

        String userEmail = authentication.getName(); // 현재 로그인한 사용자의 이메일 가져오기
        ResponseEntity<String> response;

        try {
            response = memberService.deleteMember(userEmail, password, answer);
        } catch (CustomException e) {
            response = ResponseEntity.badRequest().body(e.getMessage());
        }

        return response;
    }

    @PutMapping(path = "/signup-with-kakao", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateWithKakao(@RequestParam String accessToken, @ModelAttribute MemberWithKakaoRequestDto requestDto) {

        // 여기서 accessToken은 프론트엔드에서 받은 카카오 액세스 토큰입니다.
        // Service 메소드는 카카오 API로부터 사용자 정보를 조회하고 추가 정보와 결합하여 처리합니다.
        ResponseEntity<?> responseDto = memberService.registerOrLoginWithKakao(accessToken,requestDto);

        // 결과를 클라이언트에 반환합니다.
        return ResponseEntity.ok(responseDto.getBody());
    }

//    @GetMapping("/kakao/userinfo")
//    public ResponseEntity<?> getkakaoUserInfo(@AuthenticationPrincipal OAuth2User principal) {
//
//        if (principal == null) {
//            return ResponseEntity.status(401).build(); // Unauthorized
//        }
//
//        Member member = memberService.UpdateExistingMember(principal);
//        return ResponseEntity.ok(member);
//    }
}
