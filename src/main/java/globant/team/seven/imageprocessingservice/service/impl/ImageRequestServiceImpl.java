package globant.team.seven.imageprocessingservice.service.impl;

import globant.team.seven.imageprocessingservice.model.ProcessImageRequest;
import globant.team.seven.imageprocessingservice.model.repository.Image;
import globant.team.seven.imageprocessingservice.repository.ImageRepository;
import globant.team.seven.imageprocessingservice.service.ImageRequestService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageRequestServiceImpl implements ImageRequestService {

    private final ImageRepository imageRepository;

    public ImageRequestServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<ProcessImageRequest> getImageProcessRequestByDates(Instant beginDate, Instant endDate) {
        List<Image> images = imageRepository.findByBeginDateBetween(beginDate, endDate);
        List<ProcessImageRequest> requests = new ArrayList<>();
        images.forEach(image -> {
            ProcessImageRequest imageRequest = ProcessImageRequest.builder()
                    .id(image.getId())
                    .title(image.getTitle())
                    .description(image.getDescription())
                    .folder(image.getFolder())
                    .size(image.getSize())
                    .imageStatus(image.getStatus())
                    .initialDate(image.getBeginDate())
                    .finalDate(image.getEndDate())
                    .emails(image.getEmails())
                    .build();
            requests.add(imageRequest);
        });
        return requests;
    }
}
