package br.com.abimael.cursotestes.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlConfigLoader {
  private final Map<String, Object> config;

  public YamlConfigLoader(String filePath) {
    Yaml yaml = new Yaml();
    try (InputStream in = getClass().getClassLoader().getResourceAsStream(filePath)) {
      if (in == null) {
        throw new RuntimeException("Failed to find the YAML configuration file: " + filePath);
      }
      config = yaml.load(in);
    } catch (Exception e) {
      throw new RuntimeException(
              "Failed to load YAML configuration file. Error: " + e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public Object getNestedConfigValue(String... keys) {
    Map<String, Object> currentConfig = config;
    Object value = null;
    for (String key : keys) {
      value = currentConfig.get(key);
      if (value instanceof Map) {
        currentConfig = (Map<String, Object>) value;
      } else {
        break;
      }
    }
    return value;
  }
}
