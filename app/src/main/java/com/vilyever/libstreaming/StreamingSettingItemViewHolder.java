package com.vilyever.libstreaming;

import android.view.ViewGroup;
import android.widget.TextView;

import com.vilyever.temputilities.RecyclerHelper.Selection.SelectionViewHolder;

/**
 * StreamingSettingItemViewHolder
 * ESB <com.vilyever.operationbar.text>
 * Created by vilyever on 2016/2/26.
 * Feature:
 */
public class StreamingSettingItemViewHolder extends SelectionViewHolder {
    final StreamingSettingItemViewHolder self = this;

    /* Constructors */
    public StreamingSettingItemViewHolder(ViewGroup parent) {
        super(parent, R.layout.streaming_setting_item_view_holder);
    }

    /* Public Methods */

    /* Properties */
    private TextView titleLabel;
    public TextView getTitleLabel() { if (titleLabel == null) { titleLabel = (TextView) self.itemView.findViewById(R.id.titleLabel); } return titleLabel; }

    /* Interfaces */
}