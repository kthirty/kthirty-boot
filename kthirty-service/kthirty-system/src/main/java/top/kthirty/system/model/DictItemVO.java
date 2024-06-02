package top.kthirty.system.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kthirty.system.entity.DictItem;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictItemVO extends DictItem {
    private List<DictItemVO> children;
}
