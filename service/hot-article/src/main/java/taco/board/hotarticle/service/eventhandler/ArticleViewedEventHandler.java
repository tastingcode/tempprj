package taco.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.ArticleViewedEventPayload;
import taco.board.hotarticle.repository.ArticleViewCountRepository;
import taco.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload> {
	private final ArticleViewCountRepository articleViewCountRepository;

	@Override
	public void handle(Event<ArticleViewedEventPayload> event) {
		ArticleViewedEventPayload payload = event.getPayload();
		articleViewCountRepository.createOrUpdate(
				payload.getArticleId(),
				payload.getArticleViewCount(),
				TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<ArticleViewedEventPayload> event) {
		return EventType.ARTICLE_UNLIKED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleViewedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
