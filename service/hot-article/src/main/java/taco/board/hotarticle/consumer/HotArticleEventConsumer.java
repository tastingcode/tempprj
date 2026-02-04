package taco.board.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import taco.board.common.event.Event;
import taco.board.common.event.EventPayload;
import taco.board.common.event.EventType;
import taco.board.hotarticle.service.HotArticleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
	private final HotArticleService hotArticleService;

	@KafkaListener(topics = {
			EventType.Topic.TACO_BOARD_ARTICLE,
			EventType.Topic.TACO_BOARD_COMMENT,
			EventType.Topic.TACO_BOARD_LIKE,
			EventType.Topic.TACO_BOARD_VIEW
	})
	public void listen(String message, Acknowledgment ack){
		log.info("[HotArticleEventConsumer.listen] received message={}", message);
		Event<EventPayload> event = Event.fromJson(message);
		if (event != null){
			hotArticleService.handleEvent(event);
		}
		ack.acknowledge();
	}
}
