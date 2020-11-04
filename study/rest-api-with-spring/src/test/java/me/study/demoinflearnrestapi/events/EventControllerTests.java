package me.study.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *   웹에 관련된 슬라이싱 테스트
 * */
@RunWith(SpringRunner.class)
@WebMvcTest // 웹과 관련된 Bean이 등록됨 (Repository는 등록하지 않으므로 MockBean을 등록해서 해야 에러가 안남)
public class EventControllerTests {

    /**
     *  Mocking 되어 있는 Dispatcher Servlet을 상대로
     *  가짜 요청을 Dispatcher Servlet 보내고
     *  응답을 확인할 수 있는 기능
     * */
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("Rest API Study")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,3,20,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 11, 4,20,11))
                .beginEventDateTime(LocalDateTime.of(2020,11,5,20,11))
                .endEventDateTime(LocalDateTime.of(2020,11,6,20,11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();
        
        //event로 save를 요청하면 event를 Return 해라
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print()) // print() 를 통해서 출력하였던 모든 로그들은 andExpect를 통해서 검증할 수 있는 내용들이다.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        ;
    }
}
