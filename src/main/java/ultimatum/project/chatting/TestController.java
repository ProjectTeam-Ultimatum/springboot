package ultimatum.project.chatting;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class TestController {

    @GetMapping("/connectTest")
    public String Test() {
        return "스프링& 뷰 연동테스트";
    }
}
