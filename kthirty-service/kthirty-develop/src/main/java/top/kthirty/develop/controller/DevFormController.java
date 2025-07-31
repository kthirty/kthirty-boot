package top.kthirty.develop.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryCondition;

import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.entity.table.DevFormItemTableDef;
import top.kthirty.develop.entity.table.DevFormTableDef;
import top.kthirty.develop.service.DevFormItemService;
import top.kthirty.develop.service.DevFormService;

import java.io.Serializable;
import java.util.List;

/**
 * form开发 控制层。
 *
 * @author KTHIRTY
 * @since 2024-07-02
 */
@RestController
@Tag(name = "form开发接口")
@AllArgsConstructor
@RequestMapping("/dev/form")
public class DevFormController extends BaseController {
    private final DevFormService devFormService;
    private final DevFormItemService devFormItemService;


    @PostMapping("save")
    @Operation(summary = "保存form开发",description="保存form开发")
    public boolean save(@RequestBody @Parameter(description="form开发")@Valid DevForm devForm) {
        return devFormService.save(devForm);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键form开发",description="根据主键form开发")
    public boolean remove(@PathVariable @Parameter(description="form开发主键")Serializable id) {
        return devFormService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新form开发",description="根据主键更新form开发")
    public boolean update(@RequestBody @Parameter(description="form开发主键")@Valid DevForm devForm) {
        return devFormService.updateById(devForm);
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取form开发",description="根据主键获取form开发")
    public DevForm getInfo(@PathVariable Serializable id) {
        DevForm devForm = devFormService.getById(id);
        devForm.setItems(devFormItemService.list(QueryCondition.create(DevFormItemTableDef.DEV_FORM_ITEM.FORM_ID, id)));
        return devForm;
    }

    @GetMapping("page")
    @Operation(summary = "分页查询form开发",description="分页查询form开发")
    public Page<DevForm> page(@Parameter(description="分页信息")Query<DevForm> query,DevForm devForm) {
        return devFormService.page(query.getPage(), Condition.getWrapper(devForm));
    }

    @GetMapping("list")
    @Operation(summary = "查询所有form开发",description="查询所有form开发")
    public List<DevForm> list(DevForm devForm) {
        return devFormService.list(Condition.getWrapper(devForm));
    }

    @GetMapping("tableNameExists")
    @Operation(summary = "名称是否已存在", description = "名称是否已存在")
    public boolean tableNameExists(String id,String tableName) {
        return devFormService.exists(QueryWrapper.create().and(DevFormTableDef.DEV_FORM.TABLE_NAME.eq(tableName).and(DevFormTableDef.DEV_FORM.ID.ne(id))));
    }
}
