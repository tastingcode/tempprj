package taco.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.articleread.repository.ArticleQueryModelRepository;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.CommentCreatedEventPayload;
import taco.board.common.event.payload.CommentDeletedEventPayload;

@Component
@RequiredArgsConstructor
public class CommentDeletedEventHandler implements EventHandler<CommentDeletedEventPayload> {
	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<CommentDeletedEventPayload> event) {
		articleQueryModelRepository.read(event.getPayload().getArticleId())
				.ifPresent(articleQueryModel -> {
					articleQueryModel.updateBy(event.getPayload());
					articleQueryModelRepository.update(articleQueryModel);
				});
	}

	@Override
	public boolean supports(Event<CommentDeletedEventPayload> event) {
		return EventType.COMMENT_DELETED == event.getType();
	}
}
