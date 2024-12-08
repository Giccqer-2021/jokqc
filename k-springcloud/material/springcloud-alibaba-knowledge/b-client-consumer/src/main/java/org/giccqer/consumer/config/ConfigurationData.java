package org.giccqer.consumer.config;

import org.giccqer.consumer.feign.ConfigurationCenterFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class ConfigurationData {
    @Autowired
    private ConfigurationCenterFeign configurationCenterFeign;
    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
    public String config1;
    public String config2;
    public String config3;

    public void refresh() {
        ByteArrayResource resource = new ByteArrayResource(configurationCenterFeign.getConfiguration().getBytes(StandardCharsets.UTF_8));
        try {
            List<PropertySource<?>> yamlProperties = loader.load(null, resource);
            Object c1 = yamlProperties.getFirst().getProperty("custom-faces.smiling-face");
            Object c2 = yamlProperties.getFirst().getProperty("custom-faces.crying-face");
            Object c3 = yamlProperties.getFirst().getProperty("custom-faces.calm-face");
            config1 = c1 != null ? c1.toString() : null;
            config2 = c2 != null ? c2.toString() : null;
            config3 = c3 != null ? c3.toString() : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
