package top.kthirty.core.tool.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.ietf.jgss.GSSException.UNAUTHORIZED;

/**
 * 业务代码枚举
 * 返回码规划
 * @author Kthirty
 */
@Getter
@AllArgsConstructor
public enum SystemResultCode implements IResultCode {

    SUCCESS(0, "操作成功"),
    FAILURE(-1, "业务异常"),
    UN_AUTHORIZED(403, "请求未授权"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "404 没找到请求"),
    MSG_NOT_READABLE(405, "消息不能读取"),
    METHOD_NOT_SUPPORTED(405, "不支持当前请求方法"),
    MEDIA_TYPE_NOT_SUPPORTED(415, "不支持当前媒体类型"),
    NOT_LOGIN(401, "用户未登录");

    /**
     * code编码
     */
    final Integer code;
    /**
     * 中文信息描述
     */
    final String message;

}
