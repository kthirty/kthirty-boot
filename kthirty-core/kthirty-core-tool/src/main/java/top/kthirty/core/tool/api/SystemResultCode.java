package top.kthirty.core.tool.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务代码枚举
 * 返回码规划
 * @author Kthirty
 */
@Getter
@AllArgsConstructor
public enum SystemResultCode implements IResultCode {

    SUCCESS("AA0000", "操作成功"),
    FAILURE("AA0001", "业务异常"),
    UN_AUTHORIZED("AA0002", "请求未授权"),
    INTERNAL_SERVER_ERROR("AA0003", "服务器异常"),
    PARAM_ERROR("AA0004", "参数错误"),
    NOT_FOUND("AA0005", "404 没找到请求"),
    MSG_NOT_READABLE("AA0006", "消息不能读取"),
    METHOD_NOT_SUPPORTED("AA0007", "不支持当前请求方法"),
    MEDIA_TYPE_NOT_SUPPORTED("AA0008", "不支持当前媒体类型"),
    SERVICE_NOT_FOUND("AA0009", "服务不可用"),
    NOT_LOGIN("AA0010", "用户未登录")
    ;

    /**
     * code编码
     */
    final String code;
    /**
     * 中文信息描述
     */
    final String message;

}