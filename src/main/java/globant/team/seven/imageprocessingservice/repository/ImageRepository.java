package globant.team.seven.imageprocessingservice.repository;


import globant.team.seven.imageprocessingservice.model.repository.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    boolean existsByFolderAndTitle(String folder, String title);

    @Query("select status from Image where id = :imageId")
    String findStatusByImageId(@Param("imageId") String imageId);

    List<Image> findByBeginDateBetween(Instant beginDate, Instant endDate);

    @Modifying
    @Query("delete from Image I where I.folder = :folder and I.title=:title")
    void deleteImageByFolderAndTitle(@Param("folder") String folder, @Param("title") String title);

    @Modifying
    @Query("update Image I set I.status = :status, I.endDate = :endDate  where I.id = :imageId")
    int updateImageStatus (@Param(value = "status") String status, @Param(value = "endDate") Instant endDate,
                           @Param(value = "imageId") String imageId);

}
