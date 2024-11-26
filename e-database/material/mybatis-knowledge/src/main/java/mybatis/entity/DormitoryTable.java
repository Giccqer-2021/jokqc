package mybatis.entity;

import lombok.*;

/**
 * mybatis 自动生成的实体类,对应数据表 dormitory_table .
 * <p>与一般的实体类不同的是,其setter方法配置字符串时会自动去除两边的空格</p>
 */
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DormitoryTable {
    @Setter
    private Integer studentId;
    private String studentName;
    @Setter
    private Byte isMale;
    @Setter
    private Long studentPhone;
    private String notes;

    public void setStudentName(String studentName) {
        this.studentName = studentName == null ? null : studentName.trim();
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }
}