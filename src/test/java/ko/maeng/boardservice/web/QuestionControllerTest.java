package ko.maeng.boardservice.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

class QuestionControllerTest extends IntegrationTests {
    private static final String QUESTION_FORM = "/questions/form";
    private static final String QUESTION_URI = "/questions";

    @Test
    @DisplayName("로그인 하지 않은 사용자가 질문 작성 폼에 들어갈 경우 로그인 페이지로 302 리턴")
    void invalidUserRedirectLoginForm() throws Exception {
        final ResultActions actions = mockMvc.perform(get(QUESTION_FORM));

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/users/loginForm"));
    }

    @Test
    @DisplayName("로그인 한 유저는 질문 작성 폼으로 이동한다.")
    void loginUserGoToQuestionForm() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());

        final ResultActions actions = mockMvc.perform(get(QUESTION_FORM)
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/qna/form"));
    }

    @Test
    @DisplayName("로그인 한 사용자는 질문을 작성할 수 있다.")
    void loginUserCreateQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("title", "test title");
        params.add("contents", "test contents");

        final ResultActions actions = mockMvc.perform(post(QUESTION_URI)
            .session(session)
            .params(params)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_ROOT_URI));
    }

    @Test
    @DisplayName("특정 ID에 맞는 질문을 조회한다.")
    void showQuestionById() throws Exception {
        final ResultActions actions = mockMvc.perform(get(QUESTION_URI + "/1"));

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/qna/show"))
            .andExpect(model().attributeExists("question"));
    }

    @Test
    @DisplayName("로그인 한 사용자는 자신의 질문이 아닌 질문을 수정하는 수정 폼에 갈 수 없다.")
    void loginUserGotoQuestionUpdateFormIsNotSameWriter() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());

        final ResultActions actions = mockMvc.perform(get(QUESTION_URI + "/1/form")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/user/login"))
            .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    @DisplayName("로그인 한 사용자는 자신의 질문을 수정하는 수정 폼으로 갈 수 있다.")
    void loginUserGotoQuestionUpdateForm() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());

        final ResultActions actions = mockMvc.perform(get(QUESTION_URI + "/2/form")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(view().name("/qna/updateForm"))
            .andExpect(model().attributeExists("question"));
    }

    @Test
    @DisplayName("로그인 한 사용자는 자신의 질문을 수정할 수 있다.")
    void loginUserUpdateQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("title", "test title");
        params.add("contents", "test contents");

        final ResultActions actions = mockMvc.perform(put(QUESTION_URI + "/2")
            .session(session)
            .params(params)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/questions/2"));
    }

    @Test
    @DisplayName("로그인 한 사용자는 자신의 질문을 삭제할 수 있다.")
    void loginUserDeleteQuestion() throws Exception {
        User loginUser = new User();
        loginUser.setId(3L);
        loginUser.setUserId("jeremy");
        loginUser.setName("jeremy");
        loginUser.setEmail("jeremy@naver.com");
        loginUser.setPassword("1234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);

        final ResultActions actions = mockMvc.perform(delete(QUESTION_URI + "/3")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(REDIRECT_ROOT_URI));
    }
}