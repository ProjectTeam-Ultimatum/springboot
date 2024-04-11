package ultimatum.project.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ultimatum.project.domain.dto.MemberRequestDto;
import ultimatum.project.domain.entity.member.Member;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;
import ultimatum.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입 시 비밀번호 암호화를 진행한다.
    // Spring Security 기능을 사용하기 위해서는 해당 암호화 기능을 진행하여아한다.
    // bCryptPasswordEncoder라는 암호화 클래스를 이용
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String createMember(MemberRequestDto memberRequestDto) {
        // 입력된 회원 정보 확인
        if (memberRequestDto.getMemberName() == null || memberRequestDto.getMemberPassword() == null || memberRequestDto.getMemberEmail() == null
                || memberRequestDto.getMemberName().isEmpty() || memberRequestDto.getMemberPassword().isEmpty() || memberRequestDto.getMemberEmail().isEmpty()
                || memberRequestDto.getMemberAge() == null || memberRequestDto.getMemberGender() == null || memberRequestDto.getMemberAddress() == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        // 회원 정보 생성 및 저장
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

        // 회원가입 완료 메시지 반환
        return "회원가입 완료: " + member.getMemberName();
    }

}