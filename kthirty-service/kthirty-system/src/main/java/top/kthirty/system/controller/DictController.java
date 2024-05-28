package top.kthirty.system.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.entity.Dict;
import top.kthirty.system.service.DictService;

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
@RequestMapping("/sys/dict")
public class DictController extends BaseController {
    private final DictService dictService;

    @PostMapping("save")
    @Operation(summary = "保存",description="保存")
    public boolean save(@RequestBody @Parameter(description="")@Valid Dict dict) {
        return dictService.save(dict);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键",description="根据主键")
    public boolean remove(@PathVariable @Parameter(description="主键")Serializable id) {
        return dictService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新",description="根据主键更新")
    public boolean update(@RequestBody @Parameter(description="主键")@Valid Dict dict) {
        return dictService.updateById(dict);
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取",description="根据主键获取")
    public Dict getInfo(@PathVariable Serializable id) {
        return dictService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询",description="分页查询")
    public Page<Dict> page(@Parameter(description="分页信息")Query<Dict> query,Dict dict) {
        return dictService.page(query.getPage(), Condition.getWrapper(dict));
    }

    @GetMapping("list")
    @Operation(summary = "查询所有",description="查询所有")
    public List<Dict> list(Dict dict) {
        return dictService.list(Condition.getWrapper(dict));
    }

}
