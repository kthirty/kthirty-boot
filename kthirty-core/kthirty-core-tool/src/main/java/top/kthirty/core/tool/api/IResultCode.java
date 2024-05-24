package top.kthirty.core.tool.api;

import java.io.Serializable;

/**
 * 业务响应码接口
 * @author Kthirty
 * @since 2023/11/17
 */
public interface IResultCode extends Serializable {

    /**
     * 消息
     *
     * @return String
     */
    String getMessage();

    /**
     * 状态码
     *
     * @return int
     */
    Integer getCode();

}
