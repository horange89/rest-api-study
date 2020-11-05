package me.study.demoinflearnrestapi.events;

// Event에 너무 Annotation이 많아서 DTO로 분산처리
// 입력을 받을 때만 사용, 단점 : 중복
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
@Builder @NoArgsConstructor @AllArgsConstructor
@Data
public class EventDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime;
    @NotNull
    private LocalDateTime closeEnrollmentDateTime;
    @NotNull
    private LocalDateTime beginEventDateTime;
    @NotNull
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; // (optional)
    @Min(0)
    private int maxPrice; // (optional)
    @Min(0)
    private int limitOfEnrollment;
}
