package org.giccqer.test;


import org.junit.jupiter.api.Test;

/**
 * 用于检验测试方法是否能正常执行.
 */
public class PrintTest {
    @Test
    public void printHello() {
        System.out.println("方式1-打印消息:你好!");
    }

    @Test
    public void printFaces() {
        System.out.println("方式2-打印表情:😍😊😜");
    }

    @Test
    public void printAphorism() {
        System.out.println("方式3-打印格言:如果是玫瑰,它总会开花的");
    }
}
