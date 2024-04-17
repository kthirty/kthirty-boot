package top.kthirty.core.tool.redis;

import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 * 将redis key序列化
 * </p>
 *
 * @author KThirty
 * @since 2023/11/19
 */
public class RedisKeySerializer implements RedisSerializer<Object> {
	private final Charset charset;
	private final ConversionService converter;
	private final String prefix;

	public RedisKeySerializer(String prefix) {
		this(StandardCharsets.UTF_8,prefix);
	}

	public RedisKeySerializer(Charset charset,String prefix) {
		Objects.requireNonNull(charset, "Charset must not be null");
		this.charset = charset;
		this.converter = DefaultConversionService.getSharedInstance();
		this.prefix = prefix;
	}

	@Override
	public Object deserialize(byte[] bytes) {
		// redis keys 会用到反序列化
		if (bytes == null) {
			return null;
		}
		return new String(bytes, charset);
	}

	@Override
	@Nullable
	public byte[] serialize(Object object) {
		Objects.requireNonNull(object, "redis key is null");
		String key;
		if (object instanceof SimpleKey) {
			key = "";
		} else if (object instanceof String) {
			key = (String) object;
		} else {
			key = converter.convert(object, String.class);
		}
		key = prefix + key;
		return key.getBytes(this.charset);
	}

}
