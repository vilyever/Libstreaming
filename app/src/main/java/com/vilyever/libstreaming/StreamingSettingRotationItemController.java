package com.vilyever.libstreaming;

import android.content.Context;

/**
 * StreamingSettingItemController
 * ESB <com.vilyever.operationbar.text>
 * Created by vilyever on 2016/2/26.
 * Feature:
 */
public class StreamingSettingRotationItemController extends StreamingSettingItemController {
    final StreamingSettingRotationItemController self = this;

    private int[] rotations = new int[]{
            0,
            90,
            180,
            270
    };

    /* Constructors */
    public StreamingSettingRotationItemController(Context context) {
        super(context);

        getSelectionAdapter().selectItem(0);
    }

    /* Public Methods */
    public int getRotation() {
        return this.rotations[getSelectionAdapter().getSingleSelectedPosition()];
    }

    /* Properties */


    /* Overrides */

    /* Delegates */


    /* Protected Methods */
    protected int internalGainItemCount() {
        return this.rotations.length;
    }

    protected void internalBindViewHolder(StreamingSettingItemViewHolder viewHolder, int position) {
        viewHolder.getTitleLabel().setText(String.format("%d", this.rotations[position]));
    }

    protected void internalOnItemSelected(int position) {

    }

    /* Private Methods */

}