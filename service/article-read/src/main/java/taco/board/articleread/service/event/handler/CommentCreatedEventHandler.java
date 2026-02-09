package taco.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.articleread.repository.ArticleQueryModelRepository;
import taco.board.common.event.Event;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.CommentCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {
	private final ArticleQueryModelRepository articleQueryModelRepository;

	@Override
	public void handle(Event<CommentCreatedEventPayload> event) {
		articleQueryModelRepository.read(event.getPayload().getArticleId())
				.ifPresent(articleQueryModel -> {
					articleQueryModel.updateBy(event.getPayload());
					articleQueryModelRepository.update(articleQueryModel);
				});
	}

	@Override
	public boolean supports(Event<CommentCreatedEventPayload> event) {
		return EventType.COMMENT_CREATED == event.getType();
	}
}
