package taco.board.view.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.board.common.event.EventType;
import taco.board.common.event.payload.ArticleViewedEventPayload;
import taco.board.common.outboxmessagerelay.OutboxEventPublisher;
import taco.board.view.entity.ArticleViewCount;
import taco.board.view.repository.ArticleViewCountBackUpRepository;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackUpProcessor {
	private final OutboxEventPublisher outboxEventPublisher;
	private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;

	@Transactional
	public void backUp(Long articleId, Long viewCount) {
		int result = articleViewCountBackUpRepository.updateViewCount(articleId, viewCount);
		if (result == 0) {
			articleViewCountBackUpRepository.findById(articleId)
					.ifPresentOrElse(ignored -> {
							},
							() -> articleViewCountBackUpRepository.save(ArticleViewCount.init(articleId, viewCount)));
		}

		outboxEventPublisher.publish(
				EventType.ARTICLE_VIEWED,
				ArticleViewedEventPayload.builder()
						.articleId(articleId)
						.articleViewCount(viewCount)
						.build(),
				articleId
		);
	}
}
