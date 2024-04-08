package ultimatum.project.service;

import org.springframework.stereotype.Service;
import ultimatum.project.global.exception.CustomException;
import ultimatum.project.global.exception.ErrorCode;

@Service
public class SampleService {

    public String retrieveData() {
        // 간단한 예외를 발생시킵니다.
        throw new CustomException(ErrorCode.SERVER_ERROR);
    }

    public String testError() {
        throw new CustomException(ErrorCode.ACCESS_DENIED);
    }
}
