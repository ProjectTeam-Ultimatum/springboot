package ultimatum.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UltimatumApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHandleCustomException() throws Exception {
        // 예외가 발생하는 API 엔드포인트 호출
        ResultActions resultActions = mockMvc.perform(get("/api/sample")
                .contentType(MediaType.APPLICATION_JSON));

        // 상태코드 500과 ErrorResponse 검증
        resultActions.andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("예기치 못한 오류가 발생하였습니다."));
    }
}
