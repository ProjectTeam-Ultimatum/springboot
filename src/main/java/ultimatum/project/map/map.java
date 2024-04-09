package ultimatum.project.map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class map {

    @GetMapping("/hello")
    public String helloWorld() {
        return "hello!";
    }
}

