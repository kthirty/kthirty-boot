package #(backendPackage).controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.web.base.BaseController;
import #(backendPackage).entity.#(entityName);
import #(backendPackage).service.#(entityName)Service;

import java.io.Serializable;
import java.util.List;

@RestController
@Tag(name = "#(title)接口")
@AllArgsConstructor
@RequestMapping("#(apiPrefix)")
public class #(entityName)Controller extends BaseController {
    private final #(entityName)Service #(entityVar)Service;

    @PostMapping("save")
    @Operation(summary = "保存#(title)")
    public boolean save(@RequestBody @Valid #(entityName) #(entityVar)) {
        return #(entityVar)Service.save(#(entityVar));
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "删除#(title)")
    public boolean remove(@PathVariable Serializable id) {
        return #(entityVar)Service.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "更新#(title)")
    public boolean update(@RequestBody @Valid #(entityName) #(entityVar)) {
        return #(entityVar)Service.updateById(#(entityVar));
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "获取#(title)详情")
    public #(entityName) getInfo(@PathVariable Serializable id) {
        return #(entityVar)Service.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询#(title)")
    public Page<#(entityName)> page(Query<#(entityName)> query, #(entityName) #(entityVar)) {
        return #(entityVar)Service.page(query.getPage(), Condition.getWrapper(#(entityVar)));
    }

    @GetMapping("list")
    @Operation(summary = "查询#(title)列表")
    public List<#(entityName)> list(#(entityName) #(entityVar)) {
        return #(entityVar)Service.list(Condition.getWrapper(#(entityVar)));
    }
#if(treeList)

    @GetMapping("tree")
    @Operation(summary = "查询#(title)树")
    public List<#(entityName)> tree(#(entityName) #(entityVar)) {
        return #(entityVar)Service.list(Condition.getWrapper(#(entityVar)));
    }
#end
}
