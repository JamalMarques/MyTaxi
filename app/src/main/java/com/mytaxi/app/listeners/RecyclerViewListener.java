package com.mytaxi.app.listeners;

import android.view.View;

public interface RecyclerViewListener<T> {
    void recyclerViewOnItemClickListener(View view, int position, T object);

    /*Add more callbacks if needed*/
}
