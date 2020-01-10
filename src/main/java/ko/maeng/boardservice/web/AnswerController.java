package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public AnswerController(AnswerRepository client, QuestionRepository question){
        this.answerRepository = client;
        this.questionRepository = question;
    }

    @PostMapping("")
    public Answer create(@PathVariable Long questionId, String contents, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)) {
            return null;
        }
        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).get();
        Answer answer = new Answer(loginUser, question, contents);
        return answerRepository.save(answer);
    }
}
