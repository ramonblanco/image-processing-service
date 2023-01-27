package globant.team.seven.imageprocessingservice.error;

import globant.team.seven.imageprocessingservice.model.error.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ImageProcessingErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ImageProcessingErrorHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileSizeLimitExceededException.class)
    protected ResponseEntity<Object> handleFileSizeLimitExceededException(
            FileSizeLimitExceededException ex, WebRequest request) {
        String errorMessage = ex.getLocalizedMessage();
        log.error(errorMessage, ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,errorMessage);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.httpStatus(), request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartExceptionException(
            MultipartException ex, WebRequest request) {
        String errorMessage = ex.getLocalizedMessage();
        log.error(errorMessage);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST,errorMessage);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.httpStatus(), request);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }
        String errorMessage = ex.getLocalizedMessage();
        log.error(errorMessage);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage,errors);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), errorResponse.httpStatus(), request);
    }
}
