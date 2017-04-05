package org.jiaoyajing.dizner.wplayer.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Dizner on 2017/3/12.
 * 自定义ListView适应Scrollview的。
 */

public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) {
        this(context, null);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }
    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置为Integer.MAX_VALUE>>2 是listview全部展开
        int measureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
//设置为400是设置listview的高度只能有400 不全部展开   实现可以滑动的效果
        int measureSpec1 = MeasureSpec.makeMeasureSpec(400, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, measureSpec);
    }
}
