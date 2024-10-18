package art.aelaort.models;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CloudflareDnsDto {
	private final String content;
	private final String name;
	private final String type = "A";
	private final int ttl = 300;
}
