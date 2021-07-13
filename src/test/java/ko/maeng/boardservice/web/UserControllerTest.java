package ko.maeng.boardservice.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import ko.maeng.boardservice.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class UserControllerTest extends IntegrationTests {

    private static final String REDIRECT_USERS_LOGIN_FORM = "redirect:/users/loginForm";
    private static final String REDIRECT_USERS = "redirect:/users";
    private static final String USER_API = "/users";
    private static final String LOGIN_API = "/login";

    @Test
    @DisplayName("회원가입에 필요한 정보를 입력하고 회원가입한다.")
    void registerUser() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "sam");
        params.add("password", "123456");
        params.add("name", "sam kim");
        params.add("email", "sam151@gmail.com");

        final ResultActions actions = mockMvc.perform(post(USER_API)
            .params(params)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_USERS));
    }

    @Test
    @DisplayName("로그인 시 로그인 정보가 주어지지 않으면 loginForm으로 이동")
    void userIsNull() throws Exception {
        final ResultActions actions = mockMvc.perform(post(USER_API + LOGIN_API));

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_USERS_LOGIN_FORM));
    }

    @Test
    @DisplayName("로그인 시 패스워드가 일치하지 않으면 loginForm으로 이동")
    void misMatchPassword() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "rebwon");
        params.add("password", "515151");

        final ResultActions actions = mockMvc.perform(post(USER_API + LOGIN_API)
            .params(params)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_USERS_LOGIN_FORM));
    }

    @Test
    @DisplayName("로그인 정보가 일치하여 로그인 성공")
    void loginSuccess() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", "rebwon");
        params.add("password", "1234");

        final ResultActions actions = mockMvc.perform(post(USER_API + LOGIN_API)
            .params(params)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_ROOT_URI));
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

        final ResultActions actions = mockMvc.perform(get(USER_API + "/logout")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_ROOT_URI));
    }

    @Test
    @DisplayName("/user/list로 접근 시 모든 유저를 조회하여 모델에 담아 리턴")
    void findAllUser() throws Exception {
        final ResultActions actions = mockMvc.perform(get(USER_API));

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/user/list"))
            .andExpect(model().attributeExists("users"));
    }

    @Test
    @DisplayName("로그인 한 유저가 UpdateForm으로 이동")
    void updateForm() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());

        final ResultActions actions = mockMvc.perform(get(USER_API + "/2/form")
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
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("name", "jeremy");
        params.add("password", "1234567");
        params.add("email", "jeremy@gmail.com");

        final ResultActions actions = mockMvc.perform(put(USER_API + "/2")
            .session(session)
            .params(params)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_USERS));
    }
}