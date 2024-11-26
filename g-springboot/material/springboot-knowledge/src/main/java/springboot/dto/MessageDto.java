package springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本类仅用于封装从后端发向前端的字符串数据.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String message;
}
