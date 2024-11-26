package org.giccqer.provider.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 提供者的数据传输对象,与消费者相同.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDto {
    private String name;
    private Integer age;
    private Long phone;
}
