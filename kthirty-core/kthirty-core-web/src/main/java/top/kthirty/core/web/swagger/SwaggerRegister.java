package top.kthirty.core.web.swagger;

import cn.hutool.core.util.StrUtil;
/**
 * <p>
 * Swagger文档注册器，使用MainApplication实现此接口，即可分模块展示此模块所有的接口
 * </p>
 *
 * @author KThirty
 * @since 2024/7/9
 */
public interface SwaggerRegister {
    default String group(){
        return StrUtil.toUnderlineCase(StrUtil.removeSuffixIgnoreCase(this.getClass().getSimpleName(), "application")).toLowerCase();
    }
    default String packagesToScan(){
        return this.getClass().getPackageName();
    }
    default String pathsToMatch(){
        return "/**";
    }
}
