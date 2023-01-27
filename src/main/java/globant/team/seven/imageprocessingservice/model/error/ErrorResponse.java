package globant.team.seven.imageprocessingservice.model.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErrorResponse(HttpStatus httpStatus, String errorMessage, List<String> specificErrors) {

    public ErrorResponse(HttpStatus httpStatus, String errorMessage) {
        this(httpStatus, errorMessage, null);
    }
}
