package mybatis.util;

/**
 * 用于定义Mapper操作方法的函数式接口.
 * <p>@{@link FunctionalInterface} 注解表明它是一个函数式接口</p>
 *
 * @param <T> mapper接口类型
 */
@FunctionalInterface
public interface MapperOperator<T> {
    void operateSql(T mapper);
}
