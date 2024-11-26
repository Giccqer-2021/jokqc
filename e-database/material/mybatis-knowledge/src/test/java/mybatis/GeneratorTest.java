package mybatis;

import mybatis.util.MybatisUtil;
import org.junit.jupiter.api.Test;

/**
 * 用这里的方法使用mybatis逆向工程生成实体类和mapper.
 */
public class GeneratorTest {
    /**
     * 别运行.
     *
     * @throws Exception 很多
     */
    @Test
    public void generate() throws Exception {
        MybatisUtil.generateEntityAndMapper();
    }
}
