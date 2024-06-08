package top.kthirty.system.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.kthirty.core.db.base.mapper.BaseMapper;
import top.kthirty.system.entity.Dept;

import java.util.List;

/**
 * 部门信息 映射层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
public interface DeptMapper extends BaseMapper<Dept> {
    @Select("select * from sys_dept where status = #{status}")
    List<Dept> selectDeptByStatus(@Param("status") String status);
}
