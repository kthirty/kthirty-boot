package top.kthirty.core.tool.exception;

import lombok.Getter;
import top.kthirty.core.tool.api.IResultCode;
import top.kthirty.core.tool.api.SystemResultCode;

import java.io.Serial;

/**
 * Secure异常
 *
 * @author Kthirty
 */
@Getter
public class SecureException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 2359767895161832954L;

	private final IResultCode resultCode;

	public SecureException(){
		this(SystemResultCode.UN_AUTHORIZED);
	}
	public SecureException(String message) {
		super(message);
		this.resultCode = SystemResultCode.UN_AUTHORIZED;
	}

	public SecureException(IResultCode resultCode) {
		super(resultCode.getMessage());
		this.resultCode = resultCode;
	}

	public SecureException(IResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
