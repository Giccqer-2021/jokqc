package org.giccqer.consumer.controller;

import org.giccqer.consumer.config.ConfigurationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadConfigurationController {

    @Autowired
    private ConfigurationData configurationData;

    @GetMapping("/get-configs")
    public String getConfig() {
        configurationData.refresh();
        return "<p>从nacos配置中心读取到的配置: " + configurationData.config1 + " " + configurationData.config2 + " " + configurationData.config3 + "</p>";
    }
}
