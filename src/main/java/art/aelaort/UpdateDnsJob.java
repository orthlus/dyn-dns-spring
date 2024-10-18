package art.aelaort;

import art.aelaort.models.CloudflareDnsDto;
import art.aelaort.models.CloudflareRecords;
import art.aelaort.models.CloudflareZone;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateDnsJob {
	private final RestTemplate ifconfig;
	private final RestTemplate cloudflare;
	private final RestTemplate telegram;

	private final Properties properties;

	private String cloudflarePutRecordPath = "/zones/%s/dns_records/%s";

	private final AtomicReference<String> savedIpAddress = new AtomicReference<>("");

	@PostConstruct
	private void init() {
		fillCloudflarePathData();
	}

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
//	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES, initialDelay = 10)
	public void update() {
		String actualIp = readIfConfig();
		if (actualIp.equals(savedIpAddress.get())) {
			return;
		}
		log.info("'ip was changed, updating dns...'");

		ResponseEntity<String> response = saveIpToDns(dto(actualIp));
		if (response.getStatusCode().is2xxSuccessful()) {
			saveIpInMemory(actualIp);
			String logStr = "ip changed to " + actualIp;
			log.info(logStr);
			telegramLog(logStr);
		} else {
			String logStr = "error during update dns: " + response.getBody();
			log.error(logStr);
			telegramLog(logStr);
		}
	}

	private void telegramLog(String text) {
		String url = "/bot%s/sendmessage?chat_id={chat}&text={text}".formatted(properties.getTelegramToken());
		telegram.getForObject(url, String.class, properties.getTelegramChat(), text);
	}

	private void saveIpInMemory(String ip) {
		savedIpAddress.set(ip);
	}

	private ResponseEntity<String> saveIpToDns(CloudflareDnsDto cloudflareDnsDto) {
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

	private String readIfConfig() {
		return ifconfig.getForObject("/", String.class);
	}

	private CloudflareDnsDto dto(String ipAddress) {
		return new CloudflareDnsDto(ipAddress, properties.getDomain());
	}
}
