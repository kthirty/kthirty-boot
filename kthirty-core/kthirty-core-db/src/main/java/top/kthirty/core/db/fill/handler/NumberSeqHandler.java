package top.kthirty.core.db.fill.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DbTypeUtil;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.util.StringUtil;
import org.springframework.util.Assert;
import top.kthirty.core.db.fill.FillHandler;
import top.kthirty.core.tool.utils.SpringUtil;
import top.kthirty.core.tool.utils.StringPool;

import javax.sql.DataSource;
import java.lang.reflect.Field;
/**
 * <p>
 * 数字序列填充器
 * args
 * 1. 长度，lpad
 * </p>
 *
 * @author KThirty
 * @since 2025/3/29
 */
public class NumberSeqHandler implements FillHandler<String> {
    private static final int DEFAULT_LENGTH = 4;

    @Override
    public String getVal(Object object, Class<?> clazz, Field field, String... args) {
        Table table = AnnotationUtil.getAnnotation(clazz, Table.class);
        Assert.notNull(table,"Table注解不能为空");
        String tableName = table.value();
        Column column = AnnotationUtil.getAnnotation(field, Column.class);
        String columnName = column != null ? column.value() : StringUtil.camelToUnderline(field.getName());
        int length = args.length > 0 ? Convert.toInt(args[0]) : DEFAULT_LENGTH;
        // 查询数据库
        DbType dbType = DbTypeUtil.getDbType(SpringUtil.getBean(DataSource.class));
        int maxCode = switch (dbType){
            case MYSQL -> Db.selectOneBySql(StrUtil.format("select max(convert({},DECIMAL(10))) MAX_CODE from {}", columnName, tableName)).getInt("MAX_CODE", 0);
            case ORACLE -> Db.selectOneBySql(StrUtil.format("select max(to_number({})) MAX_CODE from {}", columnName, tableName)).getInt("MAX_CODE", 0);
            default -> throw new RuntimeException("不支持的数据库类型");
        };
        String code = String.valueOf(maxCode + 1);
        return StrUtil.padPre(code, Math.max(length,code.length()), StringPool.ZERO);
    }
}
