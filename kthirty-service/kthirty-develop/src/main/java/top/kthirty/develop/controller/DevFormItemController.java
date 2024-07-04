package top.kthirty.develop.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import top.kthirty.develop.entity.DevFormItem;
import top.kthirty.develop.service.DevFormItemService;
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
 * @author KTHIRTY
 * @since 2024-07-02
 */
@RestController
@Tag(name = "接口")
@AllArgsConstructor
@RequestMapping("/devFormItem")
public class DevFormItemController extends BaseController {
    private final DevFormItemService devFormItemService;

    @PostMapping("save")
    @Operation(summary = "保存",description="保存")
    public boolean save(@RequestBody @Parameter(description="")@Valid DevFormItem devFormItem) {
        return devFormItemService.save(devFormItem);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键",description="根据主键")
    public boolean remove(@PathVariable @Parameter(description="主键")Serializable id) {
        return devFormItemService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新",description="根据主键更新")
    public boolean update(@RequestBody @Parameter(description="主键")@Valid DevFormItem devFormItem) {
        return devFormItemService.updateById(devFormItem);
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取",description="根据主键获取")
    public DevFormItem getInfo(@PathVariable Serializable id) {
        return devFormItemService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询",description="分页查询")
    public Page<DevFormItem> page(@Parameter(description="分页信息")Query<DevFormItem> query,DevFormItem devFormItem) {
        return devFormItemService.page(query.getPage(), Condition.getWrapper(devFormItem));
    }

    @GetMapping("list")
    @Operation(summary = "查询所有",description="查询所有")
    public List<DevFormItem> list(DevFormItem devFormItem) {
        return devFormItemService.list(Condition.getWrapper(devFormItem));
    }

}
