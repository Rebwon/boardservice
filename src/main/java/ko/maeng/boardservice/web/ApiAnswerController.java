package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public ApiAnswerController(AnswerRepository client, QuestionRepository question){
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

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
        if(!HttpSessionUtils.isLogin(session)) {
            return Result.fail("로그인해야 합니다.");
        }
        Answer answer = answerRepository.findById(id).get();
        User loginUser = (User) HttpSessionUtils.getUserFromSession(session);
        if(!answer.isSameWriter(loginUser)){
            return Result.fail("자신의 글만 삭제가 가능합니다.");
        }
        answerRepository.deleteById(id);
        return Result.ok();
    }
}
