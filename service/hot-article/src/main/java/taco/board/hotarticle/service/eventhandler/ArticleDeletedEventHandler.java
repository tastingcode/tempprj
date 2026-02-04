package taco.board.hotarticle.service.eventhandler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.ArticleDeletedEventPayload;
import taco.board.hotarticle.repository.ArticleCreatedTimeRepository;
import taco.board.hotarticle.repository.HotArticleListRepository;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
	private final HotArticleListRepository hotArticleListRepository;
	private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

	@Override
	public void handle(Event<ArticleDeletedEventPayload> event) {
		ArticleDeletedEventPayload payload = event.getPayload();
		articleCreatedTimeRepository.delete(payload.getArticleId());
		hotArticleListRepository.remove(payload.getArticleId(), payload.getCreatedAt());

	}

	@Override
	public boolean supports(Event<ArticleDeletedEventPayload> event) {
		return EventType.ARTICLE_DELETED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleDeletedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
