package art.aelaort.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CloudflareDnsDto {
	@NonNull
	@JsonProperty
	private String content;
	@NonNull
	@JsonProperty
	private String name;
	@JsonProperty
	private String type = "A";
	@JsonProperty
	private int ttl = 300;
}
