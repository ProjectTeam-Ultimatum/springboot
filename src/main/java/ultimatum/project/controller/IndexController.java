package ultimatum.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/login_page")
    public String loginPage() {
        return "login_page";
    }

    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/myprofile")
    public String myProfilePage() {
        return "myprofile";
    }

    @GetMapping("/userInfo")
    public String userInfoPage() {
        return "userInfo";
    }

    @GetMapping("/kakaologin")
    public String a() {
        return "kakaologin";
    }


}
