package mybatis.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * mybatis工具类.
 * <p>有两个方法,一个为启动mybatis增删改查功能的基本架构,一个用于生成mybatis逆向工程文件</p>
 */
public class MybatisUtil {
    /**
     * 启动mybatis的基本架构,包含打开会话,关闭会话,处理异常等功能.
     * <p>用户需要传入相应的方法来处理sql语句,使用 {@link MapperOperator} 接口封装该方法</p>
     *
     * @param mapperInterface 自定义的mapper接口
     * @param operator        使用该接口后要执行的数据操作
     * @param commit          是否提交事务,如果为否则在操作结束后回滚事务
     * @param <T>             mapper接口的泛型
     */
    public static <T> void operate(Class<T> mapperInterface, MapperOperator<T> operator, boolean commit) {
        SqlSession session = null;
        try {
            InputStream input = Resources.getResourceAsStream("mybatisConfig.xml"); // 加载 resources 文件夹下的配置文件
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(input);
            session = factory.openSession();
            T mapper = session.getMapper(mapperInterface);
            operator.operateSql(mapper); // 自定义的操作方法
            if (commit) session.commit(); // 是否提交数据,选择否则会话结束会回滚数据
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * 生成mybatis逆向工程文件.
     * <p>使用该方法需要先导入 mybatis-generator-core 依赖并在 resources 文件夹下创建一个 generatorConfig.xml 文件件为逆向工程配置文件</p>
     * <p>本方法大部分为固定写法,照抄便是</p>
     *
     * @throws Exception 很多
     */
    public static void generateEntityAndMapper() throws Exception {
        List<String> warnings = new ArrayList<>();
        boolean overwrite = true;
        File configFile = new File("src/main/resources/generatorConfig.xml"); // 逆向工程配置文件路径
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        System.out.println("mybatis逆向工程文件生成成功");
    }
}
