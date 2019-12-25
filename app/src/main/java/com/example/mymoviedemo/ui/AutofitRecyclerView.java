package com.example.mymoviedemo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AutofitRecyclerView extends RecyclerView {
    private GridLayoutManager layoutManager;
    private int columnWidth = -1;

    public AutofitRecyclerView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public AutofitRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public AutofitRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs){
        if(attrs!=null){
            int[] attrsArray = {android.R.attr.columnWidth};
            TypedArray array = context.obtainStyledAttributes(attrs,attrsArray);
            columnWidth = array.getDimensionPixelOffset(0,-1);
            array.recycle();
        }

        layoutManager = new GridLayoutManager(context,2);
        setLayoutManager(layoutManager);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if(columnWidth > 0){
            int spanCount = Math.max(2,getMeasuredWidth() / columnWidth);
            layoutManager.setSpanCount(spanCount);
        }
    }
}
