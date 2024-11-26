package springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 该类用于封装并发送消息给前端,会转化为json数据封装到响应体中.
 * <p>同样,该类中的属性名会作为转化成的json数据的属性名</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String title;
    private String message;
}
