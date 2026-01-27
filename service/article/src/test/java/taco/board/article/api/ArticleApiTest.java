package taco.board.article.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import taco.board.article.service.request.ArticleCreateRequest;
import taco.board.article.service.response.ArticleResponse;

public class ArticleApiTest {
	RestClient restClient = RestClient.create("http://localhost:9000");

	@Test
	void createTest() {
		ArticleResponse response = create(new ArticleCreateRequest("hizzzz", "my content", 1L, 1L));
		System.out.println("response = " + response);
	}

	ArticleResponse create(ArticleCreateRequest request) {
		return restClient.post()
				.uri("/v1/articles")
				.body(request)
				.retrieve()
				.body(ArticleResponse.class);
	}

	@Test
	void readTest() {
		ArticleResponse response = read(274412118498242560L);
		System.out.println("response = " + response);
	}

	ArticleResponse read(Long articleId) {
		return restClient.get()
				.uri("/v1/articles/{articleId}", articleId)
				.retrieve()
				.body(ArticleResponse.class);
	}

	@Test
	void updateTest() {
		update(274412118498242560L);
		ArticleResponse response = read(274412118498242560L);
		System.out.println("response = " + response);
	}

	void update(Long articleId) {
		restClient.put()
				.uri("/v1/articles/{articleId}", articleId)
				.body(new ArticleUpdateRequest("hi update", "my content update"))
				.retrieve();
	}

	@Test
	void deleteTest() {
		restClient.delete()
				.uri("/v1/articles/{articleId}", 274412118498242560L)
				.retrieve();
	}

	@Getter
	@AllArgsConstructor
	static class ArticleCreateRequest {
		private String title;
		private String content;
		private Long writerId;
		private Long boardId;
	}

	@Getter
	@AllArgsConstructor
	static class ArticleUpdateRequest {
		private String title;
		private String content;
	}

}
