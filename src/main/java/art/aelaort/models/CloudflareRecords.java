package art.aelaort.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudflareRecords {
	@JsonProperty
	private List<Record> result;

	public String getRecordIdByName(String name) {
		return result.stream()
				.filter(zone -> zone.getName().equals(name))
				.map(Record::getId)
				.findFirst()
				.orElseThrow();
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	private static class Record {
		@JsonProperty
		private String id;
		@JsonProperty
		private String name;
	}
}
