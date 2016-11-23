package com.iktwo.switcha.delegates;

import com.iktwo.switcha.model.Switch;

/**
 * Switch delegate handler.
 */

public interface SwitchDelegateHandler {
    void onSwitchSelected(Switch switchObject, boolean on, int index);
}
