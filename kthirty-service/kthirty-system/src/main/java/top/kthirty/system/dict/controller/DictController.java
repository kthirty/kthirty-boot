package top.kthirty.system.dict.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.AllArgsConstructor;
import top.kthirty.system.dict.entity.Dict;
import top.kthirty.system.dict.service.DictService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 *  控制层。
 *
 * @author Thinkpad
 * @since 2024-03-01
 */
@RestController
@Tag(name = "接口")
@AllArgsConstructor
@RequestMapping("/dict")
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
    public Page<Dict> page(@Parameter(description="分页信息")Page<Dict> page) {
        return dictService.page(page);
    }

}
