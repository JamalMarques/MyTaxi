package com.mytaxi.app.listeners;

import android.view.View;

/**
 * Created by yamil.marques on 9/9/2018.
 */
public interface RecyclerViewListener<T> {
    void recyclerViewOnItemClickListener(View view, int position, T object);

    /*Add more callbacks if needed*/
}
