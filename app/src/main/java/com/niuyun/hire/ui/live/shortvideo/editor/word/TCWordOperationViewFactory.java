package com.niuyun.hire.ui.live.shortvideo.editor.word;

import android.content.Context;
import android.view.View;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.live.shortvideo.editor.word.widget.TCWordOperationView;


/**
 * Created by hanszhli on 2017/6/21.
 * <p>
 * 创建 OperationView的工厂
 */

public class TCWordOperationViewFactory {


    public static TCWordOperationView newOperationView(Context context) {
        return (TCWordOperationView) View.inflate(context, R.layout.layout_default_operation_view, null);
    }
}
