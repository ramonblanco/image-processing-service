package globant.team.seven.imageprocessingservice.controller;

import globant.team.seven.imageprocessingservice.model.ProcessImageRequest;
import globant.team.seven.imageprocessingservice.model.ProcessImageResponse;
import globant.team.seven.imageprocessingservice.service.ImageProcessingService;
import globant.team.seven.imageprocessingservice.service.ImageRequestService;
import globant.team.seven.imageprocessingservice.util.ValidFile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/image")
@Validated
public class ImageProcessingController {

    private final ImageProcessingService imageProcessingService;

    private final ImageRequestService imageRequestService;


    public ImageProcessingController(ImageProcessingService imageProcessingService,
                                     ImageRequestService imageRequestService) {
        this.imageProcessingService = imageProcessingService;
        this.imageRequestService = imageRequestService;
    }


    @PostMapping
    public ResponseEntity<ProcessImageResponse> getUUIDFromUploadedImage(@RequestParam("folder") @Valid @NotBlank(message = "Folder should not be empty or null") String folder,
                                                                         @RequestParam("file") @ValidFile MultipartFile file,
                                                                         @RequestParam("title") @Valid @NotBlank(message = "Title should not be empty or null") String title,
                                                                         @RequestParam("description") @Valid @NotBlank(message = "Description should not be empty or null") String description,
                                                                         @RequestHeader("targetEmails") @NotEmpty List<@Pattern(regexp="^[A-Za-z0-9._%+-]+@globant\\.com$", message = "Only globant domain emails are valid") String> targetEmails) {
        return new ResponseEntity<>(imageProcessingService.processImage(folder, file, title, description, targetEmails), HttpStatus.CREATED);
    }

    @GetMapping("/getByDates")
    public ResponseEntity<List<ProcessImageRequest>> getRequestByDates(
            @RequestParam("beginDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date beginDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {

        List<ProcessImageRequest> imagesRequest =
                imageRequestService.getImageProcessRequestByDates(beginDate.toInstant(), endDate.toInstant());
        return ResponseEntity.ok(imagesRequest);
    }

    @GetMapping
    public ResponseEntity<ProcessImageResponse> getStatusFromImageId(@RequestParam("imageId") @Valid @NotBlank(message = "Id should not be empty or null") String imageId) {
        return new ResponseEntity<>(imageProcessingService.getImageStatus(imageId), HttpStatus.OK);
    }
}
