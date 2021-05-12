package com.hcan53.android.views.adapter;

import android.view.View;


import com.hcan53.android.views.R;
import com.hcan53.android.views.base.BaseHolder;
import com.hcan53.android.views.base.BaseRecyclerViewAdapter;
import com.hcan53.android.views.round.JmRoundTextView;

import java.util.List;

public class BottomSheetListAdapter extends BaseRecyclerViewAdapter<String> {
    private final int ITME_TYPE_NORMAL = 0;
    private final int ITME_TYPE_TOP = 1;
    private final int ITME_TYPE_BOTTOM = 2;
    private final int ITME_TYPE_SINGLE = 3;

    private boolean noTitle;
    public BottomSheetListAdapter(List<String> items, boolean noTitle) {
        mInfos = items;
        this.noTitle = noTitle;
    }

    @Override
    public BaseHolder<String> getHolder(View v, int viewType) {
        return new ItemsHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        if(viewType == ITME_TYPE_NORMAL) {
            return R.layout.jm_bottom_sheet_item;
        } else if(viewType == ITME_TYPE_TOP) {
            return R.layout.jm_bottom_sheet_item_top;
        } else if(viewType == ITME_TYPE_BOTTOM) {
            return R.layout.jm_bottom_sheet_item_bottom;
        } else if(viewType == ITME_TYPE_SINGLE) {
            return R.layout.jm_bottom_sheet_item_single;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(noTitle) { //无title
            if(mInfos.size() == 1) {
                return ITME_TYPE_SINGLE;
            } else if(position == 0) {
                return ITME_TYPE_TOP;
            } else if(position == mInfos.size() - 1) {
                return ITME_TYPE_BOTTOM;
            }
        } else {
            if(position == mInfos.size() - 1) {
                return ITME_TYPE_BOTTOM;
            }
        }
        return ITME_TYPE_NORMAL;
    }

    class ItemsHolder extends BaseHolder<String> {
        JmRoundTextView itemTv;
        ItemsHolder(View itemView) {
            super(itemView);
            itemTv = itemView.findViewById(R.id.bottom_sheet_item_tv);
        }

        @Override
        public void setData(String data, int position) {
            itemTv.setText(data);
        }
    }
}