package webapp.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public String index() {
        return "login";
    }

    @GetMapping("/ajax")
    public String anotherIndex() {
        return "ajax";
    }

    @GetMapping("/wait")
    public String anotherIndex2() {
        return "wait";
    }

    @GetMapping("/finish")
    public String anotherIndex3() {
        return "finish";
    }

}