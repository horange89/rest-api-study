package me.study.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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
 *   애플리케이션과 가장 가까운 API 테스트방식은 Spring Boot Test 방식으로 진행하는 것이 편함.
 * */
@RunWith(SpringRunner.class)
@SpringBootTest // SpringBoot 테스트가 더 편하다. Mocking 할게 적고 편함
@AutoConfigureMockMvc // 슬라이싱테스트가 아닐때 MockMvc를 사용하기 위한 설정 -> 실제로 Repositry를 사용해서 테스트를 동작시킴
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

    @Test
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
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

        mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print()) // print() 를 통해서 출력하였던 모든 로그들은 andExpect를 통해서 검증할 수 있는 내용들이다.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    public void createEvent_BadRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
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
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print()) // print() 를 통해서 출력하였던 모든 로그들은 andExpect를 통해서 검증할 수 있는 내용들이다.
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
       EventDto eventDto = EventDto.builder().build();

       this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
               .andDo(print())
               .andExpect(status().isBadRequest())
       ;
    }

    @Test
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("Rest API Study")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,4,20,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 11, 3,20,11))
                .beginEventDateTime(LocalDateTime.of(2020,11,2,20,11))
                .endEventDateTime(LocalDateTime.of(2020,11,1,20,11))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
}
