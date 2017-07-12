package com.niuyun.hire.utils.photoTool;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;


import com.niuyun.hire.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PhotoPicActivity extends Activity {
	// ArrayList<Entity> dataList;//用来装载数据源的列表
	List<ImageBucket> dataList;
	GridView gridView;
	ImageBucketAdapter adapter;// 自定义的适配器
	AlbumHelper helper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	int maxSize=9;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_bucket);

		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());
		initData();
		initView();
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		maxSize=getIntent().getIntExtra("max", 9);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			//申请WRITE_EXTERNAL_STORAGE权限
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
					1);
			finish();
		}else{
			dataList = helper.getImagesBucketList(false);
			bimap = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_addpic_unfocused);
		}

	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
			dataList = helper.getImagesBucketList(false);
			bimap = BitmapFactory.decodeResource(getResources(),
					R.drawable.icon_addpic_unfocused);
		}else{
			Toast.makeText(this, "获取本地读写权限失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 初始化view视图
	 */
	private void initView() {
		gridView = (GridView) findViewById(R.id.gridview);
		adapter = new ImageBucketAdapter(PhotoPicActivity.this, dataList);
		gridView.setAdapter(adapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/**
				 * 通知适配器，绑定的数据发生了改变，应当刷新视图
				 */
				// adapter.notifyDataSetChanged();
				Intent intent = new Intent(PhotoPicActivity.this,
						ImageGridActivity.class);
				intent.putExtra(PhotoPicActivity.EXTRA_IMAGE_LIST,
						(Serializable) dataList.get(position).imageList);
				intent.putExtra("max",maxSize);
				startActivityForResult(intent, 10);

			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 10) {

			// Bundle b = data.getExtras();
			//
			// String str = b.getString("photo");

			ArrayList<String> list = data.getExtras().getStringArrayList(
					"photo");
			Intent i = new Intent();

			Bundle b = new Bundle();
			b.putStringArrayList("photo", list);

			i.putExtras(b);

			setResult(10,i);
			finish();

		}
	}

	public void cancleOnclick(View v) {

		this.finish();

	}
}
