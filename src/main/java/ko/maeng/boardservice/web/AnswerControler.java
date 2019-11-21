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

    private Result valid(HttpSession session, Question question){
        if(!HttpSessionUtils.isLogin(session)){
            return Result.fail("로그인이 필요합니다");
        }
        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        if(!question.isSameWriter(loginUser)){
            return Result.fail("자신이 쓴 글만 수정, 삭제가 가능합니다.");
        }
        return Result.ok();
    }

    @PostMapping("")
    public String create(@PathVariable Long questionId, String contents, HttpSession session){
        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findById(questionId).get();
        Answer answer = new Answer(loginUser, question, contents);
        answerRepository.save(answer);
        return String.format("redirect:/questions/%d", questionId);
    }
}
