package mybatis.mapper;

import mybatis.entity.SimpleUserTable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 对应数据表 simple_user_table .
 */
public interface SimpleUserTableMapper {
    /**
     * 查询所有用户.
     * <p>这个方法很有可能是你创建的第一个 Mapper 接口方法.</p>
     *
     * @return 查询结果的List集合
     */
    @Select("SELECT * FROM simple_user_table")
    List<SimpleUserTable> selectAllUsers();

    /**
     * 如果输入的用户名在数据库中存在多个,则返回第一个,如果不存在则返回null.
     *
     * @param userName 要查询的用户名
     * @return 查询结果
     */
    SimpleUserTable selectUserByName(String userName);

    /**
     * 使用聚合函数查询用户总数.
     *
     * @return 查询结果
     */
    int userCount();

    /**
     * 这里使用 @Param 注解用来映射xml文件中的标识符.
     *
     * @param userName 用户名
     * @param isMale   性别,在数据库中对应的是 tinyint(1) 类型的数据
     * @param minAge   最小年龄
     * @param maxAge   最大年龄
     * @return 查询结果
     */
    List<SimpleUserTable> selectUserByConditions(@Param("name") String userName, @Param("isMale") Boolean isMale, @Param("ageBiggerThen") Byte minAge, @Param("ageSmallerThen") Byte maxAge);

    /**
     * 插入用户.
     *
     * @param user 要插入的用户的实体类,其中的字段名要与xml映射文件中的占位符一致.
     */
    void insertIntoUserTable(SimpleUserTable user);

    /**
     * 根据用户名修改对应用户的信息.
     * <p>注:创建方法时不可以将字符串与实体类在形参中混用</p>
     *
     * @param user 根据 checkName 字段查询用户,根据其他字段修改用户信息.其中 checkName 字段必须写上,不能为空.
     */
    void updateUserTable(SimpleUserTable user);

    /**
     * 根据用户名删除对应的用户.
     *
     * @param UserName 要删除的用户的用户名.
     */
    void deleteUserByName(String UserName);
}
