package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.Question;
import ko.maeng.boardservice.domain.QuestionRepository;
import ko.maeng.boardservice.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model){
        model.addAttribute("question", questionRepository.findById(id).get());
        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            log.info("Login Please!");
            return "/users/loginForm";
        }
        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(id).get();
        if(!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        model.addAttribute("question", question);
        return "/qna/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, String title, String contents, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            log.info("Login Please!");
            return "/users/loginForm";
        }
        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(id).get();
        if(!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        question.update(title, contents);
        questionRepository.save(question);
        return String.format("redirect:/questions/%d", id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            log.info("Login Please!");
            return "/users/loginForm";
        }
        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(id).get();
        if(!question.isSameWriter(loginUser)){
            return "/users/loginForm";
        }

        questionRepository.deleteById(id);
        return "redirect:/";
    }
}
