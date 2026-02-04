package taco.board.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.board.common.event.payload.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
	ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.TACO_BOARD_ARTICLE),
	ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, Topic.TACO_BOARD_ARTICLE),
	ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.TACO_BOARD_ARTICLE),
	COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.TACO_BOARD_COMMENT),
	COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.TACO_BOARD_COMMENT),
	ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.TACO_BOARD_LIKE),
	ARTICLE_UNLIKED(ArticleUnlikedEventPayload.class, Topic.TACO_BOARD_LIKE),
	ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.TACO_BOARD_VIEW)
	;

	private final Class<? extends EventPayload> payloadClass;
	private final String topic;

	public static EventType from(String type) {
		try {
			return valueOf(type);
		} catch (Exception e) {
			log.error("[EventType.from] type={}", type, e);
			return null;
		}
	}

	public static class Topic {
		public static final String TACO_BOARD_ARTICLE = "msa-board-article";
		public static final String TACO_BOARD_COMMENT = "msa-board-comment";
		public static final String TACO_BOARD_LIKE = "msa-board-like";
		public static final String TACO_BOARD_VIEW = "msa-board-view";
	}
}

