package com.vilyever.libstreaming;

import android.content.Context;

/**
 * StreamingSettingItemController
 * ESB <com.vilyever.operationbar.text>
 * Created by vilyever on 2016/2/26.
 * Feature:
 */
public class StreamingSettingRatioItemController extends StreamingSettingItemController {
    final StreamingSettingRatioItemController self = this;

    private final int[] ratioWidths = new int[]{
            176,
            320,
            352,
            640,
            640,
            720,
            1024,
            1280
    };

    private final int[] ratioHeights = new int[]{
            144,
            240,
            288,
            360,
            480,
            480,
            576,
            720
    };

    /* Constructors */
    public StreamingSettingRatioItemController(Context context) {
        super(context);

        getSelectionAdapter().selectItem(0);
    }

    /* Public Methods */
    public int getRatioWidth() {
        return this.ratioWidths[getSelectionAdapter().getSingleSelectedPosition()];
    }

    public int getRatioHeight() {
        return this.ratioHeights[getSelectionAdapter().getSingleSelectedPosition()];
    }

    /* Properties */


    /* Overrides */

    /* Delegates */


    /* Protected Methods */
    protected int internalGainItemCount() {
        return this.ratioWidths.length;
    }

    protected void internalBindViewHolder(StreamingSettingItemViewHolder viewHolder, int position) {
        viewHolder.getTitleLabel().setText(String.format("%d X %d", this.ratioWidths[position], this.ratioHeights[position]));
    }

    protected void internalOnItemSelected(int position) {

    }

    /* Private Methods */

}