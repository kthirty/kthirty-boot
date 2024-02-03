package top.kthirty.core.db.base.entity;

import com.mybatisflex.annotation.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class IdEntity<T> extends BaseEntity {
    /**
     * 主键
     */
    @Id
    protected T id;
}
