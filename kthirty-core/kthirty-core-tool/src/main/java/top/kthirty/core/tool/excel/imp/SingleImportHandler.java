package top.kthirty.core.tool.excel.imp;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import top.kthirty.core.tool.excel.support.ExcelContext;
import top.kthirty.core.tool.excel.support.ExcelHelper;
import top.kthirty.core.tool.excel.support.ExcelParams;
import top.kthirty.core.tool.utils.StringPool;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 单页合并单元格导入处理器
 * </p>
 *
 * @author KThirty
 * @since 2023/11/26
 */
@Slf4j
public class SingleImportHandler<E> implements ImportHandler<E>{
    private final ExcelContext context = new ExcelContext();
    private final List<String> title = new ArrayList<>();
    @Override
    public List<E> read(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        ExcelReader reader = new ExcelReader(inputStream, 0);
        // 计算标题行数
        int startRow = ExcelHelper.getStartRow(reader, params.getIgnoreStartRow());
        context.setHeaderStartRow(startRow);
        context.setSheet(reader.getSheet());
        // 首个纵向合并单元格的开始作为标题开始行，结束作为标题结束行
        reader.getSheet().getMergedRegions()
                .stream()
                .filter(it -> it.getFirstRow() == startRow && it.getFirstRow() != it.getLastRow())
                .findFirst()
                .ifPresentOrElse(it -> context.setHeaderEndRow(it.getLastRow()),() -> context.setHeaderEndRow(startRow));
        // 读取标题并建立标题联系
        readTitle(clazz, reader);
        // 初始化reader
        if(ObjUtil.isNotNull(params.getCellReader())){
            params.getCellReader().init(cell -> ExcelHelper.getFieldByTitle(clazz,title.get(cell.getColumnIndex())));
            reader.setCellEditor(params.getCellReader());
        }
        // 开始读取
        List<E> result = new ArrayList<>();
        Map<String,Integer> titleIndex = new HashMap<>();
        title.stream().map(it -> StrUtil.subBefore(it,StringPool.DOT,true)).distinct().forEach(it -> titleIndex.put(it,-1));

        while(context.getCurrentRow() < reader.getRowCount()){
            // 本行修改过下标的标题集合
            List<String> currentLineModifyIndexTitles = new ArrayList<>();
            Row row = context.getRow();
            for (int currentCol = 0; currentCol < row.getLastCellNum(); currentCol++) {
                context.setCurrentCol(currentCol);
                Object value = ExcelHelper.getValue(reader, context.getCurrentRow(), context.getCurrentCol());
                // 为空向后一列
                if(ObjUtil.isNull(value)){
                    context.addCol();
                    continue;
                }
                // 计算当前下标
                String fullTitle = title.get(currentCol);
                String objTitle = StrUtil.subBefore(fullTitle, StringPool.DOT, true);
                if(!currentLineModifyIndexTitles.contains(objTitle)){
                    // 修改当前ObjTitle下标
                    titleIndex.put(objTitle,titleIndex.get(objTitle) + 1);
                    currentLineModifyIndexTitles.add(objTitle);
                    // 将当前obj的下属属性的下标重置为0
                    title.stream()
                            .map(it -> StrUtil.subBefore(it,StringPool.DOT,true))
                            .distinct()
                            .filter(it -> StrUtil.startWith(it, objTitle +StringPool.DOT))
                            .forEach(it -> {
                                titleIndex.put(it,0);
                                currentLineModifyIndexTitles.add(it);
                            });
                }
                // 设置读取到的属性
                String beanPropPath = getBeanPropPath(clazz, fullTitle, titleIndex);
                if(StrUtil.isNotBlank(beanPropPath)){
                    BeanPath.create(beanPropPath).set(result,value);
                }
            }
            // 本行结束
            currentLineModifyIndexTitles.clear();
            context.addRow();
        }
        return result;
    }

    private String getBeanPropPath(Class<E> clazz, String fullTitle, Map<String, Integer> titleIndex) {
        List<Integer> indexList = new ArrayList<>();
        String fullPath = ExcelHelper.getFieldPathByTitle(clazz, StrUtil.subAfter(fullTitle,StringPool.DOT,false));
        while (StrUtil.contains(fullTitle,StringPool.DOT)){
            fullTitle = StrUtil.subBefore(fullTitle,StringPool.DOT,true);
            indexList.add(0,titleIndex.get(fullTitle));
        }
        return StrUtil.format("[{}]."+StrUtil.replace(fullPath, StringPool.DOT, "[{}]."), indexList.toArray());
    }

    private void readTitle(Class<E> clazz, ExcelReader reader) {
        Row headerEndRow = reader.getSheet().getRow(context.getHeaderEndRow());
        for (int col = 0; col < headerEndRow.getLastCellNum(); col++) {
            List<String> tempTitles = new ArrayList<>();
            for (int currentRow = context.getHeaderStartRow(); currentRow <= context.getHeaderEndRow(); currentRow++) {
                String value = StrUtil.toString(ExcelHelper.getValue(reader, currentRow, col));
                if(StrUtil.isNotBlank(value) && !CollUtil.contains(tempTitles,value)){
                    tempTitles.add(value);
                }
            }
            tempTitles.add(0,ExcelHelper.getClassTitle(clazz));
            title.add(StrUtil.join(StringPool.DOT,tempTitles));
        }
        context.setCurrentRow(context.getHeaderEndRow() + 1);
    }

    @SneakyThrows
    private String getPath(Class<?> clazz,String title){
        if(clazz == null){
            return null;
        }
        Class<?> currentClass = clazz;
        List<String> fieldNames = new ArrayList<>();
        for (String currentFieldTitle : StrUtil.splitTrim(title, StringPool.DOT)) {
            Field field = ExcelHelper.getFieldByTitle(currentClass, currentFieldTitle);
            if(field == null){
                return null;
            }
            fieldNames.add(field.getName());
            if (Collection.class.isAssignableFrom(field.getType())) {
                currentClass = ExcelHelper.getFieldGenericType(field);
            }
        }
        log.info("getPath {} {} {}",clazz.getName(),title,String.join(StringPool.DOT,fieldNames));
        return String.join(StringPool.DOT,fieldNames);
    }
}
