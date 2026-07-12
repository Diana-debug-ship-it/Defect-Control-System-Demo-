package diana.dev.quality_control_service.domain;


import diana.dev.quality_control_service.api.FrameUIDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SseNotificationService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter createConnection() {
        SseEmitter emitter = new SseEmitter(0L);
        this.emitters.add(emitter);

        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        emitter.onError((e) -> this.emitters.remove(emitter));

        return emitter;
    }


    public void broadcast(FrameUIDto uiDto) {
        for (SseEmitter emitter: emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("frame-check")
                        .data(uiDto));
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }

}
