package diana.dev.quality_control_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FrameCheckRepository extends JpaRepository<FrameCheckEntity, Long> {
    Long countByHasDefectTrue();

    List<FrameCheckEntity> findTop10ByOrderByTimestampDesc();
}
