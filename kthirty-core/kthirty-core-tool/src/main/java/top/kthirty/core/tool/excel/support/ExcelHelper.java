package top.kthirty.core.tool.excel.support;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.reader.BeanSheetReader;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.excel.Excel;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
public class ExcelHelper {
    /**
     * 获取数据开始行
     *
     * @param reader ExcelReader
     * @param start  开始计算行
     * @return 首列不为空的行数
     */
    public static int getStartRow(ExcelReader reader, int start) {
        for (int i = start; i <= start + 10; i++) {
            if (Func.isNotBlank(Func.toStr(reader.readCellValue(i, 0)))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取字段标注Title
     *
     * @param field 字段
     * @return Excel.title > Schema.title > field.name
     */
    public static String getFieldTitle(Field field) {
        if (AnnotationUtil.hasAnnotation(field, Excel.class) && Func.isNotBlank(AnnotationUtil.getAnnotation(field, Excel.class).title())) {
            return AnnotationUtil.getAnnotation(field, Excel.class).title();
        }
        if (AnnotationUtil.hasAnnotation(field, Schema.class) && Func.isNotBlank(AnnotationUtil.getAnnotation(field, Schema.class).title())) {
            return AnnotationUtil.getAnnotation(field, Schema.class).title();
        }
        return field.getName();
    }

    public static <E> List<E> deepRead(Workbook workbook, Class<E> clazz, String sheetName, ExcelParams params, Func1<Map,Boolean> filter) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            return null;
        }
        ExcelReader reader = new ExcelReader(sheet);
        // 本次需要处理的字段
        Field[] fields = getFieldsByGroup(clazz, params.getGroups());
        // 计算读取行
        int headerRowIndex = getStartRow(reader, params.getIgnoreStartRow());
        int startRowIndex = headerRowIndex + 1;
        int endRowIndex = reader.getRowCount();
        // 设置Bean读取器
        final BeanSheetReader<Map> beanSheetReader = new BeanSheetReader<>(headerRowIndex, startRowIndex, endRowIndex, Map.class);
        beanSheetReader.setIgnoreEmptyRow(true);
        Map<String, String> aliasMap = Arrays.stream(fields).collect(Collectors.toMap(ExcelHelper::getFieldTitle, Field::getName));
        beanSheetReader.setHeaderAlias(aliasMap);
        // 开始读取
        List<Map> records = reader.read(beanSheetReader);
        // 根据传入Filter去除无效数据
        records = records.stream().filter(filter::callWithRuntimeException).toList();
        // 类型转换
        List<E> result = new ArrayList<>(records.size());
        for (Map item : records) {
            E bean = BeanUtil.mapToBean(item, clazz, false, CopyOptions.create().setIgnoreError(true));
            // 处理子类
            Field[] subTableFields = getSubTableFields(clazz, params.getGroups());
            if(Func.isNotEmpty(subTableFields)){
                for (Field subTableField : subTableFields) {
                    Class<?> subClass = getFieldGenericType(subTableField);
                    String title = getFieldTitle(subTableField);
                    List<?> subResult = deepRead(workbook, subClass, title, params, subItem -> {
                        // 下级的 {sheetName序号} 属性要与当前数据的{序号}属性相同
                        String mainId = MapUtil.getStr(item,"序号");
                        String subMainId = MapUtil.getStr(subItem, sheetName + "序号");
                        return Func.equalsSafe(mainId,subMainId);
                    });
                    if(subResult != null){
                        ReflectUtil.setFieldValue(bean,subTableField,subResult);
                    }
                }
            }
            result.add(bean);
        }
        return result;
    }

    /**
     * 分组获取字段
     *
     * @param clazz  源Clas
     * @param groups 组
     * @param <E>    实体类型
     * @return 所有符合分组的字段
     */
    public static <E> Field[] getFieldsByGroup(Class<E> clazz, String[] groups) {
        return ReflectUtil.getFields(clazz, field -> AnnotationUtil.hasAnnotation(field, Excel.class)
                && ArrayUtil.containsAny(AnnotationUtil.getAnnotation(field, Excel.class).group(), groups));
    }

    /**
     * 获取Entity中包含@Excel注解的子表字段，仅获取List<SubClass> 这类属性
     *
     * @param clazz 源Class
     * @param <E>   实体类型
     * @return 所有需要处理子类的字段
     */
    public static <E> Field[] getSubTableFields(Class<E> clazz, String[] groups) {
        return ReflectUtil.getFields(clazz, field -> {
            // 字段不包含Excel注解
            if (!AnnotationUtil.hasAnnotation(field, Excel.class)) {
                return false;
            }
            // 字段非List类型
            if (!field.getType().isAssignableFrom(List.class)) {
                return false;
            }
            // 获取字段的泛型类型
            Class<?> typeArgument = getFieldGenericType(field);
            // 泛型非常规Class
            if(!ClassUtil.isNormalClass(typeArgument)){
                return false;
            }
            // 泛型为JdkClass
            if(ClassUtil.isJdkClass(typeArgument)){
                return false;
            }
            // 泛型中不包含该分组需要的字段
            Field[] fields = getFieldsByGroup(typeArgument, groups);
            if(Func.isEmpty(fields)){
                return false;
            }
            return true;
        });
    }

    /**
     * 获取字段的泛型类型
     * @param field 字段
     * @return 泛型类
     */
    public static Class<?> getFieldGenericType(Field field){
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            return ClassUtil.loadClass((typeArguments[0]).getTypeName());
        }
        return null;
    }


}
