package art.aelaort;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Properties {
	@Value("${cloudflare.sub-domain}")
	private String subDomain;
	@Value("${cloudflare.root-domain}")
	private String rootDomain;
	@Value("${telegram.chat}")
	private String telegramChat;
	@Value("${telegram.token}")
	private String telegramToken;

	public String getDomain() {
		return subDomain + "." + rootDomain;
	}
}
