package top.kthirty.core.db.auto;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DbTypeUtil;
import com.mybatisflex.spring.boot.MybatisFlexProperties;
import com.tangzc.mybatisflex.autotable.CustomAutoTableMetadataAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.dromara.autotable.annotation.ColumnType;
import org.springframework.core.annotation.AnnotatedElementUtils;
import top.kthirty.core.tool.utils.SpringUtil;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author KThirty
 * @description 自定义元数据适配器
 * @since 2025/4/2 9:55
 */
public class KThirtyCustomAutoTableMetadataAdapter extends CustomAutoTableMetadataAdapter {
    private final MybatisFlexProperties mybatisFlexProperties;

    public KThirtyCustomAutoTableMetadataAdapter(MybatisFlexProperties mybatisFlexProperties) {
        super(mybatisFlexProperties);
        this.mybatisFlexProperties = mybatisFlexProperties;
    }

    @Override
    public String getColumnComment(Field field, Class<?> clazz) {
        String columnComment = super.getColumnComment(field, clazz);
        if(StrUtil.isBlank(columnComment)){
            Schema schema = AnnotationUtil.getAnnotation(field, Schema.class);
            if(schema != null && StrUtil.isNotBlank(schema.description())){
                columnComment = schema.description();
            }
        }

        return columnComment;
    }

    @Override
    public ColumnType getColumnType(Field field, Class<?> clazz) {
        ColumnDefine column = AnnotationUtil.getAnnotation(field, ColumnDefine.class);
        if (column != null) {
            Table table = AnnotatedElementUtils.findMergedAnnotation(field, Table.class);
            DbType dbType = getDbType(table);
            if(dbType != null){
                ColumnSupport.ColumnInfo columnInfo = ColumnSupport.getColumnType(dbType, column.value());
                return toColumnType(columnInfo);
            }
        }

        return super.getColumnType(field, clazz);
    }

    private ColumnType toColumnType(ColumnSupport.ColumnInfo columnInfo) {
        return new ColumnType(){
            @Override
            public Class<? extends Annotation> annotationType() {
                return ColumnType.class;
            }
            @Override
            public String value() {
                return columnInfo.getType();
            }
            @Override
            public int length() {
                return columnInfo.getLength();
            }
            @Override
            public int decimalLength() {
                return columnInfo.getDecimalLength();
            }
            @Override
            public String[] values() {
                return new String[0];
            }
        };
    }

    private DbType getDbType(Table table) {
        String url;
        if (CollUtil.isNotEmpty(this.mybatisFlexProperties.getDatasource())) {
            String dbKey = table != null && StrUtil.isNotBlank(table.dataSource()) ? table.dataSource() : this.mybatisFlexProperties.getDefaultDatasourceKey();
            url = String.valueOf(this.mybatisFlexProperties.getDatasource().get(dbKey).get("url"));
        }else{
            url = DbTypeUtil.getJdbcUrl(SpringUtil.getBean(DataSource.class));
        }
        return DbTypeUtil.parseDbType(url);
    }

}
