package me.study.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.study.demoinflearnrestapi.common.BaseControllerTest;
import me.study.demoinflearnrestapi.common.RestDocsConfiguration;
import me.study.demoinflearnrestapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *   애플리케이션과 가장 가까운 API 테스트방식은 Spring Boot Test 방식으로 진행하는 것이 편함.
 * */

public class EventControllerTests extends BaseControllerTest {

    /**
     *  Mocking 되어 있는 Dispatcher Servlet을 상대로
     *  가짜 요청을 Dispatcher Servlet 보내고
     *  응답을 확인할 수 있는 기능
     * */
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
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
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an events"),
                                linkWithRel("profile").description("profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("HAL JSON TYPE")
                        ),
                        // 응답의 일부분만 검증하는 relaxedResponseFields를 사용하면 된다. 단점 : 정확하게 모든것을 검증하지 못한다.
                        //relaxedResponseFields(
                        responseFields(
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline or online"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("self event link"),
                                fieldWithPath("_links.query-events.href").description("query event link"),
                                fieldWithPath("_links.update-event.href").description("update event link"),
                                fieldWithPath("_links.profile.href").description("profile link")
                        )
                ));

    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
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
    @TestDescription("입력값이 비어 있는 경우에 에러가 발생하는 테스트")
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
    @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트")
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
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }
    
    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0, 30).forEach(this::generatedEvent);

        // When&Then -> Page는 0번부터 시작함
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
   }

   @Test
   @TestDescription("기존의 이벤트를 하나 조회하기")
   public void getEvent() throws Exception {
        //Given
       Event event = this.generatedEvent(100);

       // When & Then
       this.mockMvc.perform(get("/api/events/{id}", event.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("name").exists())
               .andExpect(jsonPath("id").exists())
               .andExpect(jsonPath("_links.self").exists())
               .andExpect(jsonPath("_links.profile").exists())
               .andDo(document("get-an-event"))
       ;
   }

   @Test
   @TestDescription("없는 이번트를 조회했을 때 404 응답 받기")
   public void getEvent404() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/api/events/11883"))
            .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generatedEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"))
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 이벤트 수정 실패")
    public void updateEventEmpty400() throws Exception {
        // Given
        Event event = this.generatedEvent(300);
        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                    )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEventWrong400() throws Exception {
        // Given
        Event event = this.generatedEvent(400);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                 )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generatedEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/123123123")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    // 이벤트 저장하는 로직
    private Event generatedEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2020,11,3,20,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2020, 11, 4,20,11))
                .beginEventDateTime(LocalDateTime.of(2020,11,5,20,11))
                .endEventDateTime(LocalDateTime.of(2020,11,6,20,11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }
}
