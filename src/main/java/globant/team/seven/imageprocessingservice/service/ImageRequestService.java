package globant.team.seven.imageprocessingservice.service;

import globant.team.seven.imageprocessingservice.model.ProcessImageRequest;
import java.time.Instant;

import java.util.List;

public interface ImageRequestService {
    List<ProcessImageRequest> getImageProcessRequestByDates(Instant beginDate, Instant endDate);
}
