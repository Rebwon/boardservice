package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerControler {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public AnswerControler(AnswerRepository client, QuestionRepository question){
        this.answerRepository = client;
        this.questionRepository = question;
    }

    @PostMapping("")
    public String create(@PathVariable Long questionId, String contents, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            log.info("Login Please!");
            return "/users/loginForm";
        }

        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).get();
        Answer answer = new Answer(loginUser, question, contents);
        answerRepository.save(answer);
        return String.format("redirect:/questions/%d", questionId);
    }
}
