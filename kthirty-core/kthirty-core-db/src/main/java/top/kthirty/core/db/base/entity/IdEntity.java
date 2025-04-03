package top.kthirty.core.db.base.entity;

import com.mybatisflex.annotation.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import top.kthirty.core.db.auto.ColumnDefine;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class IdEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    @ColumnDefine(ColumnDefine.Type.SHORT_STRING)
    protected String id;
}
