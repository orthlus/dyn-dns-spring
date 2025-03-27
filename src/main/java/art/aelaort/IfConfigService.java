package art.aelaort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class IfConfigService {
	private final Properties properties;
	private final RestTemplate ifconfig;

	public String getIp() {
		try {
			return privateRequest();
		} catch (Exception e) {
			log.error("private ifconfig error: {}", e.getMessage());
			return publicRequest();
		}
	}

	private String privateRequest() {
		return ifconfig.getForObject(URI.create(properties.getIfconfigUrl()), String.class);
	}

	private String publicRequest() {
		return ifconfig.getForObject(URI.create("https://ifconfig.me/"), String.class);
	}
}
