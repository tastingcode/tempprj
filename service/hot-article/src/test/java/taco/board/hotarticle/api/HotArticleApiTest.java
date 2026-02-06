package taco.board.hotarticle.api;

import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import taco.board.hotarticle.service.response.HotArticleResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HotArticleApiTest {
	RestClient restClient = RestClient.create("http://localhost:9004");

	@Test
	void readAllTest() {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		List<HotArticleResponse> responses = restClient.get()
				.uri("/v1/hot-articles/articles/date/{dateStr}", today)
				.retrieve()
				.body(new ParameterizedTypeReference<List<HotArticleResponse>>() {
				});

		for (HotArticleResponse response : responses) {
			System.out.println("response = " + response);
		}
	}
}
