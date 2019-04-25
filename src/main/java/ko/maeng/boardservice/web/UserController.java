package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.User;
import ko.maeng.boardservice.domain.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "/user/login";
    }

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session){
        User user = userRepository.findByUserId(userId);

        if(user == null){
            log.info("Login Failure!");
            return "redirect:/users/loginForm";
        }

        if(!user.matchPassword(password)){
            log.info("Login Failure!");
            return "redirect:/users/loginForm";
        }

        log.info("Login Success!");
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
        log.info("Logout!");
        return "redirect:/";
    }

    @GetMapping("/form")
    public String form(){
        return "/user/form";
    }

    @PostMapping("")
    public String create(User user){
        System.out.println("User: " + user);
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "redirect:/users/loginForm";
        }

        User sessionedUser = (User) HttpSessionUtils.getUserFromSession(session);
        if(!sessionedUser.matchId(id)){
            throw new IllegalStateException("You can't update the another user!");
        }

        model.addAttribute("user", userRepository.findById(id).get());
        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, HttpSession session){
        if(!HttpSessionUtils.isLogin(session)){
            return "redirect:/users/loginForm";
        }

        User sessionedUser = (User) HttpSessionUtils.getUserFromSession(session);
        if(!id.equals(sessionedUser.getId())){
            throw new IllegalStateException("You can't update the another user!");
        }

        User user = userRepository.findById(id).get();
        user.update(updatedUser);
        userRepository.save(user);
        return "redirect:/users";
    }
}
