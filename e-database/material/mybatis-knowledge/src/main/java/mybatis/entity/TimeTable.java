package mybatis.entity;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Year;

/**
 * 对应数据表 time_table 的实体类.
 * <p>这里使用了各种各样的字段类型,当然这些字段的类型都可以设置为字符串</p>
 */
@Data
public class TimeTable {
    private Long id;
    private String description;
    private Date date;
    private Time time;
    private java.util.Date dateTime;
    private Timestamp TimeStamp;
    private Year Year;
}
