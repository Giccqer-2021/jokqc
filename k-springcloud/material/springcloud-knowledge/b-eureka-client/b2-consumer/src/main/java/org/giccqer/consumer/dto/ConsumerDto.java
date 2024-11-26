package org.giccqer.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消费者的数据传输对象,与提供者相同.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerDto {
    private String name;
    private Integer age;
    private Long phone;
}
