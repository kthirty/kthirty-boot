package top.kthirty.system.dict.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import top.kthirty.system.dict.entity.DictItem;
import top.kthirty.system.dict.service.DictItemService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.db.support.Query;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 *  控制层。
 *
 * @author KThirty
 * @since 2024-04-17
 */
@RestController
@Tag(name = "接口")
@AllArgsConstructor
@RequestMapping("/sys/dictItem")
public class DictItemController extends BaseController {
    private final DictItemService dictItemService;

    @PostMapping("save")
    @Operation(summary = "保存",description="保存")
    public boolean save(@RequestBody @Parameter(description="")@Valid DictItem dictItem) {
        return dictItemService.save(dictItem);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键",description="根据主键")
    public boolean remove(@PathVariable @Parameter(description="主键")Serializable id) {
        return dictItemService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新",description="根据主键更新")
    public boolean update(@RequestBody @Parameter(description="主键")@Valid DictItem dictItem) {
        return dictItemService.updateById(dictItem);
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取",description="根据主键获取")
    public DictItem getInfo(@PathVariable Serializable id) {
        return dictItemService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询",description="分页查询")
    public Page<DictItem> page(@Parameter(description="分页信息")Query<DictItem> query,DictItem dictItem) {
        return dictItemService.page(query.getPage(), Condition.getWrapper(dictItem));
    }

    @GetMapping("list")
    @Operation(summary = "查询所有",description="查询所有")
    public List<DictItem> list(DictItem dictItem) {
        return dictItemService.list(Condition.getWrapper(dictItem));
    }

}
