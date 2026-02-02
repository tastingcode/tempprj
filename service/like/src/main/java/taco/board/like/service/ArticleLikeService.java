package taco.board.like.service;

import taco.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taco.board.like.entity.ArticleLike;
import taco.board.like.entity.ArticleLikeCount;
import taco.board.like.repository.ArticleLikeCountRepository;
import taco.board.like.repository.ArticleLikeRepository;
import taco.board.like.service.response.ArticleLikeResponse;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {
	private final Snowflake snowflake = new Snowflake();
	private final ArticleLikeRepository articleLikeRepository;
	private final ArticleLikeCountRepository articleLikeCountRepository;

	public ArticleLikeResponse read(Long articleId, Long userId) {
		return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
				.map(ArticleLikeResponse::from)
				.orElseThrow();
	}

	/**
	 * 비관적 락
	 * 방식1: update
	 */
	@Transactional
	public void likePessimisticLock1(Long articleId, Long userId) {
		articleLikeRepository.save(
				ArticleLike.create(
						snowflake.nextId(),
						articleId,
						userId
				)
		);

		int result = articleLikeCountRepository.increase(articleId);
		if (result == 0) {
			articleLikeCountRepository.save(
					ArticleLikeCount.init(articleId, 1L)
			);
		}
	}

	@Transactional
	public void unlikePessimisticLock1(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
				.ifPresent(articleLike -> {
					articleLikeRepository.delete(articleLike);
					articleLikeCountRepository.decrease(articleId);
				});
	}

	/**
	 * 비관적 락
	 * 방식2: select ... for update + update
	 */
	@Transactional
	public void likePessimisticLock2(Long articleId, Long userId) {
		articleLikeRepository.save(
				ArticleLike.create(
						snowflake.nextId(),
						articleId,
						userId
				)
		);
		ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId)
				.orElseGet(() -> ArticleLikeCount.init(articleId, 0L));
		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	}

	@Transactional
	public void unlikePessimisticLock2(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
				.ifPresent(articleLike -> {
					articleLikeRepository.delete(articleLike);
					ArticleLikeCount articleLikeCount = articleLikeCountRepository.findLockedByArticleId(articleId).orElseThrow();
					articleLikeCount.decrease();
				});
	}

	/**
	 * 낙관적 락
	 */
	@Transactional
	public void likeOptimisticLock(Long articleId, Long userId) {
		articleLikeRepository.save(
				ArticleLike.create(
						snowflake.nextId(),
						articleId,
						userId
				)
		);

		ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId)
				.orElseGet(() -> ArticleLikeCount.init(articleId, 0L));
		articleLikeCount.increase();
		articleLikeCountRepository.save(articleLikeCount);
	}

	@Transactional
	public void unlikeOptimisticLock(Long articleId, Long userId) {
		articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
				.ifPresent(articleLike -> {
					articleLikeRepository.delete(articleLike);
					ArticleLikeCount articleLikeCount = articleLikeCountRepository.findById(articleId).orElseThrow();
					articleLikeCount.decrease();
				});
	}

	public Long count(Long articleId){
		return articleLikeCountRepository.findById(articleId)
				.map(ArticleLikeCount::getLikeCount)
				.orElse(0L);
	}


}
