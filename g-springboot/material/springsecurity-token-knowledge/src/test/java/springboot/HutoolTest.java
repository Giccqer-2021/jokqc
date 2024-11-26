package springboot;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import springboot.entity.UserEntity;
import springboot.entity.UserExamples;

import java.util.Map;

/**
 * 用来测试 hutool工具包 对 json和 jwt 的操作.
 */
public class HutoolTest {
    private static final String SECRET = "3Gdr0/h8Dd03=e3dAU";

    /**
     * 使用hutool工具包将某对象转换成json字符串, 再将json字符串转换成对象.
     */
    @Test
    public void JsonTest() {
        String jsonStr = JSONUtil.toJsonStr(UserExamples.USER2.getUserEntity());//将对象转化为json字符串
        System.out.println("生成的json字符串: " + jsonStr);
        UserEntity jsonEntity = JSONUtil.toBean(jsonStr, UserEntity.class);//将json字符串转换为对象
        System.out.println("json对象转化成的对象: " + jsonEntity);
    }

    /**
     * 使用hutool工具包封装json字符串生成一个token, 并验证token是否合法.
     * <p>使用 {@code JSONUtil.createObj()} 方法生成一个自定义的json字符串将其封装为载荷,然后进行加密并签名</p>
     * <p>验证签名是否合法后对载荷进行解码处理</p>
     */
    @Test
    public void JWTStringTest() {
        //创建自定义的json字符串
        String jsonStr = JSONUtil.createObj().set("name", "小破孩").set("age", "八岁啦").set("gender", "男").toString();
        System.out.println("创建的json字符串为: " + jsonStr);
        String token = JWT.create().setPayload("user", jsonStr).setKey(SECRET.getBytes()).sign(); //加密签名
        System.out.println("生成的token: " + token);
        boolean verify = JWTUtil.verify(token, SECRET.getBytes()); //验签
        System.out.println("验证该token是否合法: " + verify);
        String getJson = (String) JWTUtil.parseToken(token).getPayload("user"); //解码
        System.out.println("从token中获取的json字符串为: " + getJson);
    }

    /**
     * 使用hutool工具包封装一个对象生成一个token, 并验证token是否合法.
     * <p>这里使用已实例化的对象作为演示,使用 {@code JWTUtil.createToken()} 方法直接将对象封装为token</p>
     * <p>{@code Map.of()} 方法为创建一个不可改变的集合,使用 "user"来标记该对象</p>
     */
    @Test
    public void JWTObjectTest() {
        UserEntity userEntity = UserExamples.USER2.getUserEntity(); //实例化对象
        String token = JWTUtil.createToken(Map.of("user", userEntity), SECRET.getBytes()); //加密签名
        System.out.println("生成的对象token: " + token);
        boolean verify = JWTUtil.verify(token, SECRET.getBytes()); //验签
        System.out.println("验证该对象token是否合法: " + verify);
        String userJSON = JWTUtil.parseToken(token).getPayload("user").toString(); //解码
        System.out.println("从对象token中获取的json字符串为: " + userJSON);
        UserEntity getUser = JSONUtil.toBean(userJSON, UserEntity.class); //将json字符串转换成对象
        System.out.println("从json字符串中获取的对象为: " + getUser);
    }

    /**
     * 使用 RSA 非对称加密算法对json字符串进行加密解密操作.
     * <p>似乎无需手动添加更多依赖也可以运行代码</p>
     * <p>非对称加密算法的保密性更好,但运算量大于对称加密,一般不用于jwt签名</p>
     */
    @Test
    public void JWTAsymmetricEncryptionTest() {
        //使用此法创建rsa对象实例时会自动生成一对公钥和私钥,根据其构造器,当已知公钥时可以推导出私钥,已知私钥时也可推导出公钥
        //请根据实际情况调用相应的构造器
        RSA rsa = new RSA();
        System.out.println("生成的公钥: " + rsa.getPublicKeyBase64());
        System.out.println("生成的私钥: " + rsa.getPrivateKeyBase64());
        String jsonStr = JSONUtil.toJsonStr(UserExamples.USER2.getUserEntity()); //对象转化成json字符串
        //StrUtil.bytes()方法:编码字符串输出 byte[] ,使用utf8编码,然后再使用公钥加密生成新编码
        byte[] encodeBytes = rsa.encrypt(StrUtil.bytes(jsonStr, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        //Base64:一种基于64个可打印字符来表示二进制数据的表示方法,可以将任何一组 byte[] 编码用易读取的方式表示出来
        String encodeStr = Base64.encode(encodeBytes);
        System.out.println("公钥加密后的字符串: " + encodeStr);
        //私钥解码
        byte[] decodeBytes = rsa.decrypt(Base64.decode(encodeStr), KeyType.PrivateKey);
        //Base64解码
        String decodeStr = StrUtil.str(decodeBytes, CharsetUtil.CHARSET_UTF_8);
        System.out.println("私钥解密后的字符串: " + decodeStr);
        //同理,可以试试私钥加密,公钥解密的操作
        byte[] encodeBytes2 = rsa.encrypt(StrUtil.bytes(jsonStr, CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        String encodeStr2 = Base64.encode(encodeBytes2);
        System.out.println("私钥加密后的字符串: " + encodeStr2);
        byte[] decodeBytes2 = rsa.decrypt(Base64.decode(encodeStr2), KeyType.PublicKey);
        String decodeStr2 = StrUtil.str(decodeBytes2, CharsetUtil.CHARSET_UTF_8);
        System.out.println("公钥解密后的字符串: " + decodeStr2);
    }
}
