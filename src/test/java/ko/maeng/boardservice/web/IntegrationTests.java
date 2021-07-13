package ko.maeng.boardservice.web;

import ko.maeng.boardservice.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class IntegrationTests {
    protected static final String REDIRECT_ROOT_URI = "redirect:/";

    @Autowired protected MockMvc mockMvc;

    public User factoryKitty() {
        User user = new User();
        user.setId(2L);
        user.setUserId("kitty");
        user.setName("kitty");
        user.setEmail("kitty@naver.com");
        user.setPassword("1234");
        return user;
    }
}
