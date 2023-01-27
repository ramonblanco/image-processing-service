package globant.team.seven.imageprocessingservice.service;

import globant.team.seven.imageprocessingservice.model.ImageStatusEnum;
import globant.team.seven.imageprocessingservice.model.repository.Image;
import globant.team.seven.imageprocessingservice.repository.ImageRepository;
import globant.team.seven.imageprocessingservice.util.ManageDrive;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.Instant;
import java.util.List;

@Service
public class HelperService {

    private static final Logger log = LoggerFactory.getLogger(HelperService.class);

    private final ImageRepository imageRepository;
    private final ManageDrive manageDrive;

    private final EmailService emailService;

    public HelperService(ImageRepository imageRepository, ManageDrive manageDrive, EmailService emailService) {
        this.imageRepository = imageRepository;
        this.manageDrive = manageDrive;
        this.emailService = emailService;
    }



    @Transactional
    @Async
    public void continueProcessAsync(Image persistedImage, String folder, File file, String title, String description, List<String> targetEmails) {
        String UUID = persistedImage.getId();
        try {
            log.info("Waiting 15 Seconds");
            Thread.sleep(15000);
            new AsyncResult<>("hello world !!!!");
        } catch (InterruptedException e) {
            log.error("Error while waiting 5 seconds");
        }

        try {
            log.info("Uploading image with Id {}, title {} and description {} to Google drive folder {}",
                    persistedImage.getId(), title, description, folder);
            manageDrive.processImage(title, file, folder, description);
        } catch (Exception e) {
            log.error("Failed to upload image with Id {}, title {} and description {} to Google drive folder {}",
                    persistedImage.getId(), title, description, folder);
            updateImageStatusAndEndDate(ImageStatusEnum.FAILED, persistedImage.getId());
            e.printStackTrace();
            return;
        }
        deliverEmailToTargets(UUID, file, targetEmails);
        updateImageStatusAndEndDate(ImageStatusEnum.COMPLETED, persistedImage.getId());
    }

    private void deliverEmailToTargets(String UUID, File file, List<String> targetEmails) {
        log.info("Sending email to target emails: {}", String.join(",", targetEmails));
        emailService.sendEmailAttachment("Globant Hackaton - Team 7-", UUID, file, targetEmails);
    }

    private void updateImageStatusAndEndDate(ImageStatusEnum newStatusToUpdate, String imageId) {
        log.info("Updating status for image with id {} to {} status", imageId, newStatusToUpdate);
        imageRepository.updateImageStatus(newStatusToUpdate.getStatus(), Instant.now(), imageId);
    }

}
