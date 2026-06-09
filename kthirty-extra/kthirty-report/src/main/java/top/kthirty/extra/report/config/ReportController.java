package top.kthirty.extra.report.config;

import com.bstek.ureport.provider.report.ReportFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.extra.report.config.provider.DbReportProvider;

import java.util.List;

/**
 * @description Report Rest
 * @author KThirty
 * @since 2025/7/31 13:00
 */
@ResponseBody
@RequestMapping("report")
@Tag(name = "报表接口")
@AllArgsConstructor
@RestController
public class ReportController extends BaseController {
    @Autowired(required = false)
    private DbReportProvider dbReportProvider;

    @GetMapping("list")
    @Operation(summary = "查询报表列表", description = "查询报表列表")
    public List<ReportFile> list(){
        return dbReportProvider.getReportFiles();
    }

}
