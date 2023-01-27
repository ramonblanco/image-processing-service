package globant.team.seven.imageprocessingservice.service;


import globant.team.seven.imageprocessingservice.model.ImageStatusEnum;
import globant.team.seven.imageprocessingservice.model.ProcessImageResponse;
import globant.team.seven.imageprocessingservice.model.repository.Image;
import globant.team.seven.imageprocessingservice.repository.ImageRepository;
import globant.team.seven.imageprocessingservice.util.FileHelper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
public class ImageProcessingService {

    private static final Logger log = LoggerFactory.getLogger(ImageProcessingService.class);
    private final ImageRepository imageRepository;
    private final HelperService helperService;

    public ImageProcessingService(ImageRepository imageRepository, HelperService helperService) {
        this.imageRepository = imageRepository;
        this.helperService = helperService;
    }


    @Transactional
    public ProcessImageResponse processImage(String folder, MultipartFile file, String title, String description, List<String> targetEmails) {
        deleteIfImageExistsInDb(folder, title);
        log.info("Persisting image in DB with folder {}, title {} and description {}", folder, title, description);
        Image persistedImage = imageRepository.save(buildImageEntity(folder, file, title, description, targetEmails));
        log.info("Persisted image in DB  {}", persistedImage);
        String UUID = persistedImage.getId();
        File javaFile;
        try {
            javaFile = FileHelper.multipartToFile(file, title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        helperService.continueProcessAsync(persistedImage, folder, javaFile, title, description, targetEmails);

        return new ProcessImageResponse(UUID);
    }


    private Image buildImageEntity(String folder, MultipartFile file, String title, String description,
                                   List<String> targetEmails) {
        return Image.builder().title(title).description(description).folder(folder).size((double) file.getSize()).
        status(ImageStatusEnum.IN_PROGRESS.getStatus()).beginDate(Instant.now()).emails(String.join(",", targetEmails)).build();
    }

    public ProcessImageResponse getImageStatus(String imageId) {
        String statusByImageId = imageRepository.findStatusByImageId(imageId);
        return new ProcessImageResponse(imageId, statusByImageId);
    }

    private void deleteIfImageExistsInDb(String folder, String title) {
        boolean existsByFolderAndTitle = imageRepository.existsByFolderAndTitle(folder, title);
        if (existsByFolderAndTitle) {
            log.info("Deleting from DB previous existing file with folder: {} and title: {}", folder, title);
            imageRepository.deleteImageByFolderAndTitle(folder, title);
        }

    }



}
