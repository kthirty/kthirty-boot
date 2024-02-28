package top.kthirty.core.db.base.entity;

import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrgEntity extends DataEntity{
    @Column(value = "org_code")
    protected String orgCode;
    @Column(isLogicDelete = true)
    protected Boolean deleted;
}
