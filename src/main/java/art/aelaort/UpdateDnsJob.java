package art.aelaort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
	private final RestTemplate telegram;

	private final Properties properties;

	private final AtomicReference<String> savedIpAddress = new AtomicReference<>("");
	private final IfConfigService ifConfigService;
	private final YandexDnsService yandexDnsService;

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
	public void update() {
		String actualIp = ifConfigService.getIp();
		if (actualIp.equals(savedIpAddress.get())) {
			return;
		}
		log.info("'ip was changed, updating dns...'");

		ResponseEntity<String> response = yandexDnsService.saveIpToDns(actualIp);
		if (response.getStatusCode().is2xxSuccessful()) {
			savedIpAddress.set(actualIp);

			String logStr = "ip changed to " + actualIp;
			log.info(logStr);
			telegramLog("update-dns\n%s\n%s".formatted(properties.getDomainName(), actualIp));
		} else {
			String logStr = "error during update dns";
			log.error("{}: {}", logStr, response.getBody());
			telegramLog(logStr);
		}
	}

	private void telegramLog(String text) {
		String url = "/bot%s/sendmessage?chat_id={chat}&text={text}".formatted(properties.getTelegramToken());
		telegram.getForObject(url, String.class, properties.getTelegramChat(), text);
	}
}
