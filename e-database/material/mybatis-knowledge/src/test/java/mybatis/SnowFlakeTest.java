package mybatis;

import mybatis.util.SnowFlakeUtil;
import org.junit.jupiter.api.Test;

/**
 * 本类用于测试雪花算法.
 */
public class SnowFlakeTest {
    /**
     * 获取当前时间戳,使用该时间戳作为雪花算法的起始时间戳(常量 START_TIME_STAMP ).
     */
    @Test
    public void getCurrentTimeMillis() {
        System.out.println("系统当前的时间戳为: " + System.currentTimeMillis());
    }

    /**
     * 测试位运算,获取运算结果.
     */
    @Test
    public void bitwiseOperationTest() {
        System.out.println(~(-1L << 12));
    }

    /**
     * 测试自定义的雪花算法,模拟在短时间内快速生成id的环境.
     */
    @Test
    public void generateIdTest() {
        System.out.println("使用雪花算法循环生成20个id: ");
        for (int i = 0; i < 20; i++) {
            System.out.println(SnowFlakeUtil.generateId());
        }
    }
}
