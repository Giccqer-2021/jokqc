package mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应数据库表 simple_user_table 的实体类.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserTable {
    private Integer userId;
    private String userName;
    private Boolean userIsMale;
    private Byte userAge;
    private Long userPhone;
    private String userAddress;
    private String userComment;
    /**
     * checkName 并不映射数据表内对应的任何字段,仅用作查询时输入条件.
     */
    private String checkName;

    /**
     * 自定义的构造函数,用于将非必要填入的 checkName 排除在外.
     *
     * @param userId      用户ID
     * @param userName    用户名
     * @param userIsMale  性别
     * @param userAge     年龄
     * @param userPhone   电话号码
     * @param userAddress 地址
     * @param userComment 备注
     */
    public SimpleUserTable(Integer userId, String userName, Boolean userIsMale, Byte userAge, Long userPhone, String userAddress, String userComment) {
        this(userId, userName, userIsMale, userAge, userPhone, userAddress, userComment, null);
    }
}
