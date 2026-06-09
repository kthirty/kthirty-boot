package top.kthirty.extra.oos;

import top.kthirty.core.boot.KthirtyApplication;
import top.kthirty.core.web.swagger.SwaggerRegister;

/**
 * 对象存储模块入口
 *
 * @author KThirty
 */
public class OosApplication implements SwaggerRegister {
    public static void main(String[] args) {
        KthirtyApplication.run(OosApplication.class, args);
    }
}
