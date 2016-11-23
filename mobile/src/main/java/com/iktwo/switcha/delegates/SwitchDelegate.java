package com.iktwo.switcha.delegates;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.iktwo.switcha.R;
import com.iktwo.switcha.databinding.DelegateSwitchBinding;
import com.iktwo.switcha.model.Switch;
import com.iktwo.switcha.utils.BindingViewHolder;
import com.iktwo.switcha.utils.ViewDelegate;

/**
 * Switch delegate.
 */

public class SwitchDelegate extends ViewDelegate<BindingViewHolder<DelegateSwitchBinding>> {
    private SwitchDelegateHandler handler;

    public SwitchDelegate(SwitchDelegateHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getItemViewType() {
        return Delegates.SWITCH;
    }

    @Override
    public BindingViewHolder<DelegateSwitchBinding> onCreateViewHolder(ViewGroup viewGroup) {
        DelegateSwitchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.delegate_switch, viewGroup, false);

        binding.setHandler(handler);

        return new BindingViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<DelegateSwitchBinding> viewHolder, Object item, int position) {
        viewHolder.binding.setModel((Switch) item);
        viewHolder.binding.setIndex(position);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public boolean canHandleItemTypeAtPosition(Object item, int position) {
        return item instanceof Switch;
    }
}
