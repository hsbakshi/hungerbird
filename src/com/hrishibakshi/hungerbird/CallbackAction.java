package com.hrishibakshi.hungerbird;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.view.View;

import com.markupartist.android.widget.ActionBar.AbstractAction;

public class CallbackAction extends AbstractAction {
	        private Activity mActivity;
	        private String mCallback;

	        public CallbackAction(Activity activity, String callBackFunc, int drawable) {
	            super(drawable);
	            mActivity = activity;
	            mCallback = callBackFunc;
	        }

	        public void performAction(View view) {
	        	try {
					mActivity.getClass().getMethod(mCallback).invoke(mActivity);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
}
