package top.kthirty.core.web.swagger;

import cn.hutool.core.util.StrUtil;

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
