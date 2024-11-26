package springboot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用来测试增删改查权限的数据控制类.
 * <p>实际上并不能真正操作数据库,仅用作模拟</p>
 * <p>要在主方法或配置类中添加 @{@link EnableMethodSecurity}使权限控制生效</p>
 * <p>需要权限控制的方法需添加 @{@link PreAuthorize} 注解,其中的value值使用 SpEL 表达式</p>
 */
@RestController
@RequestMapping("/data")
public class LimitsController {

    /**
     * 模拟操作数据库中数据的 增 操作.
     * <p>hasAnyAuthority 表示拥有其中一个权限即可生效</p>
     *
     * @return 数据操作成功的提示,下面所有方法同理
     */
    @PreAuthorize("hasAnyAuthority('admin','user:add')") //增
    @RequestMapping("/add")
    public String dataAdd() {
        return "<p>成功执行 增加 数据命令</p>";
    }

    @PreAuthorize("hasAnyAuthority('admin','user:delete')") //删
    @RequestMapping("/delete")
    public String deleteData() {
        return "<p>成功执行 删除 数据命令</p>";
    }

    @PreAuthorize("hasAnyAuthority('admin','user:edit')") //改
    @RequestMapping("/edit")
    public String editData() {
        return "<p>成功执行 修改 数据命令</p>";
    }

    @PreAuthorize("hasAnyAuthority('admin','user:view')") //查
    @RequestMapping("/view")
    public String viewData() {
        return "<p>成功执行 查询 数据命令</p>";
    }
}
