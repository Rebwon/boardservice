package ko.maeng.boardservice.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import ko.maeng.boardservice.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("로그인 시 로그인 정보가 주어지지 않으면 loginForm으로 이동")
    void userIsNull() throws Exception {
        final ResultActions actions = mockMvc.perform(post("/users/login"));

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/users/loginForm"));
    }

    @Test
    @DisplayName("로그인 시 패스워드가 일치하지 않으면 loginForm으로 이동")
    void misMatchPassword() throws Exception {
        final ResultActions actions = mockMvc.perform(post("/users/login")
            .param("userId", "rebwon")
            .param("password", "515151")
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/users/loginForm"));
    }

    @Test
    @DisplayName("로그인 정보가 일치하여 로그인 성공")
    void loginSuccess() throws Exception {
        final ResultActions actions = mockMvc.perform(post("/users/login")
            .param("userId", "rebwon")
            .param("password", "1234")
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/"));
    }

    @Test
    @DisplayName("로그인 한 유저는 로그아웃을 할 수 있다.")
    void logout() throws Exception {
        User user = new User();
        user.setUserId("rebwon");
        user.setName("rebwon");
        user.setEmail("rebwon@gmail.com");
        user.setPassword("1234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

        final ResultActions actions = mockMvc.perform(get("/users/logout")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/"));
    }

    @Test
    @DisplayName("/user/list로 접근 시 모든 유저를 조회하여 모델에 담아 리턴")
    void findAllUser() throws Exception {
        final ResultActions actions = mockMvc.perform(get("/users"));

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/user/list"))
            .andExpect(model().attributeExists("users"));
    }

    @Test
    @DisplayName("로그인 한 유저가 UpdateForm으로 이동")
    void updateForm() throws Exception {
        User loginUser = new User();
        loginUser.setId(2L);
        loginUser.setUserId("kitty");
        loginUser.setName("kitty");
        loginUser.setEmail("kitty@naver.com");
        loginUser.setPassword("1234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);

        final ResultActions actions = mockMvc.perform(get("/users/2/form")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/user/updateForm"))
            .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("로그인 한 유저가 자신의 정보를 변경")
    void update() throws Exception {
        User loginUser = new User();
        loginUser.setId(2L);
        loginUser.setUserId("kitty");
        loginUser.setName("kitty");
        loginUser.setEmail("kitty@naver.com");
        loginUser.setPassword("1234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);

        final ResultActions actions = mockMvc.perform(put("/users/2")
            .session(session)
            .param("name", "jeremy")
            .param("password", "1234567")
            .param("email", "jeremy@gmail.com")
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/users"));
    }
}