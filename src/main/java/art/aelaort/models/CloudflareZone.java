package art.aelaort.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudflareZone {
	@JsonProperty
	private List<Zone> result;

	public String getZoneIdByName(String name) {
		return result.stream()
				.filter(zone -> zone.getName().equals(name))
				.map(Zone::getId)
				.findFirst()
				.orElseThrow();
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Zone {
		@JsonProperty
		private String id;
		@JsonProperty
		private String name;
	}
}
