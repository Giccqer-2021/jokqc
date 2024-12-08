package org.giccqer.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "CONFIGURATION-CENTER", url = "http://ip6-localhost:8848")
public interface ConfigurationCenterFeign {
    @GetMapping("/nacos/v1/cs/configs?dataId=config-faces.yml&group=DEFAULT_GROUP&tenant=ce2bb47e-4538-4d72-a705-adc1ef76fe55")
    String getConfiguration();
}
