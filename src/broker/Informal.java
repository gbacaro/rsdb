package broker;

import java.util.Map;

import org.json.JSONWriter;

import util.JsonUtil;
import util.collections.ReadonlyList;
import util.yaml.YamlMap;

public class Informal {
	
	public final static Informal EMPTY = new Builder().build();
	
	public final String title;
	public final String description;
	public final ReadonlyList<String> tags;
	public final String acquisition_date;
	public final String corresponding_contact;
	
	public static Informal ofYaml(YamlMap yamlMap) {
		return new Builder(yamlMap).build();
	}
	
	private Informal(Builder builder) {
		title = builder.title;
		description = builder.description;
		tags = builder.tags;
		acquisition_date = builder.acquisition_date;
		corresponding_contact = builder.corresponding_contact;
	}
	
	public void writeYaml(Map<String, Object> map) {
		if (hasTitle()) {
			map.put("title", title);
		}
		if (hasDescription()) {
			map.put("description", description);
		}
		map.put("tags", tags);
		if (hasAcquisition_date()) {
			map.put("acquisition_date", acquisition_date);
		}
		if (has_corresponding_contact()) {
			map.put("corresponding_contact", corresponding_contact);
		}
	}
	
	public void writeJson(JSONWriter json) {
		JsonUtil.put(json, "title", title);
		JsonUtil.put(json, "description", description);
		JsonUtil.writeOptList(json, "tags", tags);
		JsonUtil.optPut(json, "acquisition_date", acquisition_date);
		JsonUtil.optPut(json, "corresponding_contact", corresponding_contact);
	}
	
	public Builder toBuilder() {
		return new Builder(this);
	}
	
	public boolean hasDescription() {
		return !description.isEmpty();
	}
	
	public boolean hasTitle() {
		return !title.isEmpty();
	}
	
	public boolean hasAcquisition_date() {
		return !acquisition_date.isEmpty();
	}
	
	public boolean has_corresponding_contact() {
		return !corresponding_contact.isEmpty();
	}
	
	public static class Builder {
		public String title = "";
		public String description = "";
		public ReadonlyList<String> tags = ReadonlyList.EMPTY;
		public String acquisition_date = "";
		public String corresponding_contact = "";
		
		public Builder() {}
		
		private Builder(Informal informal) {
			title = informal.title;
			description = informal.description;
			tags = informal.tags;
			acquisition_date = informal.acquisition_date;
			corresponding_contact = informal.corresponding_contact;
		}
		
		public Builder(YamlMap yamlMap) {
			title = yamlMap.optString("title", "");
			description = yamlMap.optString("description", "");
			tags = yamlMap.optList("tags").asReadonlyStrings();
			acquisition_date = yamlMap.optString("acquisition_date", "");
			corresponding_contact = yamlMap.optString("corresponding_contact", "");
		}
		
		public Informal build() {
			return new Informal(this);
		}
		
		public void setTags(String... tags) {
			this.tags = ReadonlyList.of(tags);
		}
	}
	
	@Override
	public String toString() {
		return "Informal [title=" + title + ", description=" + description + ", tags=" + tags + ", acquisition_date="
				+ acquisition_date + "]";
	}
}
