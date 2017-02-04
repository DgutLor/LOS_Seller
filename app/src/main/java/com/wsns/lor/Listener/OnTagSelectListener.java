package com.wsns.lor.Listener;

import com.wsns.lor.view.layout.FlowTagLayout;

import java.util.List;


public interface OnTagSelectListener {
    void onItemSelect(FlowTagLayout parent, List<Integer> selectedList);
}
