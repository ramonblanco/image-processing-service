package globant.team.seven.imageprocessingservice.repository;

import globant.team.seven.imageprocessingservice.model.repository.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.time.Instant;
import java.util.List;

@DataJpaTest
class ImageRepositoryTest {
    @Autowired
    private ImageRepository imageRepository;


    @Test
    void findStatusByImageId() {
        List<Image> all = imageRepository.findAll();
        Assertions.assertTrue(all.isEmpty());

        Image image = new Image();
        image.setTitle("test_image.jpg");
        image.setDescription("test description for image");
        image.setFolder("/testing");
        image.setSize(12345678987d);
        image.setStatus("IN_PROGRESS");
        image.setBeginDate(Instant.now());
        image.setEmails("uriel.moreno@globant.com");

        imageRepository.save(image);

        Assertions.assertNotNull(image.getId());
    }

    @Test
    void findByBeginDateBetween() {
        List<Image> all = imageRepository.findAll();
        Assertions.assertTrue(all.isEmpty());

        Image image = new Image();
        image.setTitle("test_image.jpg");
        image.setDescription("test description for image");
        image.setFolder("/testing");
        image.setSize(12345678987d);
        image.setStatus("IN_PROGRESS");
        image.setBeginDate(Instant.now());
        image.setEmails("uriel.moreno@globant.com");

        imageRepository.save(image);

        List<Image> images = imageRepository.findByBeginDateBetween(
                Instant.now().minusSeconds(300),
                Instant.now().plusSeconds(300));

        Assertions.assertEquals(1, images.size());
        Assertions.assertEquals(image.getId(), images.get(0).getId());

    }

    @Test
    void findStatusByImageId_InvalidStatus() {
        Image image = new Image();
        image.setTitle("test_image.jpg");
        image.setDescription("test description for image");
        image.setSize(12345678987d);
        image.setStatus("CREATED");
        image.setBeginDate(Instant.now());
        image.setEmails("uriel.moreno@globant.com");

        Assertions.assertThrows(InvalidDataAccessApiUsageException.class,
                () -> imageRepository.save(image));
    }
}