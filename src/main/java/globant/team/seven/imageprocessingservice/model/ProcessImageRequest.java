package globant.team.seven.imageprocessingservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ProcessImageRequest {

    @Size(max = 36)
    private String id;

    @Size(max = 45)
    @NotBlank
    private String title;

    @Size(max = 100)
    private String description;

    @Size(max = 250)
    @NotBlank
    private String folder;

    private Double size;

    @NotNull
    private String imageStatus;

    @NotBlank
    @DateTimeFormat
    private Instant initialDate;

    @DateTimeFormat
    private Instant finalDate;

    @NotBlank
    private String emails;

}
