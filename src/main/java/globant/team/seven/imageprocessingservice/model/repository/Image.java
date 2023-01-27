package globant.team.seven.imageprocessingservice.model.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "IMAGE", schema = "IMAGE_SERVICE", indexes = {
        @Index(name = "FOLDER_UNIQUE", columnList = "FOLDER", unique = true),
        @Index(name = "TITLE_UNIQUE", columnList = "TITLE", unique = true)
})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Size(max = 36)
    @Column(name = "ID", nullable = false, length = 36)
    private String id;

    @Size(max = 45)
    @NotNull
    @Column(name = "TITLE", nullable = false, length = 45)
    private String title;

    @Size(max = 100)
    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Size(max = 250)
    @NotNull
    @Column(name = "FOLDER", nullable = false, length = 250)
    private String folder;

    @Column(name = "SIZE")
    private Double size;

    @NotNull
    @Column(name = "STATUS")
    private String status; //In Progress //Failed o Completed

    @NotNull
    @Column(name = "BEGIN_DATE", nullable = false)
    private Instant beginDate;

    @Column(name = "END_DATE")
    private Instant endDate;

    @NotNull
    @Lob
    @Column(name = "EMAILS", nullable = false)
    private String emails;

}