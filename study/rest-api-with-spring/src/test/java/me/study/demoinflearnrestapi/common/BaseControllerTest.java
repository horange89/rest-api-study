package me.study.demoinflearnrestapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.study.demoinflearnrestapi.events.EventRepository;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest // SpringBoot 테스트가 더 편하다. Mocking 할게 적고 편함
@AutoConfigureMockMvc // 슬라이싱테스트가 아닐때 MockMvc를 사용하기 위한 설정 -> 실제로 Repositry를 사용해서 테스트를 동작시킴
@AutoConfigureRestDocs // RestDocs 사용하기 위한 애노테이션
@Import(RestDocsConfiguration.class) // Rest Docs Customizing (Pretty Printing Process)
@ActiveProfiles("test")
@Ignore // 테스트를 가지고 있는 클래스가 아님
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;

}
