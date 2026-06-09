package top.kthirty.extra.oos.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对象存储类型
 *
 * @author KThirty
 */
@Getter
@AllArgsConstructor
public enum OosStorageType {
    LOCAL("local"),
    MINIO("minio"),
    SEAWEEDFS("seaweedfs");

    private final String value;

    public static OosStorageType of(String value) {
        for (OosStorageType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported storage type: " + value);
    }
}
