package top.kthirty.core.web.thread;

import org.springframework.context.ApplicationListener;
import org.springframework.web.context.support.ServletRequestHandledEvent;
import top.kthirty.core.tool.support.RequestVariableHolder;

public class RequestVariableCleanListener implements ApplicationListener<ServletRequestHandledEvent> {
    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        RequestVariableHolder.clear();
    }
}
