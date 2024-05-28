package top.kthirty.system.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.AllArgsConstructor;
import top.kthirty.system.entity.Post;
import top.kthirty.system.service.PostService;
import org.springframework.web.bind.annotation.RestController;
import top.kthirty.core.web.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 岗位信息 控制层。
 *
 * @author KThirty
 * @since 2024-01-05
 */
@RestController
@Tag(name = "岗位信息接口")
@AllArgsConstructor
@RequestMapping("/sys/post")
public class PostController extends BaseController {
    private final PostService postService;

    @PostMapping("save")
    @Operation(summary = "保存岗位信息",description="保存岗位信息")
    public boolean save(@RequestBody @Parameter(description="岗位信息")@Valid Post post) {
        return postService.save(post);
    }

    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键岗位信息",description="根据主键岗位信息")
    public boolean remove(@PathVariable @Parameter(description="岗位信息主键")Serializable id) {
        return postService.removeById(id);
    }

    @PutMapping("update")
    @Operation(summary = "根据主键更新岗位信息",description="根据主键更新岗位信息")
    public boolean update(@RequestBody @Parameter(description="岗位信息主键")@Valid Post post) {
        return postService.updateById(post);
    }

    @GetMapping("list")
    @Operation(summary = "查询所有岗位信息",description="查询所有岗位信息")
    public List<Post> list() {
        return postService.list();
    }

    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取岗位信息",description="根据主键获取岗位信息")
    public Post getInfo(@PathVariable Serializable id) {
        return postService.getById(id);
    }

    @GetMapping("page")
    @Operation(summary = "分页查询岗位信息",description="分页查询岗位信息")
    public Page<Post> page(@Parameter(description="分页信息")Page<Post> page) {
        return postService.page(page);
    }

}
