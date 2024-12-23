package art.aelaort;

import art.aelaort.models.RecordSets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class YandexDnsService {
	private final YandexIAMSupplier yandexIAMSupplier;
	private final RestTemplate yandexDns;
	private final Properties properties;
	@Value("${yandex.dns.zone_id}")
	private String zoneId;

	public String getSavedIp() {
		return yandexDns.exchange(
				"/dns/v1/zones/%s:listRecordSets".formatted(zoneId),
				HttpMethod.GET,
				entityBearerToken(yandexIAMSupplier.getToken()),
				RecordSets.class
		)
				.getBody()
				.getIpByName(properties.getDomainName());
	}

	public ResponseEntity<String> saveIpToDns(String ip) {
		return yandexDns.exchange(
				"/dns/v1/zones/%s:upsertRecordSets".formatted(zoneId),
				HttpMethod.POST,
				entityBearerToken(body(ip), yandexIAMSupplier.getToken()),
				String.class);
	}

	private String body(String ip) {
		return """
				{"replacements": [{
				  "name": "%s.",
				  "type": "A",
				  "ttl": "60",
				  "data": ["%s"]
				}]}
				""".formatted(properties.getDomainName(), ip);
	}

	private HttpEntity<?> entityBearerToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		return new HttpEntity<>(headers);
	}

	private HttpEntity<?> entityBearerToken(String body, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		return new HttpEntity<>(body, headers);
	}
}
