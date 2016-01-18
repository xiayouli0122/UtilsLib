package com.yuri.utilslib;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ViewUtil {

	/**
	 * ViewHolder util
	 * @author Yuri
	 *
	 */
	@SuppressWarnings("unchecked")
	public static class ViewHolder{
	    public static <T extends View> T get(View view,int id){
			SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
	        if(viewHolder == null){
	            viewHolder = new SparseArray<View>();
	            view.setTag(viewHolder);
	        }
	        View childView = viewHolder.get(id);
	        if(childView == null){
	            childView = view.findViewById(id);
	            viewHolder.put(id,childView);   
	        }
	        return (T) childView;
	    }
	}
	
	/**
	 * show the input method mannual
	 * @param v the view that need show input method,like edittext
	 * @param hasFocus
	 */
	public static void onFocusChange(final View v, boolean hasFocus) {
		final boolean isFocus = hasFocus;
		(new Handler()).postDelayed(new Runnable() {
			public void run() {
				InputMethodManager imm = (InputMethodManager) v.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (isFocus) {
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				} else {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		}, 500);
	}

}
