package top.kthirty.core.web.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import top.kthirty.core.tool.utils.StringPool;

import java.io.IOException;
import java.util.List;

/**
 * jackson 默认值为 null 时的处理
 *
 * 主要是为了避免 app 端出现null导致闪退
 *
 * 规则：
 * number -1
 * string ""
 * date ""
 * boolean false
 * array []
 * Object {}
 *
 * @author Kthirty
 */
public class KthirtyBeanSerializerModifier extends BeanSerializerModifier {
    private final static int INDEX_NOT_FOUND = -1;
    @Override
    public List<BeanPropertyWriter> changeProperties(
            SerializationConfig config, BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties) {
        // 循环所有的beanPropertyWriter
        beanProperties.forEach(writer -> {
             handleSerializer(writer);
			// 如果已经有 null 序列化处理如注解：@JsonSerialize(nullsUsing = xxx) 跳过
			if (writer.hasNullSerializer()) {
				return;
			}
            /*JavaType type = writer.getType();
            Class<?> clazz = type.getRawClass();
            if (type.isTypeOrSubTypeOf(Number.class)) {
                writer.assignNullSerializer(NullJsonSerializers.NUMBER_JSON_SERIALIZER);
            }else if (type.isTypeOrSubTypeOf(Boolean.class)) {
                writer.assignNullSerializer(NullJsonSerializers.BOOLEAN_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Character.class)) {
				writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
			}  else if (type.isTypeOrSubTypeOf(String.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isArrayType() || clazz.isArray() || type.isTypeOrSubTypeOf(Collection.class)) {
                writer.assignNullSerializer(NullJsonSerializers.ARRAY_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(OffsetDateTime.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else if (type.isTypeOrSubTypeOf(Date.class) || type.isTypeOrSubTypeOf(TemporalAccessor.class)) {
                writer.assignNullSerializer(NullJsonSerializers.STRING_JSON_SERIALIZER);
            } else {
                writer.assignNullSerializer(NullJsonSerializers.OBJECT_JSON_SERIALIZER);
            }*/
        });
        return super.changeProperties(config, beanDesc, beanProperties);
    }

    /**
     * 处理属性的默认Serializer
     */
    private void handleSerializer(BeanPropertyWriter writer){
        // 如果已经有序列化处理如注解：@JsonSerialize(using = xxx) 跳过
        if (writer.hasSerializer()) {
            return;
        }
        // 默认转换Long为String，处理精度问题
        if (writer.getType().isTypeOrSubTypeOf(Long.class)) {
            writer.assignSerializer(ToStringSerializer.instance);
        }
    }

    public interface NullJsonSerializers {

        JsonSerializer<Object> STRING_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(StringPool.EMPTY);
            }
        };

        JsonSerializer<Object> NUMBER_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(INDEX_NOT_FOUND);
            }
        };

        JsonSerializer<Object> BOOLEAN_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeObject(Boolean.FALSE);
            }
        };

        JsonSerializer<Object> ARRAY_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartArray();
                gen.writeEndArray();
            }
        };

        JsonSerializer<Object> OBJECT_JSON_SERIALIZER = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeStartObject();
                gen.writeEndObject();
            }
        };

    }

}
