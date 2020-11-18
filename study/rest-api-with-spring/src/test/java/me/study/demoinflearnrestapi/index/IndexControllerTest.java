package me.study.demoinflearnrestapi.index;

import me.study.demoinflearnrestapi.common.RestDocsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *   Index Controller Test.
 * */
@RunWith(SpringRunner.class)
@SpringBootTest // SpringBoot 테스트가 더 편하다. Mocking 할게 적고 편함
@AutoConfigureMockMvc // 슬라이싱테스트가 아닐때 MockMvc를 사용하기 위한 설정 -> 실제로 Repositry를 사용해서 테스트를 동작시킴
@AutoConfigureRestDocs // RestDocs 사용하기 위한 애노테이션
@Import(RestDocsConfiguration.class) // Rest Docs Customizing (Pretty Printing Process)
@ActiveProfiles("test")
public class IndexControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());
    }

}
