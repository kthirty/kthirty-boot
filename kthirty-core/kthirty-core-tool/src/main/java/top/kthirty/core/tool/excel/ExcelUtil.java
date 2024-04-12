package top.kthirty.core.tool.excel;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.tool.excel.exp.ExportHandler;
import top.kthirty.core.tool.excel.handler.DefaultCellStyleEditor;
import top.kthirty.core.tool.excel.handler.DictCellReader;
import top.kthirty.core.tool.excel.handler.DictCellWriter;
import top.kthirty.core.tool.excel.imp.ImportHandler;
import top.kthirty.core.tool.excel.support.ExcelGroup;
import top.kthirty.core.tool.excel.support.ExcelParams;
import top.kthirty.core.tool.excel.support.ExcelStyle;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Slf4j
@SuppressWarnings("unchecked")
public class ExcelUtil {
    /**
     * @author Kthirty
     * @since 2023/11/26
     * @param inputStream 文件输入流
     * @param clazz 实体类Class
     * @param params 导入参数
     * @return java.util.List<E>
     */
    public static <E> List<E> imp(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        params = ObjUtil.defaultIfNull(params,new ExcelParams());
        params.defaultGroups(ExcelGroup.IMPORT);
        params.defaultStyle(ExcelStyle.MULTIPLE);
        params.defaultCellReader(new DictCellReader());
        // 开始读取Excel，使用Handler处理
        ImportHandler<E> importHandler = ReflectUtil.newInstance(params.getStyle().getImportHandlerClass());
        List<E> result = importHandler.read(inputStream, clazz, params);
        IoUtil.closeIfPosible(inputStream);
        return result;
    }
    /**
     * 导出Excel，支持字典解析
     * @author Kthirty
     * @since 2023/11/26
     * @param list 数据列表
     * @param clazz 实体类型
     * @param outputStream 输出流（不会自动关闭）
     * @param params 额外参数
     */
    public static <E> void exp(List<E> list,Class<E> clazz, OutputStream outputStream, ExcelParams params){
        params = ObjUtil.defaultIfNull(params,new ExcelParams());
        params.defaultGroups(ExcelGroup.IMPORT);
        params.defaultStyle(ExcelStyle.MULTIPLE);
        params.defaultCellWriter(new DictCellWriter());
        params.defaultCellStyleEditor(new DefaultCellStyleEditor());
        ExportHandler<E> exportHandler = ReflectUtil.newInstance(params.getStyle().getExportHandlerClass());
        ExcelWriter writer = exportHandler.write(list, clazz, params);
        writer.flush(outputStream);
        IoUtil.close(writer);
    }
}
