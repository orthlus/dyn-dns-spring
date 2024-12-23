package art.aelaort.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordSet {
	@JsonProperty
	String name;
	@JsonProperty
	String type;
	@JsonProperty
	List<String> data;
}
