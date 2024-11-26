package springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 封装四种用于表示日期和时间对象的dto信息类.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataFormatDto {
    private Date utilDate;
    private Timestamp sqlTimestamp;
    private java.sql.Date sqlDate; //只能表示日期,不能表示时间
    private Time sqlTime; //只能表示时间,不能表示日期
}
