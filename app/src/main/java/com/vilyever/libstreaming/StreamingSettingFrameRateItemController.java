package com.vilyever.libstreaming;

import android.content.Context;

/**
 * StreamingSettingItemController
 * ESB <com.vilyever.operationbar.text>
 * Created by vilyever on 2016/2/26.
 * Feature:
 */
public class StreamingSettingFrameRateItemController extends StreamingSettingItemController {
    final StreamingSettingFrameRateItemController self = this;

    public static final int MinFrameRate = 15;
    public static final int MaxFrameRate = 25;

    /* Constructors */
    public StreamingSettingFrameRateItemController(Context context) {
        super(context);

        getSelectionAdapter().selectItem(0);
    }

    /* Public Methods */
    public int getFrameRate() {
        return MinFrameRate + getSelectionAdapter().getSingleSelectedPosition();
    }

    /* Properties */


    /* Overrides */

    /* Delegates */


    /* Protected Methods */
    protected int internalGainItemCount() {
        return MaxFrameRate - MinFrameRate + 1;
    }

    protected void internalBindViewHolder(StreamingSettingItemViewHolder viewHolder, int position) {
        viewHolder.getTitleLabel().setText(String.format("%d", MinFrameRate + position));
    }

    protected void internalOnItemSelected(int position) {

    }

    /* Private Methods */

}