package ko.maeng.boardservice.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ko.maeng.boardservice.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;

class ApiAnswerControllerTest extends IntegrationTests {

    @Test
    @DisplayName("1번 질문에 Kitty라는 사용자가 답변을 작성한다.")
    void loginUserAnswerQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());

        final ResultActions actions = mockMvc.perform(post("/api/questions/1/answers")
            .session(session)
            .param("contents", "test answers")
        );

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.contents").value("test answers"))
            .andExpect(jsonPath("$.writer.id").value(2))
            .andExpect(jsonPath("$.writer.userId").value("kitty"))
            .andExpect(jsonPath("$.question.id").value(1))
            .andExpect(jsonPath("$.question.writer.id").value(1))
            .andExpect(jsonPath("$.question.countOfAnswer").value(1));
    }

    @Test
    @DisplayName("3번 질문에 달린 답변에서 자신의 답변이 아닌 경우 삭제가 불가능하다.")
    void loginUserIsNotWriterDeletedAnswer() throws Exception {
        User user = new User();
        user.setUserId("rebwon");
        user.setName("rebwon");
        user.setEmail("rebwon@gmail.com");
        user.setPassword("1234");
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);

        final ResultActions actions = mockMvc.perform(delete("/api/questions/3/answers/1")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.valid").value(false))
            .andExpect(jsonPath("$.errorMessage").value("자신의 글만 삭제가 가능합니다."));
    }

    @Test
    @DisplayName("3번 질문에 달린 한 개의 답변을 삭제한다.")
    void loginUserDeletedAnswer() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, factoryKitty());

        final ResultActions actions = mockMvc.perform(delete("/api/questions/3/answers/1")
            .session(session)
        );

        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.valid").value(true))
            .andExpect(jsonPath("$.errorMessage").doesNotExist());
    }
}