package top.kthirty.core.tool.excel.support;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.tool.Func;
import top.kthirty.core.tool.excel.Excel;
import top.kthirty.core.tool.utils.StringPool;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
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

    /**
     * 分组获取字段
     *
     * @param clazz  源Clas
     * @param groups 组
     * @param containsColl 包含List形式的字段
     * @param <E>    实体类型
     * @return 所有符合分组的字段
     */
    public static <E> List<Field> getFieldsByGroup(Class<E> clazz, String[] groups,boolean containsColl) {
        Field[] fields = ReflectUtil.getFields(clazz, field -> AnnotationUtil.hasAnnotation(field, Excel.class)
                && ArrayUtil.containsAny(AnnotationUtil.getAnnotation(field, Excel.class).group(), groups)
                && (containsColl || !Collection.class.isAssignableFrom(field.getType())));
        List<Field> list = ListUtil.list(true, fields);
        // 根据标题排序
        list = CollUtil.sort(list, Comparator.comparingInt(f -> AnnotationUtil.getAnnotation(f, Excel.class).sort()));
        return list;
    }
    public static <E> List<Field> getFieldsByGroup(Class<E> clazz, String[] groups){
        return getFieldsByGroup(clazz,groups,false);
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
            List<Field> fields = getFieldsByGroup(typeArgument, groups);
            return !Func.isEmpty(fields);
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
    public static Map<String,String> getHeaderAlias(List<Field> fields){
        return fields.stream().collect(Collectors.toMap(ExcelHelper::getFieldTitle, Field::getName));
    }

    public static String getClassTitle(Class<?> clazz){
        Excel excel = AnnotationUtil.getAnnotation(clazz, Excel.class);
        return excel != null ? excel.title() : StringPool.EMPTY;
    }

    public static void activeSheet(ExcelWriter writer,String sheetName){
        if(writer.getSheet().getSheetName().equals(sheetName)){
            return;
        }
        writer.setSheet(sheetName);
        int lastRowNum = writer.getSheet().getLastRowNum();
        if(lastRowNum != -1){
            writer.setCurrentRow(lastRowNum + 1);
        }
    }

}
