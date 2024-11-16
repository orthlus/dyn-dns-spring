package art.aelaort;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplatesConfig {
//	@Bean
	public RestTemplate cloudflare(RestTemplateBuilder restTemplateBuilder,
							 @Value("${cloudflare.api.token}") String token,
							 @Value("${cloudflare.api.url}") String url) {
		return restTemplateBuilder
				.rootUri(url)
				.defaultHeader("authorization", "Bearer " + token)
				.defaultHeader("content-type", "application/json")
				.build();
	}

	@Bean
	public RestTemplate yandexDns(RestTemplateBuilder restTemplateBuilder,
								  @Value("${yandex.dns.api.url}") String url) {
		return restTemplateBuilder
				.rootUri(url)
				.build();
	}

	@Bean
	public RestTemplate telegram(RestTemplateBuilder restTemplateBuilder,
							 @Value("${telegram.api}") String url) {
		return restTemplateBuilder
				.rootUri(url)
				.build();
	}

	@Bean
	public RestTemplate ifconfig(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}
}
