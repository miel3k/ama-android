package com.ama.core

import android.view.View
import android.widget.ViewSwitcher

fun ViewSwitcher?.switchView(showView2: Boolean, view1: View?, view2: View?) {
    if (this == null) {
        return
    }
    if (showView2) {
        if (currentView != view2) {
            showNext()
        }
    } else {
        if (currentView != view1) {
            showPrevious()
        }
    }
}