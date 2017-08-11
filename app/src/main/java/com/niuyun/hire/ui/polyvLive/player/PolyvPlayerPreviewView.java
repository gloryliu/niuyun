package com.niuyun.hire.ui.polyvLive.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.PolyvSDKUtil;
import com.easefun.polyvsdk.vo.PolyvVideoVO;
import com.niuyun.hire.R;
import com.niuyun.hire.utils.ImageLoadedrManager;

import java.io.File;

/**
 * 预览图视图
 * @author TanQu 2016-3-3
 */
public class PolyvPlayerPreviewView extends RelativeLayout {
	private static final String TAG = PolyvPlayerPreviewView.class.getSimpleName();
	private Context mContext = null;
	private ImageView mPreviewImage = null;
	private ImageButton mStartBtn = null;
	private Callback mCallback = null;

    public PolyvPlayerPreviewView(Context context) {
        this(context, null);
    }
    
    public PolyvPlayerPreviewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public PolyvPlayerPreviewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initViews();
    }
    
    private void initViews() {
    	LayoutInflater.from(getContext()).inflate(R.layout.polyv_player_preview_view, this);
		mPreviewImage = (ImageView) findViewById(R.id.preview_image);
		mStartBtn = (ImageButton) findViewById(R.id.start_btn);
		mStartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mCallback != null) {
					mCallback.onClickStart();
				}

                hide();
			}
		});
    	
    }

    /**
     * 设置图片并显示
     * @param vid
     */
    public void show(String vid) {
		new LoadVideoJson(vid).execute();
        setVisibility(View.VISIBLE);
    }

	private class LoadVideoJson extends AsyncTask<String, Void, PolyvVideoVO> {

		private final String mVid;

		LoadVideoJson(String vid) {
			mVid = vid;
		}

		@Override
		protected PolyvVideoVO doInBackground(String... params) {
			PolyvVideoVO video = null;
			try {
				video = PolyvSDKUtil.loadVideoJSON2Video(mVid);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return video;
		}

		@Override
		protected void onPostExecute(PolyvVideoVO v) {
			super.onPostExecute(v);
			if (v == null) return;
			File dir = PolyvSDKClient.getInstance().getVideoDownloadExtraResourceDir(mVid);
			String fileName = v.getFirstImage().substring(v.getFirstImage().lastIndexOf("/"));
			File file = new File(dir, fileName);
			if (file.exists()) {
				mPreviewImage.setImageURI(Uri.parse(file.getAbsolutePath()));
			} else {
				ImageLoadedrManager.getInstance().display(mContext,v.getFirstImage(),mPreviewImage);
			}
		}
	}

    /**
     * 隐藏
     */
    public void hide() {
    	Drawable drawable = mPreviewImage.getDrawable();
    	if (drawable != null && drawable instanceof BitmapDrawable) {
    		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
    		if (bitmap != null && !bitmap.isRecycled()) {
    			bitmap.recycle();
    			bitmap = null;
    		}
    	}

		mPreviewImage.setImageBitmap(null);
    	System.gc();
        setVisibility(View.GONE);
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
		return true;
	}

    public void setCallback(Callback callback) {
    	mCallback = callback;
    }

    public interface Callback {
    	public void onClickStart();
    }
}
