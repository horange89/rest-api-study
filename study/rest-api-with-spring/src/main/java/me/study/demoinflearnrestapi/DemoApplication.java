package me.study.demoinflearnrestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // 공용으로 사용하기 때문에, Application 단에서 Bean으로 등록해주고 사용한다.
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
