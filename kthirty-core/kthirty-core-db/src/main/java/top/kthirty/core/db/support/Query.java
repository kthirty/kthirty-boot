package top.kthirty.core.db.support;

import com.mybatisflex.core.paginate.Page;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页工具
 *
 * @author Kthirty
 */
@Data
@Accessors(chain = true)
public class Query<T> {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页的数量
     */
    private Integer size;

    public Page<T> getPage() {
        return Condition.getPage(this);
    }
}
