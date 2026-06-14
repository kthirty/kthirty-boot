package top.kthirty.develop.codegen;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;

import java.util.Map;

/**
 * DevForm 代码生成模板渲染器（Enjoy 引擎，通过 Hutool 调用）
 */
public final class DevFormTemplateRenderer {

    private static final TemplateEngine ENGINE;

    static {
        TemplateConfig config = new TemplateConfig(
                "/templates/enjoy/codegen",
                TemplateConfig.ResourceMode.CLASSPATH);
        ENGINE = TemplateUtil.createEngine(config);
    }

    private DevFormTemplateRenderer() {
    }

    public static String render(String templateName, Map<String, Object> data) {
        Template template = ENGINE.getTemplate(templateName);
        return template.render(data);
    }
}
