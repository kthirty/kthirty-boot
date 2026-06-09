package top.kthirty.extra.oos.service;

import org.springframework.web.multipart.MultipartFile;
import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.extra.oos.entity.OosFile;
import top.kthirty.extra.oos.model.OosFileVO;

import java.io.Serializable;
import java.util.List;

/**
 * 对象存储文件服务
 *
 * @author KThirty
 */
public interface OosFileService extends BaseService<OosFile> {

    OosFileVO upload(MultipartFile file);

    List<OosFileVO> uploadBatch(MultipartFile[] files);

    OosFileVO getInfo(Serializable id);

    void download(Serializable id);

    boolean remove(Serializable id);
}
