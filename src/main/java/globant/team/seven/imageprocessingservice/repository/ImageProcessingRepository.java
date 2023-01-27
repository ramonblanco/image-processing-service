package globant.team.seven.imageprocessingservice.repository;

import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ImageProcessingRepository {

    public String saveImageReference(String title, String description) {
        return saveAndGenerateUUID();
    }

    private String saveAndGenerateUUID() {

        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
