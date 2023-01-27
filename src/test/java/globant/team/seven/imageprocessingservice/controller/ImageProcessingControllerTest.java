package globant.team.seven.imageprocessingservice.controller;

import globant.team.seven.imageprocessingservice.service.ImageRequestService;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ImageProcessingControllerTest {

    @Mock
    private ImageRequestService imageRequestService;

    @InjectMocks
    private ImageProcessingController imageProcessingController;

    @Test
    void getRequestByDates() {
        DateTime beginDate = DateTime.now().minusHours(5);


    }
}
