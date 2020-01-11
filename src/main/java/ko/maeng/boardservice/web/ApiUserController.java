package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.User;
import ko.maeng.boardservice.domain.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {
    private final UserRepository userRepository;

    public ApiUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User show(@PathVariable Long id) {
        return userRepository.findById(id).get();
    }
}
