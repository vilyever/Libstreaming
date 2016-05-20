package com.vilyever.libstreaming;

import android.content.Context;

/**
 * StreamingSettingItemController
 * ESB <com.vilyever.operationbar.text>
 * Created by vilyever on 2016/2/26.
 * Feature:
 */
public class StreamingSettingBitRateItemController extends StreamingSettingItemController {
    final StreamingSettingBitRateItemController self = this;

    public static final int MinBitRate = 5;
    public static final int MaxBitRate = 10;
    public static final int BitRateFactor = 100000;

    /* Constructors */
    public StreamingSettingBitRateItemController(Context context) {
        super(context);

        getSelectionAdapter().selectItem(0);
    }

    /* Public Methods */
    public int getBitRate() {
        return (MinBitRate + getSelectionAdapter().getSingleSelectedPosition()) * BitRateFactor;
    }

    /* Properties */


    /* Overrides */

    /* Delegates */


    /* Protected Methods */
    protected int internalGainItemCount() {
        return MaxBitRate - MinBitRate + 1;
    }

    protected void internalBindViewHolder(StreamingSettingItemViewHolder viewHolder, int position) {
        viewHolder.getTitleLabel().setText(String.format("%d", (MinBitRate + position) * BitRateFactor));
    }

    protected void internalOnItemSelected(int position) {

    }

    /* Private Methods */

}