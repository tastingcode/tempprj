package taco.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.articleread.repository.ArticleIdListRepository;
import taco.board.articleread.repository.ArticleQueryModel;
import taco.board.articleread.repository.ArticleQueryModelRepository;
import taco.board.articleread.repository.BoardArticleCountRepository;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.ArticleCreatedEventPayload;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ArticleCreatedEventHandler implements EventHandler<ArticleCreatedEventPayload> {
	private final ArticleIdListRepository articleIdListRepository;
	private final ArticleQueryModelRepository articleQueryModelRepository;
	private final BoardArticleCountRepository boardArticleCountRepository;

	@Override
	public void handle(Event<ArticleCreatedEventPayload> event) {
		ArticleCreatedEventPayload payload = event.getPayload();
		articleQueryModelRepository.create(
				ArticleQueryModel.create(payload),
				Duration.ofDays(1)
		);
		articleIdListRepository.add(payload.getBoardId(), payload.getArticleId(), 1000L);
		boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
	}

	@Override
	public boolean supports(Event<ArticleCreatedEventPayload> event) {
		return EventType.ARTICLE_CREATED == event.getType();
	}
}
