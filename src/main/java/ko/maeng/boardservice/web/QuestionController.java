package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.Question;
import ko.maeng.boardservice.domain.QuestionRepository;
import ko.maeng.boardservice.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final QuestionRepository questionRepository;

    public QuestionController(QuestionRepository client){
        this.questionRepository = client;
    }

    @GetMapping("/form")
    public String form(HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            log.info("Login Please!");
            return "/users/loginForm";
        }

        return "/qna/form";
    }

    @PostMapping("")
    public String create(String title, String contents, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            log.info("Login Please!");
            return "/users/loginForm";
        }

        User sessionUser = (User) HttpSessionUtils.getUserFromSession(session);
        Question newQuestion = new Question(sessionUser, title, contents);

        questionRepository.save(newQuestion);

        return "redirect:/";
    }
}
