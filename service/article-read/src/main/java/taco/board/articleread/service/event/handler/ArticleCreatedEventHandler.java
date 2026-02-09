package taco.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.articleread.repository.ArticleQueryModel;
import taco.board.articleread.repository.ArticleQueryModelRepository;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.ArticleCreatedEventPayload;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {
		ArticleCreatedEventPayload payload = event.getPayload();
		articleQueryModelRepository.create(
				ArticleQueryModel.create(payload),
				Duration.ofDays(1)
		);
	}

	@Override
	public boolean supports(Event<ArticleCreatedEventPayload> event) {
		return EventType.ARTICLE_CREATED == event.getType();
	}
}
