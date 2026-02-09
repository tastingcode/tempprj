package taco.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.articleread.repository.ArticleQueryModel;
import taco.board.articleread.repository.ArticleQueryModelRepository;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.ArticleCreatedEventPayload;
import taco.board.common.event.payload.ArticleUpdatedEventPayload;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleUpdatedEventHandler implements EventHandler<ArticleUpdatedEventPayload> {

	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<ArticleUpdatedEventPayload> event) {
		articleQueryModelRepository.read(event.getPayload().getArticleId())
				.ifPresent(articleQueryModel -> {
					articleQueryModel.updateBy(event.getPayload());
					articleQueryModelRepository.update(articleQueryModel);
				});
	}

	@Override
	public boolean supports(Event<ArticleUpdatedEventPayload> event) {
		return EventType.ARTICLE_UPDATED == event.getType();
	}
}
