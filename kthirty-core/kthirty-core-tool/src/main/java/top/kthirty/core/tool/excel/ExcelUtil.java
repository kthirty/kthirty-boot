package top.kthirty.core.tool.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.tool.excel.imp.ImportHandler;
import top.kthirty.core.tool.excel.support.ExcelGroup;
import top.kthirty.core.tool.excel.support.ExcelParams;
import top.kthirty.core.tool.excel.support.ExcelStyle;
import top.kthirty.core.tool.excel.test.TestUser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
@SuppressWarnings("unchecked")
public class ExcelUtil {

    public static <E> List<E> imp(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        params = ObjUtil.defaultIfNull(params,new ExcelParams());
        params.defaultGroups(ExcelGroup.IMPORT);
        params.defaultStyle(ExcelStyle.MULTIPLE);
        // 开始读取Excel，使用Handler处理
        ImportHandler<E> importHandler = ReflectUtil.newInstance(params.getStyle().getImportHandlerClass());
        return importHandler.read(inputStream,clazz,params);
    }

    public static void main(String[] args) {
        String path = "D:\\System\\Desktop\\test.xlsx";
        BufferedInputStream inputStream = FileUtil.getInputStream(path);
        List<TestUser> imp = ExcelUtil.imp(inputStream, TestUser.class, null);
        System.out.println(JSONUtil.toJsonPrettyStr(imp));
    }
}