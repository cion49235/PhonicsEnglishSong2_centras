package com.phonics.englishsong.centras.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.phonics.englishsong.centras.R;
import com.phonics.englishsong.centras.activity.SubActivity;
import com.phonics.englishsong.centras.data.Ani_Sub_Data;
import com.phonics.englishsong.centras.util.FadingActionBarHelperBase;
import com.phonics.englishsong.centras.util.ImageLoader;


public class SubAdapter extends BaseAdapter{
	public Context context;
//	public ListView listview_sub;
	public ImageLoader imgLoader;
	public SharedPreferences settings,pref;
	public Editor edit;
	ArrayList<Ani_Sub_Data> list;
	public ImageButton bt_favorite;
	public String num = "empty";
	public Cursor cursor;
	public SubAdapter(Context context,ArrayList<Ani_Sub_Data> list, ListView listview_sub) {
		this.imgLoader = new ImageLoader(context.getApplicationContext());
		this.context = context;
		this.list = list;
//		this.listview_sub = listview_sub;
		
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		try{
			pref = context.getSharedPreferences(context.getString(R.string.txt_pref), Activity.MODE_PRIVATE);
			String array_subject= pref.getString("array_subject", "");
			String array_videoid = pref.getString("array_videoid", "");
			
			if(view == null){	
				LayoutInflater layoutInflater = LayoutInflater.from(context);
				view = layoutInflater.inflate(R.layout.activity_sub_listrow, parent, false); 
			}
			
			ImageView sub_img_thumb = (ImageView)view.findViewById(R.id.sub_img_thumb);
			sub_img_thumb.setFocusable(false);
			imgLoader.DisplayImage(list.get(position).thumb, R.drawable.no_image, sub_img_thumb);
			
			TextView sub_txt_subject = (TextView)view.findViewById(R.id.sub_txt_subject);
			if(sub_txt_subject != null){
				sub_txt_subject.setText(list.get(position).subject);
				if(list.get(position).subject.equals(array_subject) && list.get(position).videoid.equals(array_videoid)){
					sub_txt_subject.setTextColor(Color.RED);
				}else{
					sub_txt_subject.setTextColor(Color.BLACK);
				}
			}
			
			bt_favorite = (ImageButton)view.findViewById(R.id.bt_favorite);
			bt_favorite.setFocusable(false);
			bt_favorite.setSelected(false);
			
			try{
				cursor = SubActivity.favorite_mydb.getReadableDatabase().rawQuery(
						"select * from favorite_list where num = '"+list.get(position).id+"'", null);
				if(null != cursor && cursor.moveToFirst()){
					num = cursor.getString(cursor.getColumnIndex("num"));
				}else{
					num = "empty";
					
				}
				if(num.equals("empty")){
					bt_favorite.setImageResource(R.drawable.bt_favorite_normal);
				}else{
					bt_favorite.setImageResource(R.drawable.bt_favorite_pressed);	
				}
			}catch(Exception e){
			}finally{
				if(SubActivity.favorite_mydb != null){
					SubActivity.favorite_mydb.close();
				}
				if(cursor != null){
					cursor.close();
				}
			}
			
			bt_favorite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					cursor = SubActivity.favorite_mydb.getReadableDatabase().rawQuery(
							"select * from favorite_list where num = '"+list.get(position).id+"'", null);
					if(null != cursor && cursor.moveToFirst()){
						num = cursor.getString(cursor.getColumnIndex("num"));
					}else{
						num = "empty";
					}
					if(num.equals("empty")){
						bt_favorite.setImageResource(R.drawable.bt_favorite_pressed);
						ContentValues cv = new ContentValues();
						cv.put("id", list.get(position).videoid);
	    				cv.put("num", list.get(position).id);
	    				cv.put("subject", list.get(position).subject);
	    				cv.put("thumb", list.get(position).thumb);
	    				cv.put("portal", list.get(position).portal);
						SubActivity.favorite_mydb.getWritableDatabase().insert("favorite_list", null, cv);
						if(SubActivity.sub_adapter != null){
							SubActivity.sub_adapter.notifyDataSetChanged();
						}
						Toast.makeText(context, context.getString(R.string.txt_main_activity1), Toast.LENGTH_SHORT).show();
					}else{
						bt_favorite.setImageResource(R.drawable.bt_favorite_normal);
						SubActivity.favorite_mydb.getWritableDatabase().delete("favorite_list", "num" + "=" +num, null);
						if(SubActivity.sub_adapter != null){
							SubActivity.sub_adapter.notifyDataSetChanged();
						}
						Toast.makeText(context, context.getString(R.string.txt_main_activity2), Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			LinearLayout layout_sublistrow = (LinearLayout)view.findViewById(R.id.layout_sublistrow);
			if(FadingActionBarHelperBase.listview_sub.isItemChecked(position+1)){
//				view.setBackgroundColor(Color.parseColor("#ddffaa"));
//				layout_sublistrow.setBackground(context.getResources().getDrawable(R.drawable.listrow_color_green));
				layout_sublistrow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.listrow_color_green));
			}else{
//				view.setBackgroundColor(Color.parseColor("#00000000"));
//				layout_sublistrow.setBackground(context.getResources().getDrawable(R.drawable.listrow_color_black));
				layout_sublistrow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.listrow_color_black));
			}
		
		}catch (Exception e) {
		}
		return view;
	}
}