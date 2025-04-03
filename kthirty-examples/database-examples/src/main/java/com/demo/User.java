package com.demo;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.kthirty.core.db.base.entity.BaseEntity;

/**
 * @author Thinkpad
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table("tb_account")
public class User extends BaseEntity {

}
