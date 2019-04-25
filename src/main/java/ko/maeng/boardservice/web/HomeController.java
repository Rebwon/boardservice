package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.QuestionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final QuestionRepository questionRepository;

    public HomeController(QuestionRepository client){
        this.questionRepository = client;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        return "index";
    }
}
