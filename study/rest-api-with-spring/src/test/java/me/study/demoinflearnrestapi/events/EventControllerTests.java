package me.study.demoinflearnrestapi.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *   웹에 관련된 슬라이싱 테스트
 * */
@RunWith(SpringRunner.class)
@WebMvcTest // 웹과 관련된 Bean이 등록됨
public class EventControllerTests {

    /**
     *  Mocking 되어 있는 Dispatcher Servlet을 상대로
     *  가짜 요청을 Dispatcher Servlet 보내고
     *  응답을 확인할 수 있는 기능
     * */
    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                )
        ;
    }
}
