package art.aelaort;

import art.aelaort.models.CloudflareDnsDto;
import art.aelaort.models.CloudflareRecords;
import art.aelaort.models.CloudflareZone;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@Component
@RequiredArgsConstructor
public class CloudflareService {
	private final RestTemplate cloudflare;
	private final Properties properties;
	@Getter
	private String cloudflarePutRecordPath = "/zones/%s/dns_records/%s";

	@PostConstruct
	private void init() {
		fillCloudflarePathData();
	}

	public ResponseEntity<String> saveIpToDns(CloudflareDnsDto cloudflareDnsDto) {
		return cloudflare.exchange(cloudflarePutRecordPath, HttpMethod.PUT, new HttpEntity<>(cloudflareDnsDto), String.class);
	}

	@SuppressWarnings("DataFlowIssue")
	private void fillCloudflarePathData() {
		String zoneId = cloudflare.getForObject("/zones", CloudflareZone.class)
				.getZoneIdByName(properties.getRootDomain());
		String recordId = cloudflare.getForObject("/zones/%s/dns_records".formatted(zoneId), CloudflareRecords.class)
				.getRecordIdByName(properties.getDomain());
		cloudflarePutRecordPath = cloudflarePutRecordPath.formatted(zoneId, recordId);
	}

}
