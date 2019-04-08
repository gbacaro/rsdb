package griddb;

import java.util.LinkedHashMap;

import util.yaml.YamlMap;

public class Attribute {	
	public final byte id;
	public final int encoding;
	public final String name;
	
	public Attribute(byte id, int encoding, String name) {
		this.id = id;
		this.encoding = encoding;
		this.name = name;
	}

	public Object toYamlWithoutId() {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("encoding", encoding);
		map.put("name", name);
		return map;
	}
	
	public static Attribute ofYaml(YamlMap yamlmap, int id) {
		int encoding = yamlmap.getInt("encoding");
		String name = yamlmap.getString("name");
		return new Attribute((byte) id, encoding, name);	
	}

}
