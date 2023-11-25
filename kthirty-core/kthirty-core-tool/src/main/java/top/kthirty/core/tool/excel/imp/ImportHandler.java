package top.kthirty.core.tool.excel.imp;

import top.kthirty.core.tool.excel.support.ExcelParams;

import java.io.InputStream;
import java.util.List;
/**
 * <p>
 * 导出
 * </p>
 *
 * @author KThirty
 * @since 2023/11/25
 */
public interface ImportHandler<E> {
    /**
     * 读取为实体类
     *
     * @param inputStream 文件流
     * @param clazz       实体类型
     * @param params 参数
     * @return 实体类列表
     */
    List<E> read(InputStream inputStream, Class<E> clazz, ExcelParams params);
}
