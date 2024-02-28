package top.kthirty.core.db.base.entity;

import com.mybatisflex.annotation.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.poi.ss.formula.functions.T;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class IdEntity extends BaseEntity {
    /**
     * 主键
     */
    @Id
    protected String id;
}
