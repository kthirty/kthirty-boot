package top.kthirty.system.controller;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.tool.support.Kv;
import top.kthirty.core.tool.utils.BeanUtil;
import top.kthirty.core.tool.utils.TreeUtil;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.entity.Dict;
import top.kthirty.system.entity.DictItem;
import top.kthirty.system.entity.table.DictItemTableDef;
import top.kthirty.system.model.DictItemVO;
import top.kthirty.system.service.DictItemService;
import top.kthirty.system.service.DictService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 控制层。
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
    private final DictItemService dictItemService;

    @PostMapping("save")
    @Operation(summary = "保存", description = "保存")
    public boolean save(@RequestBody @Parameter(description = "") @Valid Dict dict) {
        return dictService.save(dict);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键", description = "根据主键")
    public boolean remove(@PathVariable @Parameter(description = "主键") Serializable id) {
        return dictService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新", description = "根据主键更新")
    public boolean update(@RequestBody @Parameter(description = "主键") @Valid Dict dict) {
        return dictService.updateById(dict);
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取", description = "根据主键获取")
    public Dict getInfo(@PathVariable Serializable id) {
        return dictService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询", description = "分页查询")
    public Page<Dict> page(@Parameter(description = "分页信息") Query<Dict> query, Dict dict) {
        return dictService.page(query.getPage(), Condition.getWrapper(dict));
    }

    @GetMapping("list")
    @Operation(summary = "查询所有", description = "查询所有")
    public List<Dict> list(Dict dict) {
        return dictService.list(Condition.getWrapper(dict));
    }

    @GetMapping("queryItem")
    @Operation(summary = "查询字典选项", description = "查询字典选项")
    public List<Tree<String>> items(@RequestParam String code) {
        List<DictItem> list = dictItemService.queryChain().where(DictItemTableDef.DICT_ITEM.CODE.eq(code)).list();
        return TreeUtil.forest(list);
    }

    @PostMapping("saveItem")
    @Operation(summary = "保存选项")
    public boolean saveItem(@RequestBody @Parameter(description = "字典详情") @Valid DictItem dictItem) {
        return dictItemService.saveOrUpdate(dictItem);
    }

    @GetMapping("queryAllItem")
    @Operation(summary = "查询所有字典", description = "查询所有字典")
    public Kv queryAllItem() {
        Kv result = Kv.init();
        Map<String, List<DictItem>> dictList = dictItemService.list()
                .stream()
                .peek(it -> it.setParentId(StrUtil.blankToDefault(it.getParentId(),"0")))
                .map(it -> BeanUtil.copyProperties(it, DictItemVO.class))
                .collect(Collectors.groupingBy(DictItem::getCode));
        dictList.forEach((code,list) -> {
            List<DictItem> dictItems = TreeUtil.buildBean(list);
            result.put(code, dictItems);
        });
        return result;
    }
}
