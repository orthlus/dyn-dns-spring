package art.aelaort.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordSets {
	@JsonProperty
	List<RecordSet> recordSets;

	public String getIpByName(String name) {
		return recordSets.stream()
				.filter(recordSet -> recordSet.type.equals("A"))
				.filter(recordSet -> recordSet.name.equals(name + "."))
				.findFirst()
				.orElseThrow()
				.data
				.get(0);
	}
}
