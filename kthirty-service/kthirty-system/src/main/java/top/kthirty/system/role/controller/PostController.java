package top.kthirty.system.role.controller;

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
import top.kthirty.system.role.entity.Post;
import top.kthirty.system.role.service.PostService;
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
 * @author KTHIRTY
 * @since 2023-12-02
 */
@RestController
@Tag(name = "岗位信息接口")
@AllArgsConstructor
@RequestMapping("/post")
public class PostController extends BaseController {
    private final PostService postService;

    /**
     * 添加岗位信息。
     *
     * @param post 岗位信息
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    @Operation(summary = "保存岗位信息",description="保存岗位信息")
    public boolean save(@RequestBody @Parameter(description="岗位信息")@Valid Post post) {
        return postService.save(post);
    }

    /**
     * 根据主键删除岗位信息。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Operation(summary = "根据主键岗位信息",description="根据主键岗位信息")
    public boolean remove(@PathVariable @Parameter(description="岗位信息主键")Serializable id) {
        return postService.removeById(id);
    }

    /**
     * 根据主键更新岗位信息。
     *
     * @param post 岗位信息
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(summary = "根据主键更新岗位信息",description="根据主键更新岗位信息")
    public boolean update(@RequestBody @Parameter(description="岗位信息主键")@Valid Post post) {
        return postService.updateById(post);
    }

    /**
     * 查询所有岗位信息。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @Operation(summary = "查询所有岗位信息",description="查询所有岗位信息")
    public List<Post> list() {
        return postService.list();
    }

    /**
     * 根据岗位信息主键获取详细信息。
     *
     * @param id 岗位信息主键
     * @return 岗位信息详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(summary = "根据主键获取岗位信息",description="根据主键获取岗位信息")
    public Post getInfo(@PathVariable Serializable id) {
        return postService.getById(id);
    }

    /**
     * 分页查询岗位信息。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(summary = "分页查询岗位信息",description="分页查询岗位信息")
    public Page<Post> page(@Parameter(description="分页信息")Page<Post> page) {
        return postService.page(page);
    }

}
