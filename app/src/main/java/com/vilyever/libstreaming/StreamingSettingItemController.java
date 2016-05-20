package com.vilyever.libstreaming;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.vilyever.popupcontroller.popup.PopupController;
import com.vilyever.temputilities.RecyclerHelper.Basic.RecyclerViewAdapter;
import com.vilyever.temputilities.RecyclerHelper.Selection.SelectionAdapter;

/**
 * StreamingSettingItemController
 * ESB <com.vilyever.operationbar.text>
 * Created by vilyever on 2016/2/26.
 * Feature:
 */
public abstract class StreamingSettingItemController extends PopupController {
    final StreamingSettingItemController self = this;

    /* Constructors */
    public StreamingSettingItemController(Context context) {
        super(context, R.layout.streaming_setting_item_controller);

        getRecyclerView().setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }

    /* Public Methods */


    /* Properties */
    private RecyclerView recyclerView;
    protected RecyclerView getRecyclerView() { if (this.recyclerView == null) { this.recyclerView = findViewById(R.id.recyclerView); } return this.recyclerView; }

    private SelectionAdapter selectionAdapter;
    protected SelectionAdapter getSelectionAdapter() {
        if (selectionAdapter == null) {
            this.selectionAdapter = new SelectionAdapter();
            this.selectionAdapter.setSelectionMode(SelectionAdapter.SelectionMode.Single);
            this.selectionAdapter.setItemDatasource(new RecyclerViewAdapter.ItemDatasource.SimpleItemDatasource() {
                @Override
                public int gainItemCount(RecyclerViewAdapter adapter) {
                    return self.internalGainItemCount();
                }

                @Override
                public RecyclerView.ViewHolder gainViewHolder(RecyclerViewAdapter adapter, ViewGroup parent, int viewType) {
                    return new StreamingSettingItemViewHolder(parent);
                }

                @Override
                public void onViewHolderWillBind(RecyclerViewAdapter adapter, RecyclerView.ViewHolder holder, int position, int viewType) {
                    StreamingSettingItemViewHolder viewHolder = (StreamingSettingItemViewHolder) holder;
                    self.internalBindViewHolder(viewHolder, position);
                }
            });
            this.selectionAdapter.setSelectionDelegate(new SelectionAdapter.SelectionDelegate.SimpleSelectionDelegate() {
                @Override
                public void onItemSelected(SelectionAdapter adapter, int position, boolean fromUser) {
                    if (fromUser) {
                        self.internalOnItemSelected(position);
                    }
                }
            });
        }
        return selectionAdapter;
    }


    /* Overrides */
    @Override
    protected void onViewFirstAppeared() {
        super.onViewFirstAppeared();

        getRecyclerView().setAdapter(getSelectionAdapter());
    }

    /* Delegates */

    /* Protected Methods */
    protected abstract int internalGainItemCount();
    protected abstract void internalBindViewHolder(StreamingSettingItemViewHolder viewHolder, int position);
    protected abstract void internalOnItemSelected(int position);

    /* Private Methods */

}