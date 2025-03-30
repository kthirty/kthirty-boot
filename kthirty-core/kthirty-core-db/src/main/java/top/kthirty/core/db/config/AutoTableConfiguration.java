package top.kthirty.core.db.config;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.spring.boot.MybatisFlexAutoConfiguration;
import com.tangzc.mybatisflex.autotable.CustomJavaTypeToDatabaseTypeConverter;
import org.apache.ibatis.type.UnknownTypeHandler;
import org.dromara.autotable.core.converter.JavaTypeToDatabaseTypeConverter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Field;

/**
 * <p>
 * 自动建表配置
 * </p>
 *
 * @author KThirty
 * @since 2025/3/30
 */
@Configuration
@AutoConfigureAfter(MybatisFlexAutoConfiguration.class)
public class AutoTableConfiguration {
    @Bean
    public JavaTypeToDatabaseTypeConverter javaTypeToDatabaseTypeConverter() {
        return new JavaTypeToDatabaseTypeConverter(){
            @Override
            public Class<?> getFieldType(Class<?> clazz, Field field) {
                if (field.getType().isEnum()) {
                    return String.class;
                }
                Column column = AnnotatedElementUtils.findMergedAnnotation(field, Column.class);
                if (column != null && column.typeHandler() != UnknownTypeHandler.class) {
                    return String.class;
                }
                return field.getType();
            }
        };
    }
}
