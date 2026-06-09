package top.kthirty.extra.oos.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.web.api.KthirtyResultIgnore;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.extra.oos.model.OosFileVO;
import top.kthirty.extra.oos.service.OosFileService;

import java.io.Serializable;
import java.util.List;

/**
 * 对象存储文件接口
 *
 * @author KThirty
 */
@RestController
@Tag(name = "对象存储接口")
@AllArgsConstructor
@RequestMapping("/oos/file")
public class OosFileController extends BaseController {

    private final OosFileService oosFileService;

    @PostMapping("upload")
    @Operation(summary = "上传单个文件", description = "上传单个文件，返回文件名与下载地址")
    public OosFileVO upload(@RequestParam("file") @Parameter(description = "上传文件") MultipartFile file) {
        return oosFileService.upload(file);
    }

    @PostMapping("upload/batch")
    @Operation(summary = "批量上传文件", description = "批量上传多个文件")
    public List<OosFileVO> uploadBatch(@RequestParam("files") @Parameter(description = "上传文件列表") MultipartFile[] files) {
        return oosFileService.uploadBatch(files);
    }

    @GetMapping("info/{id}")
    @Operation(summary = "获取文件信息", description = "根据文件 ID 获取元数据")
    public OosFileVO getInfo(@PathVariable @Parameter(description = "文件 ID") Serializable id) {
        return oosFileService.getInfo(id);
    }

    @GetMapping("download/{id}")
    @KthirtyResultIgnore
    @Operation(summary = "下载文件", description = "根据文件 ID 下载，需登录鉴权")
    public void download(@PathVariable @Parameter(description = "文件 ID") Serializable id) {
        oosFileService.download(id);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "删除文件", description = "删除文件元数据及存储对象")
    public boolean remove(@PathVariable @Parameter(description = "文件 ID") Serializable id) {
        return oosFileService.remove(id);
    }
}
