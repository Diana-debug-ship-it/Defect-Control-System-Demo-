package diana.dev.quality_control_service.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "frame_checks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FrameCheckEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "frame_check_seq"
    )
    @SequenceGenerator(
            name = "frame_check_seq",
            sequenceName = "frame_check_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "frame_id", nullable = false)
    private String frameId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QualityStatus status;

    @Column(name = "defect_type", nullable = false)
    private String defectType;

    @Column(name = "confidence", nullable = false)
    private Double confidence;

}
