// Entity와 비슷

package ultimatum.project.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 200 OK : 성공 */
    SUCCESS(200, "OK", "요청에 성공하였습니다."),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_INPUT_VALUE(400, "BAD_REQUEST", "입력값이 올바르지 않습니다."),
    BAD_REQUEST(400, "BAD_REQUEST","잘못된 요청입니다."),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    UNAUTHENTICATED_USERS(401, "UNAUTHORIZED","인증이 필요합니다."),

    /* 403 FORBIDDEN : 접근권한 없음 */
    ACCESS_DENIED(403, "FORBIDDEN","접근이 거부되었습니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    MEMBER_NOT_FOUND(404, "NOT_FOUND","해당 유저 정보를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(404, "NOT_FOUND","해당 정보를 찾을 수 없습니다."),

    /* 405 METHOD_NOT_ALLOWED : 지원하지 않는 HTTP Method */
    METHOD_NOT_ALLOWED(405, "METHOD_NOT_ALLOWED","허용되지 않은 요청입니다."),

    /* 409 CONFLICT : 데이터 중복 */
    DUPLICATE_RESOURCE(409, "CONFLICT","데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR */
    SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "예기치 못한 오류가 발생하였습니다.");



    private int status;
    private final String code;
    private final String message;



    public int getStatus() {
        return status;
    }
}

/**
 * 지금은 default값으로 모든 controller의 예외처리를 지정해두는 과정이지만
 * 만약에 특정 패키지만을 예외처리하고 싶다면,
 * @RestControllerAdvice(basePackages = "~~~~~")와 같이 패키지의 범위를 지정하여 특정 패키지 예외처리 할 수 있다고 함.
 */

/**
 * ControllerAdvice와 RestControllerAdvice의 차이는
 * ControllerAdvice의 경우 HTML에서 직접 사용자가 확인 할 수 있는 형태로 예외 처리를 진행하여 적절한 에러 페이지를 보여주거나,
 * 사용자에게 특정 알람을 띄워주는 용도
 *
 * RestControllerAdvice의 경우 예외 발생 시 json 형태로 결과를 반환하기 위해서사용한다.
 * 주로 API 서비스에서 발생하는 예외처리를 담당한다고함.
 * 참고하자면 API -> 사용자와 서버간에 데이터를 주고 받는 인터페이스라고 이해하면 편함.
 */