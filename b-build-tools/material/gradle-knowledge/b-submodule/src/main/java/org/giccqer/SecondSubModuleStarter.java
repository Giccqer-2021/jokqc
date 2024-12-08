package org.giccqer;

import org.apache.ibatis.session.SqlSession;

import javax.swing.*;

/**
 * 第二个子模块的启动类.
 * <p>将第一个子模块作为自身依赖项</p>
 */
public class SecondSubModuleStarter {
    public static void main(String[] args) {
        //来自另一个子模块中的常量
        String subModuleMessage = "从依赖的模块中导入的常量: " + FirstSubModuleStarter.SUB_MODULE_STRING;
        //来自自身依赖的使用api标记的依赖中的类
        String myModuleMessage = "从依赖的模块依赖的模块中导入的类" + SqlSession.class.getName();
        //弹出对话框并输出相关信息
        JOptionPane.showMessageDialog(null, subModuleMessage + "\n" + myModuleMessage, "消息", JOptionPane.INFORMATION_MESSAGE);
    }
}
