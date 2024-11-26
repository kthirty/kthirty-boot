package top.kthirty.core.db.support;

import cn.hutool.core.util.ObjUtil;
import com.mybatisflex.core.paginate.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

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
    private Integer pageNumber;

    /**
     * 每页的数量
     */
    private Integer pageSize;

    public Page<T> getPage() {
        return Condition.getPage(this);
    }

    public Page<T> getPage(long totalRows, List<T> records) {
        Page<T> page = getPage();
        page.setTotalRow(totalRows);
        page.setRecords(records);
        return page;
    }
    public void defaultPage(){
        this.pageNumber = ObjUtil.defaultIfNull(this.getPageNumber(), Condition.DEFAULT_CURRENT);
        this.pageSize = ObjUtil.defaultIfNull(this.getPageSize(), Condition.DEFAULT_SIZE);
    }

    public int getFirstResult(){
        defaultPage();
        return (pageNumber - 1) * pageSize;
    }

}
