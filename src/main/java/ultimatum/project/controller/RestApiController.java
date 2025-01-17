package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultimatum.project.domain.dto.logInDTO.KakaoUserInfoDTO1;
import ultimatum.project.domain.dto.logInDTO.MemberFindPasswordRequestDto;
import ultimatum.project.domain.dto.logInDTO.MemberRequestDto;
import ultimatum.project.domain.dto.logInDTO.MemberWithKakaoRequestDto;
import ultimatum.project.domain.dto.logInDTO.MemberUpdateRequestDto;
import ultimatum.project.domain.dto.logInDTO.UpdateStyleDto;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.domain.entity.member.MemberImage;
import ultimatum.project.global.config.Security.auth.PrincipalDetails;
import ultimatum.project.global.config.Security.jwt.JwtProperties;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.member.MemberRepository;
import ultimatum.project.service.member.KakaoService;
import ultimatum.project.service.member.MemberImageService;
import ultimatum.project.service.member.MemberService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Log4j2
public class RestApiController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtProperties jwtProperties;
    private final KakaoService kakaoService;
    private final MemberImageService memberImageService;

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
    public ResponseEntity<String> join(
            @RequestPart("member") MemberRequestDto memberRequestDto,
            @RequestPart("files") List<MultipartFile> files) {
        memberRequestDto.setFiles(files);
        String result = memberService.createMember(memberRequestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/accessToken")
    public ResponseEntity<?> getKakaoAccessToken(@RequestParam String code) {

            String accessToken = memberService.getUserAccessTokenFromKaKao(code);
           KakaoUserInfoDTO1 kakaoUserInfoDTO1 = memberService.getUserInfoFromKakao(accessToken);
            // 데이터베이스에서 사용자 조회 또는 저장 로직
            Member member = memberRepository.findByMemberEmail(kakaoUserInfoDTO1.getMemberEmail());
            boolean isNewMember = member == null;
            if (isNewMember) {
                member = memberService.registerOrLookupMember(kakaoUserInfoDTO1);
            }

            // JWT 토큰 생성
            String jwtToken = jwtProperties.generateToken(member);
            // 응답 헤더에 JWT 토큰 추가
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(jwtToken);
            log.info("jwtToken : " + jwtToken);

            // 응답 데이터 구성 및 반환
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("jwtToken", jwtToken);
            responseData.put("kakaoAccessToken", accessToken);
            responseData.put("userName", member.getMemberName());
            responseData.put("email", member.getMemberEmail());
            responseData.put("isNewMember", isNewMember);

            return ResponseEntity.ok().headers(headers).body(responseData);
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
        userDetails.put("memberId",member.getMemberId());
        userDetails.put("userName", member.getMemberName());
        userDetails.put("email", member.getMemberEmail());
        userDetails.put("gender", member.getMemberGender());
        userDetails.put("age", member.getMemberAge());
        userDetails.put("address", member.getMemberAddress());
        userDetails.put("memberStyle", member.getMemberStyle()); // 스타일 정보 추가
        userDetails.put("needSurvey", member.getMemberStyle() == null); // 설문 필요 여부 추가

        // 이미지 정보 추가: 멤버의 이미지 URL 리스트
        List<String> imageUrls = member.getMemberImages().stream()
                .map(MemberImage::getMemberImageUrl)
                .collect(Collectors.toList());
        userDetails.put("images", imageUrls);

        return ResponseEntity.ok(userDetails);
    }

    @PostMapping("/user/password")
    public ResponseEntity<String> changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
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

    @PostMapping("/user/updateStyle")
    public ResponseEntity<String> updateMemberStyle(@RequestBody UpdateStyleDto updateStyleDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증되지 않은 사용자입니다.");
        }
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String userEmail = principal.getUsername(); // 인증된 사용자의 이메일을 가져옴

        try {
            String result = memberService.updateMemberStyle(userEmail, updateStyleDto.getMemberStyle());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트 과정에서 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteMember(
            @RequestParam String password,
            @RequestParam String answer) {

        // 현재 인증된 사용자 정보 가져오기
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

    @PostMapping("/user/update-info")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> updateMemberInfo(
            @RequestParam String userEmail,
            @RequestBody MemberUpdateRequestDto updateRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserEmail = authentication.getName();

        if (!loggedInUserEmail.equals(userEmail)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        return memberService.updateMemberInfo(userEmail, updateRequestDto);
    }

    // 프로필 이미지 업데이트
    @PostMapping("/profile-image")
    public ResponseEntity<String> updateProfileImage(
            Authentication authentication,
            @RequestParam("imageFile") MultipartFile imageFile
    ) {
        try {
            // PrincipalDetails에서 Member 객체 추출
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Member loggedInMember = principalDetails.getUser();

            // 이미지 파일을 사용하여 이미지 업데이트 로직 수행
            MemberImage updatedImage = memberImageService.updateMemberImage(loggedInMember, imageFile);
            return ResponseEntity.ok("프로필 이미지가 업데이트되었습니다.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업데이트 실패: " + e.getMessage());
        }
    }

    @PutMapping(path = "/signup-with-kakao", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateWithKakao(@RequestParam String accessToken, @ModelAttribute MemberWithKakaoRequestDto requestDto) {

        // 여기서 accessToken은 프론트엔드에서 받은 카카오 액세스 토큰입니다.
        // Service 메소드는 카카오 API로부터 사용자 정보를 조회하고 추가 정보와 결합하여 처리합니다.
        ResponseEntity<?> responseDto = memberService.registerOrLoginWithKakao(accessToken, requestDto);

        // 결과를 클라이언트에 반환합니다.
        return ResponseEntity.ok(responseDto.getBody());
    }


}
