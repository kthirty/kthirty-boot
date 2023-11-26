package top.kthirty.core.tool.excel.handler;

import java.util.List;

public interface WriteAppender {
    List<Object> append(List<Object> row, boolean isHeader);
}
