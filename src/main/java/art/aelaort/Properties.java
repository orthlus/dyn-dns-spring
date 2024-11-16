package art.aelaort;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Properties {
	@Value("${telegram.chat}")
	private String telegramChat;
	@Value("${telegram.token}")
	private String telegramToken;
	@Value("${ifconfig.url}")
	private String ifconfigUrl;
	@Value("${domain.name}")
	private String domainName;
}
