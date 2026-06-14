package top.kthirty.develop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.kthirty.core.web.utils.WebUtil;
import top.kthirty.develop.codegen.DevFormCodegenContext;
import top.kthirty.develop.codegen.DevFormTemplateRenderer;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.model.DevFormCodegenOption;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 代码生成服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DevFormCodegenService {

    private static final Map<String, String> TEMPLATE_FILES = new LinkedHashMap<>();

    static {
        TEMPLATE_FILES.put("entity.java.tpl", "backend/{javaBase}/entity/{entityName}.java");
        TEMPLATE_FILES.put("mapper.java.tpl", "backend/{javaBase}/mapper/{entityName}Mapper.java");
        TEMPLATE_FILES.put("service.java.tpl", "backend/{javaBase}/service/{entityName}Service.java");
        TEMPLATE_FILES.put("serviceImpl.java.tpl", "backend/{javaBase}/service/impl/{entityName}ServiceImpl.java");
        TEMPLATE_FILES.put("controller.java.tpl", "backend/{javaBase}/controller/{entityName}Controller.java");
        TEMPLATE_FILES.put("api.ts.tpl", "frontend/views/{frontendPath}/api.ts");
        TEMPLATE_FILES.put("data.ts.tpl", "frontend/views/{frontendPath}/data.ts");
        TEMPLATE_FILES.put("list.vue.tpl", "frontend/views/{frontendPath}/list.vue");
        TEMPLATE_FILES.put("form.vue.tpl", "frontend/views/{frontendPath}/modules/form.vue");
    }

    public void generateAndDownload(DevForm form, DevFormCodegenOption option) {
        DevFormCodegenContext context = DevFormCodegenContext.from(form, option);
        Map<String, Object> data = context.toMap();

        File dir = FileUtil.mkdir(FileUtil.createTempFile("dev-codegen-", "", true));
        FileUtil.del(dir);
        FileUtil.mkdir(dir);

        String javaBase = context.getBackendPackage().replace('.', '/');
        for (Map.Entry<String, String> entry : TEMPLATE_FILES.entrySet()) {
            String content = DevFormTemplateRenderer.render(entry.getKey(), data);
            String outputPath = resolvePath(entry.getValue(), context, javaBase);
            FileUtil.writeString(content, FileUtil.file(dir, outputPath), StandardCharsets.UTF_8);
        }

        File zip = ZipUtil.zip(dir, StandardCharsets.UTF_8);
        WebUtil.renderFile(zip, context.getEntityName() + "-codegen.zip");
    }

    private String resolvePath(String pattern, DevFormCodegenContext context, String javaBase) {
        return pattern
                .replace("{javaBase}", javaBase)
                .replace("{entityName}", context.getEntityName())
                .replace("{frontendPath}", context.getFrontendPath());
    }
}
