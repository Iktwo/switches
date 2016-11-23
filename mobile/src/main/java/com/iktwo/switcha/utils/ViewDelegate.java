package com.iktwo.switcha.utils;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Generic delegate for a view holder.
 */

public abstract class ViewDelegate<V extends RecyclerView.ViewHolder> {
    public abstract int getItemViewType();

    public abstract V onCreateViewHolder(ViewGroup viewGroup);

    public abstract void onBindViewHolder(V viewHolder, Object item, int position);

    public abstract boolean canHandleItemTypeAtPosition(Object item, int position);
}
