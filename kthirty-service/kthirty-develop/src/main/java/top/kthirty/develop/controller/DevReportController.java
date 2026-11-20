package top.kthirty.develop.controller;

import cn.hutool.core.lang.Dict;
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
import top.kthirty.develop.config.DevReportDataSourceManager;
import top.kthirty.develop.entity.DevReport;
import top.kthirty.develop.entity.DevReportItem;
import top.kthirty.develop.entity.table.DevReportItemTableDef;
import top.kthirty.develop.entity.table.DevReportTableDef;
import top.kthirty.develop.model.DevReportParseSqlReq;
import top.kthirty.develop.service.DevReportItemService;
import top.kthirty.develop.service.DevReportService;

import java.io.Serializable;
import java.util.List;

/**
 * 报表开发 控制层。
 *
 * @author KTHIRTY
 * @since 2026-11-15
 */
@RestController
@Tag(name = "报表开发接口")
@AllArgsConstructor
@RequestMapping("/dev/report")
public class DevReportController extends BaseController {

    private final DevReportService devReportService;
    private final DevReportItemService devReportItemService;
    private final DevReportDataSourceManager dataSourceManager;

    @PostMapping("save")
    @Operation(summary = "保存报表开发", description = "保存报表开发")
    public boolean save(@RequestBody @Parameter(description = "报表开发") @Valid DevReport devReport) {
        return devReportService.save(devReport);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键删除报表开发", description = "根据主键删除报表开发")
    public boolean remove(@PathVariable @Parameter(description = "报表开发主键") Serializable id) {
        return devReportService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新报表开发", description = "根据主键更新报表开发")
    public boolean update(@RequestBody @Parameter(description = "报表开发") @Valid DevReport devReport) {
        return devReportService.updateById(devReport);
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取报表开发", description = "根据主键获取报表开发")
    public DevReport getInfo(@PathVariable Serializable id) {
        DevReport report = devReportService.getById(id);
        if (report != null) {
            report.setItems(devReportItemService.list(
                    QueryCondition.create(DevReportItemTableDef.DEV_REPORT_ITEM.REPORT_ID, id)));
        }
        return report;
    }

    @GetMapping("page")
    @Operation(summary = "分页查询报表开发", description = "分页查询报表开发")
    public Page<DevReport> page(@Parameter(description = "分页信息") Query<DevReport> query, DevReport devReport) {
        return devReportService.page(query.getPage(), Condition.getWrapper(devReport));
    }

    @GetMapping("list")
    @Operation(summary = "查询所有报表开发", description = "查询所有报表开发")
    public List<DevReport> list(DevReport devReport) {
        return devReportService.list(Condition.getWrapper(devReport));
    }

    @GetMapping("codeExists")
    @Operation(summary = "编码是否已存在", description = "编码是否已存在")
    public boolean codeExists(String id, String code) {
        return devReportService.exists(QueryWrapper.create()
                .and(DevReportTableDef.DEV_REPORT.CODE.eq(code)
                        .and(DevReportTableDef.DEV_REPORT.ID.ne(id))));
    }

    @GetMapping("datasourceKeys")
    @Operation(summary = "获取可用数据源Key列表", description = "获取可用数据源Key列表")
    public List<Dict> datasourceKeys() {
        return dataSourceManager.listKeys().stream()
                .map(key -> Dict.create().set("value", key).set("label", key))
                .toList();
    }

    @PostMapping("parseSql")
    @Operation(summary = "解析SQL字段", description = "根据SQL与数据源解析字段列表")
    public List<DevReportItem> parseSql(@RequestBody @Valid DevReportParseSqlReq req) {
        return devReportService.parseSqlFields(req.getReportSql(), req.getDsKey());
    }
}
