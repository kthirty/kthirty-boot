package top.kthirty.system.controller;

import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.kthirty.core.db.support.Condition;
import top.kthirty.core.web.base.BaseController;
import top.kthirty.system.entity.Post;
import top.kthirty.system.service.PostService;

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

    @GetMapping("tree")
    @Operation(summary = "查询所有岗位信息",description="查询所有岗位信息")
    public List<Post> tree(Post post) {
        return postService.tree(Condition.getWrapper(post));
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
