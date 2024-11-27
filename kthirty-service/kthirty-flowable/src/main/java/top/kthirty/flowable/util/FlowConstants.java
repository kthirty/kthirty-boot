package top.kthirty.flowable.util;

import cn.hutool.core.bean.copier.CopyOptions;

/**
 * <p>
 * 流程相关常亮
 * </p>
 *
 * @author KThirty
 * @since 2024/11/23
 */
public interface FlowConstants {
    /**
     * 任务处理结果
     */
    String TASK_COMPLETE_VAR_NAME = "FLOWABLE_TASK_RESULT";

    CopyOptions COPY_OPTIONS = CopyOptions.create()
            .setIgnoreProperties(
                    "taskEngineConfiguration"
                    , "engineConfigurations"
                    , "identityLinkServiceConfiguration"
                    , "variableServiceConfiguration"
                    , "taskEngineConfiguration"
                    , "transientVariables"
            )
            .ignoreError()
            .ignoreNullValue();

}
