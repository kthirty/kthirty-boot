package top.kthirty.core.db.base.entity;

import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrgEntity<T> extends DataEntity<T>{
    @Column(value = "org_code")
    private String orgCode;
    @Column(isLogicDelete = true)
    private Boolean deleted;
}
