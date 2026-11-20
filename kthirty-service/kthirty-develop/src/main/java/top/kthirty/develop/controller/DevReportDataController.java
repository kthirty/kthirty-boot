package top.kthirty.develop.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Query;
import top.kthirty.core.web.api.KthirtyResultIgnore;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.develop.model.DevReportSchemaVO;
import top.kthirty.develop.service.DevReportDataService;

import java.util.List;
import java.util.Map;

/**
 * 报表动态数据接口
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@RestController
@Tag(name = "报表动态数据接口")
@AllArgsConstructor
@RequestMapping("/dev/report/data")
public class DevReportDataController extends BaseController {

    private final DevReportDataService devReportDataService;

    @GetMapping("schema/{reportId}")
    @Operation(summary = "获取报表Schema")
    public DevReportSchemaVO schema(@PathVariable String reportId) {
        return devReportDataService.getSchema(reportId);
    }

    @GetMapping("schemaByCode/{code}")
    @Operation(summary = "按编码获取报表Schema")
    public DevReportSchemaVO schemaByCode(@PathVariable String code) {
        return devReportDataService.getSchemaByCode(code);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询报表数据")
    public Page<Map<String, Object>> page(@RequestParam String reportId,
                                          @Parameter(description = "分页信息") Query<?> query,
                                          @RequestParam Map<String, String> params) {
        return devReportDataService.page(reportId, query, params);
    }

    @GetMapping("list")
    @Operation(summary = "查询报表数据列表")
    public List<Map<String, Object>> list(@RequestParam String reportId,
                                          @RequestParam Map<String, String> params) {
        return devReportDataService.list(reportId, params);
    }

    @GetMapping("pageByCode")
    @Operation(summary = "按编码分页查询报表数据")
    public Page<Map<String, Object>> pageByCode(@RequestParam String code,
                                                @Parameter(description = "分页信息") Query<?> query,
                                                @RequestParam Map<String, String> params) {
        return devReportDataService.pageByCode(code, query, params);
    }

    @GetMapping("listByCode")
    @Operation(summary = "按编码查询报表数据列表")
    public List<Map<String, Object>> listByCode(@RequestParam String code,
                                                @RequestParam Map<String, String> params) {
        return devReportDataService.listByCode(code, params);
    }

    @GetMapping("export")
    @KthirtyResultIgnore
    @Operation(summary = "导出报表数据Excel")
    public void export(@RequestParam String reportId, @RequestParam Map<String, String> params) {
        devReportDataService.export(reportId, params);
    }

    @GetMapping("exportByCode")
    @KthirtyResultIgnore
    @Operation(summary = "按编码导出报表数据Excel")
    public void exportByCode(@RequestParam String code, @RequestParam Map<String, String> params) {
        devReportDataService.exportByCode(code, params);
    }
}
