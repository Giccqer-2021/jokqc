package org.giccqer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 测试用dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String name;
    private Boolean isMale;
    private String address;
    private Long phone;
    private String email;
}
