package ramzanlabs.imessage.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {


    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/inbox")
    public String inbox() {
        return "inbox";
    }


    @GetMapping("/")
    public String index() {
        return "index";
    }


}
