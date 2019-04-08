package pointcloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import broker.Informal;
import broker.acl.ACL;
import broker.acl.EmptyACL;
import util.yaml.YamlMap;

public class PointCloudConfig {
	private static final Logger log = LogManager.getLogger();

	public final String name;
	public final Path path;
	public final boolean transaction;

	public PointCloudConfig(String name, Path path, boolean transaction) {
		this.name = name;
		this.path = path;
		this.transaction = transaction;
	}
	
	public static PointCloudConfig ofPath(Path path, boolean transaction) {
		String name = pathToName(path);
		return new PointCloudConfig(name, path, transaction);
	}
	
	private static String pathToName(Path path) {
		String filename = path.getFileName().toString().trim();
		if(filename.isEmpty()) {
			return path.toString().trim();
		} else {
			return filename;
		}		
	}
	
	public ACL readACL() {
		String fileMetaName = name + ".yml";
		Path metaPath = path.resolve(fileMetaName);
		File metaFile = metaPath.toFile();
		try {
			if (metaPath.toFile().exists()) {
				YamlMap map;
				try(InputStream in = new FileInputStream(metaFile)) {
					map = YamlMap.ofObject(new Yaml().load(in));
				}
				return ACL.of(map.optList("acl").asStrings());
			}
			return EmptyACL.ADMIN;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e);
			return EmptyACL.ADMIN;
		}
	}
	
	public Informal readInformal() {
		String fileMetaName = name + ".yml";
		Path metaPath = path.resolve(fileMetaName);
		File metaFile = metaPath.toFile();
		try {
			if (metaPath.toFile().exists()) {
				YamlMap yamlMap;
				try(InputStream in = new FileInputStream(metaFile)) {
					yamlMap = YamlMap.ofObject(new Yaml().load(in));
				}
				return Informal.ofYaml(yamlMap);
			}
			return Informal.EMPTY;
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e);
			return Informal.EMPTY;
		}
	}
}
