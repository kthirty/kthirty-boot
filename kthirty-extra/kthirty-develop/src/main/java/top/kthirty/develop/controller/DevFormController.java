package top.kthirty.develop.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import top.kthirty.develop.entity.DevForm;
import top.kthirty.develop.service.DevFormService;
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
 * form开发 控制层。
 *
 * @author KTHIRTY
 * @since 2024-07-02
 */
@RestController
@Tag(name = "form开发接口")
@AllArgsConstructor
@RequestMapping("/devForm")
public class DevFormController extends BaseController {
    private final DevFormService devFormService;

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
        return devFormService.getById(id);
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

}
