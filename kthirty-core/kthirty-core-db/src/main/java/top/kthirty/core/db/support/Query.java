package top.kthirty.core.db.support;

import com.mybatisflex.core.paginate.Page;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.poi.ss.formula.functions.T;

/**
 * 分页工具
 *
 * @author Kthirty
 */
@Data
@Accessors(chain = true)
public class Query {

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页的数量
     */
    private Integer size;

    public Page<T> getPage() {
        return Page.of(this.current, this.size);
    }
}
