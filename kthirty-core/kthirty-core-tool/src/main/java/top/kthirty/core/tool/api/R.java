package top.kthirty.core.tool.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
/**
 * 响应工具类
 * @author Kthirty
 * @since 2023/11/17
 */
@Getter
@NoArgsConstructor
public class R<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 响应码
     */
    @Schema(title = "响应码")
    private Integer code;
    /**
    是否成功
     */
    @Schema(title = "是否成功", requiredMode = Schema.RequiredMode.AUTO)
    private boolean success;
    /**
     * 承载数据
     */
    @Schema(title = "承载数据")
    private T result;
    /**
     * 响应码描述
     */
    @Schema(title = "返回消息")
    private String message;
    /**
     * 构造函数
     * @param resultCode 响应码
     * @param result 承载数据
     * @param message 响应码描述
     */
    private R(IResultCode resultCode,T result,String message){
        this.code = resultCode.getCode();
        this.result = result;
        this.message = message==null?resultCode.getMessage():message;
        this.success =  SystemResultCode.SUCCESS.code.equals(this.code);
    }
    private R(IResultCode resultCode,T data){
        this(resultCode,data,null);
    }
    private R(Integer code,String message){
        this.code=code;
        this.message=message;
    }
    private R(IResultCode resultCode){
        this(resultCode,null,null);
    }
    private R(IResultCode resultCode,String message){ this(resultCode,null,message); }

    public static <T> R<T> success(){ return new R<>(SystemResultCode.SUCCESS); }
    public static <T> R<T> success(T data){ return new R<>(SystemResultCode.SUCCESS,data); }
    public static <T> R<T> success(T data,String message){ return new R<>(SystemResultCode.SUCCESS,data,message); }

    public static <T> R<T> fail(){ return new R<>(SystemResultCode.FAILURE); }
    public static <T> R<T> fail(String message){ return new R<>(SystemResultCode.FAILURE,message); }
    public static <T> R<T> fail(IResultCode resultCode){ return new R<>(resultCode); }
    public static <T> R<T> fail(IResultCode resultCode,String message){ return new R<>(resultCode,message); }

    /**
     * 特殊需求使用方法
     * @author Kthirty
     * @since Created in 2020/9/3 15:28
     * @param resultCode 状态码
     * @param data 装载数据
     * @param message 自定义消息
     * @return top.kthirty.core.tool.api.R<T>
     */
    public static <T> R<T> instance(SystemResultCode resultCode,T data,String message){ return new R<>(resultCode,data,message); }
    public static <T> R<T> instance(Integer code,String message){ return new R<>(code,message); }
    public R message(String message){
        this.message = message;
        return this;
    }
    public R success(boolean success){
        this.success = success;
        return this;
    }
    public R data(T result){
        this.result = result;
        return this;
    }
}
