package top.kthirty.core.secure.exception;

import lombok.Getter;
import top.kthirty.core.tool.api.IResultCode;
import top.kthirty.core.tool.api.SystemResultCode;

import java.io.Serial;

/**
 * 未登录异常
 *
 * @author Kthirty
 */
@Getter
public class NotLoginException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 2359767895161832954L;

	private final IResultCode resultCode;

	public NotLoginException(){
		this(SystemResultCode.NOT_LOGIN);
	}
	public NotLoginException(String message) {
		super(message);
		this.resultCode = SystemResultCode.NOT_LOGIN;
	}

	public NotLoginException(IResultCode resultCode) {
		super(resultCode.getMessage());
		this.resultCode = resultCode;
	}

	public NotLoginException(IResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
	}
	public NotLoginException(Throwable cause) {
		super(cause);
		this.resultCode = SystemResultCode.NOT_LOGIN;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
