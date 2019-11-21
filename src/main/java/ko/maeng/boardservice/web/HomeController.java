package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final QuestionRepository questionRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public HomeController(QuestionRepository client){
        this.questionRepository = client;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
        log.info("Index Page");
        return "index";
    }
}
