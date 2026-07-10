package diana.dev.quality_control_service.kafka;


import diana.dev.quality_control_service.api.FrameResultEvent;
import diana.dev.quality_control_service.domain.FrameCheckProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FrameResultEventConsumer {

    private final FrameCheckProcessor processor;

    @KafkaListener(
            topics = "${frame-check-result-topic}",
            groupId = "quality-control-group",
            containerFactory = "frameResultEventListenerFactory"
    )
    public void listen(FrameResultEvent event) {
        log.info("Received frame check event: delivery={}", event);
        processor.processFrameResultCheckEvent(event);
    }

}
