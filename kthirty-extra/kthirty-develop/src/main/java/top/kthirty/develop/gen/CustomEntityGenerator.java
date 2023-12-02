package top.kthirty.develop.gen;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.entity.Column;
import com.mybatisflex.codegen.entity.Table;
import com.mybatisflex.codegen.generator.impl.EntityGenerator;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class CustomEntityGenerator extends EntityGenerator {
    @Override
    public void generate(Table table, GlobalConfig globalConfig) {
        if(!globalConfig.isEntityGenerateEnable()){
            return;
        }
        Class<?> superClass = globalConfig.getEntityConfig().getSuperClass();
        List<String> superFields = Arrays.stream(ReflectUtil.getFields(superClass)).map(Field::getName).toList();
        List<Column> columns = table.getColumns().stream().filter(column -> !CollUtil.contains(superFields, column.getName())
                        && !CollUtil.contains(superFields, StrUtil.toCamelCase(column.getName())))
                .toList();
        table.setColumns(columns);
        super.generate(table, globalConfig);
    }
}
