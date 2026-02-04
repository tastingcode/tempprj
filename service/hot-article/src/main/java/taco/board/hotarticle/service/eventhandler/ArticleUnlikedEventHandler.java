package taco.board.hotarticle.service.eventhandler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.ArticleUnlikedEventPayload;
import taco.board.hotarticle.repository.ArticleLikeCountRepository;
import taco.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload> {
    private final ArticleLikeCountRepository articleLikeCountRepository;


	@Override
	public void handle(Event<ArticleUnlikedEventPayload> event) {
		ArticleUnlikedEventPayload payload = event.getPayload();
		articleLikeCountRepository.createOrUpdate(
				payload.getArticleId(),
				payload.getArticleLikeCount(),
				TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<ArticleUnlikedEventPayload> event) {
		return EventType.ARTICLE_UNLIKED == event.getType();
	}

	@Override
	public Long findArticleId(Event<ArticleUnlikedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
