package top.kthirty.core.db.sequence.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RuleContext {
    private String table;
    private String column;
    private Object entity;
}
