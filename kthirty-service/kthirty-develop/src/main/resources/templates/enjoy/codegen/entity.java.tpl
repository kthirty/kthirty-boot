package #(backendPackage).entity;

import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.tool.dict.Dict;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("#(tableName)")
@Schema(description = "#(title)")
public class #(entityName) extends LogicEntity {

#for(field : entityFields)
#if(field.hasDict)
    @Dict(code = "#(field.dictCode)")
#end
    @Schema(description = "#(field.label)")
    private #(field.javaType) #(field.fieldName);

#end
}
