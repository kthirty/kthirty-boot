package top.kthirty.develop.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.web.api.KthirtyResultIgnore;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.develop.model.DevFormImportResultVO;
import top.kthirty.develop.model.DevFormSchemaVO;
import top.kthirty.develop.service.DevFormDataService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 动态表单数据接口
 */
@RestController
@Tag(name = "form动态数据接口")
@AllArgsConstructor
@RequestMapping("/dev/form/data")
public class DevFormDataController extends BaseController {

    private final DevFormDataService devFormDataService;

    @GetMapping("schema/{formId}")
    @Operation(summary = "获取动态表单Schema")
    public DevFormSchemaVO schema(@PathVariable String formId) {
        return devFormDataService.getSchema(formId);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询动态数据")
    public Page<Map<String, Object>> page(@RequestParam String formId,
                                          @Parameter(description = "分页信息") Query<?> query,
                                          @RequestParam Map<String, String> params) {
        return devFormDataService.page(formId, query, params);
    }

    @GetMapping("list")
    @Operation(summary = "查询动态数据列表")
    public List<Map<String, Object>> list(@RequestParam String formId,
                                          @RequestParam Map<String, String> params) {
        return devFormDataService.list(formId, params);
    }

    @GetMapping("tree")
    @Operation(summary = "查询动态数据树")
    public List<Map<String, Object>> tree(@RequestParam String formId,
                                          @RequestParam Map<String, String> params) {
        return devFormDataService.tree(formId, params);
    }

    @GetMapping("getInfo/{formId}/{id}")
    @Operation(summary = "获取动态数据详情")
    public Map<String, Object> getInfo(@PathVariable String formId, @PathVariable Serializable id) {
        return devFormDataService.getInfo(formId, id);
    }

    @PostMapping("save")
    @Operation(summary = "新增动态数据")
    public boolean save(@RequestParam String formId, @RequestBody Map<String, Object> data) {
        return devFormDataService.save(formId, data);
    }

    @PutMapping("update")
    @Operation(summary = "更新动态数据")
    public boolean update(@RequestParam String formId, @RequestBody Map<String, Object> data) {
        return devFormDataService.update(formId, data);
    }

    @DeleteMapping("remove/{formId}/{id}")
    @Operation(summary = "删除动态数据")
    public boolean remove(@PathVariable String formId, @PathVariable Serializable id) {
        return devFormDataService.remove(formId, id);
    }

    @PostMapping("removeBatch")
    @Operation(summary = "批量删除动态数据")
    public boolean removeBatch(@RequestParam String formId, @RequestBody List<? extends Serializable> ids) {
        return devFormDataService.removeBatch(formId, ids);
    }

    @GetMapping("export")
    @KthirtyResultIgnore
    @Operation(summary = "导出动态数据")
    public void export(@RequestParam String formId, @RequestParam Map<String, String> params) {
        devFormDataService.export(formId, params);
    }

    @GetMapping("importTemplate")
    @KthirtyResultIgnore
    @Operation(summary = "下载导入模板")
    public void importTemplate(@RequestParam String formId) {
        devFormDataService.importTemplate(formId);
    }

    @PostMapping("import")
    @Operation(summary = "导入动态数据")
    public DevFormImportResultVO importData(@RequestParam String formId,
                                            @RequestParam("file") MultipartFile file) {
        return devFormDataService.importData(formId, file);
    }
}
