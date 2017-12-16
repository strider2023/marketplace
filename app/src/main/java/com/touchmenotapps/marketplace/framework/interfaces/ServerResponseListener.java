package com.touchmenotapps.marketplace.framework.interfaces;

import com.touchmenotapps.marketplace.common.enums.ServerEvents;

/**
 * Created by i7 on 16-10-2017.
 */

public interface ServerResponseListener {

    void onSuccess(int threadId, Object object);

    void onFaliure(ServerEvents serverEvents, Object object);
}
