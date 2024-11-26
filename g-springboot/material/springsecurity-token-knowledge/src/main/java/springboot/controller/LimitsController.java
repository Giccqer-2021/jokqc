package springboot.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.message.Notification;

@RestController
@RequestMapping("/data")
public class LimitsController {
    @PreAuthorize("hasAnyAuthority('admin','user:add')")
    @RequestMapping("/add")
    public Notification dataAdd() {
        return new Notification("成功执行 增加 数据命令");
    }

    @PreAuthorize("hasAnyAuthority('admin','user:delete')")
    @RequestMapping("/delete")
    public Notification deleteData() {
        return new Notification("成功执行 删除 数据命令");
    }

    @PreAuthorize("hasAnyAuthority('admin','user:edit')")
    @RequestMapping("/edit")
    public Notification editData() {
        return new Notification("成功执行 修改 数据命令");
    }

    @PreAuthorize("hasAnyAuthority('admin','user:view')")
    @RequestMapping("/view")
    public Notification viewData() {
        return new Notification("成功执行 查询 数据命令");
    }
}
