package taco.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.CommentDeletedEventPayload;
import taco.board.hotarticle.repository.ArticleCommentCountRepository;
import taco.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {
	private final ArticleCommentCountRepository articleCommentCountRepository;

	@Override
	public void handle(Event<CommentDeletedEventPayload> event) {
		CommentDeletedEventPayload payload = event.getPayload();
		articleCommentCountRepository.createOrUpdate(
				payload.getArticleId(),
				payload.getArticleCommentCount(),
				TimeCalculatorUtils.calculateDurationToMidnight()
		);
	}

	@Override
	public boolean supports(Event<CommentDeletedEventPayload> event) {
		return EventType.COMMENT_DELETED == event.getType();
	}

	@Override
	public Long findArticleId(Event<CommentDeletedEventPayload> event) {
		return event.getPayload().getArticleId();
	}
}
