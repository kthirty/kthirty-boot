package top.kthirty.develop.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.meta.Column;
import cn.hutool.db.meta.MetaUtil;
import cn.hutool.db.meta.Table;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import top.kthirty.core.tool.support.Constant;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.DevFormIndex;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.enums.FieldAttribute;
import top.kthirty.develop.enums.IndexType;
import top.kthirty.develop.enums.ListType;
import top.kthirty.develop.enums.TableType;

import javax.sql.DataSource;
import java.util.stream.Collectors;

/**
 * @author KThirty
 * @description 工具
 * @since 2025/6/27 9:58
 */
@Slf4j
public class DevHelper {
    public static DevForm importTable(String tableName) {
        DataSource dataSource = SpringUtil.getBeanSafe(DataSource.class);
        Assert.notNull(dataSource, "dataSource is null");
        Table table = MetaUtil.getTableMeta(dataSource, tableName);
        log.info(JSONUtil.toJsonPrettyStr(table));

        DevForm.DevFormBuilder builder = DevForm.builder()
                .isDbSync(Constant.YES)
                .tableName(table.getTableName())
                .tableType(TableType.SINGLE_TABLE.getValue())
                .remarks(table.getComment())
                .listType(ListType.PAGE.getValue());

        // 添加字段
        double i = 1;
        for (Column column : table.getColumns()) {
            Class<?> javaType = JdbcTypeToJavaType.toJavaType(column.getTypeEnum());
            FieldAttribute fieldAttribute = FieldAttribute.infer(column.getName(), table.getPkNames());
            builder.item(DevFormItem.builder()
                            .columnName(column.getName())
                            .columnType(column.getTypeName())
                            .columnLength((int)column.getSize())
                            .columnPointLength(column.getDigit())
                            .columnDefaultVal(column.getColumnDef())
                            .columnNullable(column.isNullable())
                            .columnRemarks(column.getComment())
                            .fieldType(javaType.getName())
                            .fieldAttribute(fieldAttribute.getValue())
                            .formRequired(column.isNullable())
                            .weight(i++)
                            .isShowQuery(false)
                            .isShowForm(fieldAttribute.isNormal())
                            .isShowList(fieldAttribute.isNormal())
                            .isAllowSort(false)
                            .isReadonly(false)
                    .build());
        }
        // 设置索引
        table.getIndexInfoList().forEach(index -> {
            String columnNames = index.getColumnIndexInfoList().stream()
                    .map(it -> StrUtil.format("{} {}",it.getColumnName(),it.getAscOrDesc()))
                    .collect(Collectors.joining(","));

            builder.index(DevFormIndex.builder()
                            .indexName(index.getIndexName())
                            .indexType(index.isNonUnique() ? IndexType.NORMAL.name() : IndexType.UNIQUE.name())
                            .columnNames(columnNames)
                    .build());
        });



        return builder.build();
    }
}
