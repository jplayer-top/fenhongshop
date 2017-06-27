package com.fanglin.fenhong.microbuyer.base.event;

import com.fanglin.fenhong.microbuyer.base.model.UpdateVersion;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/5/6-下午11:15.
 * 功能描述:更新功能的EventBus事件
 */
public class UpdateActionEvent {
    private UpdateVersion updateVersion;

    public UpdateActionEvent(UpdateVersion updateVersion) {
        this.updateVersion = updateVersion;
    }

    public UpdateVersion getUpdateVersion() {
        return updateVersion;
    }
}
