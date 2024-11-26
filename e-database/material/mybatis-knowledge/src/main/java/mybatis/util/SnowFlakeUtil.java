package mybatis.util;

/**
 * 本类为自定义雪花算法的工具类.
 * <p>使用静态方法 {@code generateId()} 生成id</p>
 * <p>本类中的静态常量字段可以使用 spring 注入的方式从配置文件中加载</p>
 */
public class SnowFlakeUtil {

    /**
     * 起始时间戳,生成id时需要计算当前时间戳和该起始时间戳之间的差值,时间戳占位41bit.
     * <p>该常量一旦被确定,在任何环境下都尽可能不要去改动,除非你将数据库表中的相关所有id数据完全清空</p>
     */
    private static final long START_TIME_STAMP = 1726803514266L;
    /**
     * 上次生成id的时间戳,每运行一次生成id的方法,该值都会被重置为当前时间戳.
     */
    private static long lastTimeStamp = System.currentTimeMillis();
    /**
     * 数据中心的id,在分布式服务中表示你在哪里(某公司,某团队,某工作室等)部署该服务器,占位5bit.
     * <p>可以根据实际情况调整其占位,也可以将该字段的占位与下方工作机器id占位合并</p>
     */
    private static final long DATA_CENTER_ID = 1L;
    /**
     * 具体的服务器中某台工作机器的id,占位5bit.
     */
    private static final long WORKER_ID = 1L;
    /**
     * 序列号,若某几个id在同一毫秒时刻生成,则用此来区分这些id,占位12bit.
     */
    private static long sequence = 0L;
    /**
     * 序列号掩码,也是序列号的最大值,用于计算序列号的值是否过大,12bit.
     */
    private static final long SEQUENCE_MASK = ~(-1L << 12); // 4095

    /**
     * 使用雪花算法生成独一无二的id.
     *
     * @return 生成的id
     */
    public synchronized static long generateId() {
        long currentTimeStamp = System.currentTimeMillis(); //当前时间戳
        // 如果当前时间戳比上次生成的时间戳id大,说明系统时钟回拨了,抛出异常(否则可能造成id重复)
        if (currentTimeStamp < lastTimeStamp) {
            throw new RuntimeException("时钟回拨异常");
        } //如果当前时间戳与上次生成的时间戳相同,说明是同一毫秒内生成的id,需要计算序列号
        if (currentTimeStamp == lastTimeStamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK; //使用掩码限制序列号值的位数
            if (sequence == 0L) { //如果序列号值过大(超过12位),等待一毫秒再重新生成id
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return generateId(); //递归调用本方法
            }
        } else {
            sequence = 0L; //当前时间戳发生改变后,重置序列号
        }
        lastTimeStamp = currentTimeStamp; //更新上次生成id的时间戳
        // 计算二者时间戳的相对值,其结果左移22位,数据中心id左移17位,机器id左移12位,最后与序列号合并
        return ((currentTimeStamp - START_TIME_STAMP) << 22) | (DATA_CENTER_ID << 17) | (WORKER_ID << 12) | sequence;
    }
}
