package springboot.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自行封装的传递简单消息的消息类.
 * <p>实际应用过程中可能比这要复杂得多</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private String message;
}
