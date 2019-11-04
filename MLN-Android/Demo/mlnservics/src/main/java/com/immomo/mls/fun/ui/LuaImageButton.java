package com.immomo.mls.fun.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.immomo.mls.MLSAdapterContainer;
import com.immomo.mls.fun.ud.view.UDImageButton;
import com.immomo.mls.fun.ud.view.UDImageView;
import com.immomo.mls.provider.DrawableLoadCallback;
import com.immomo.mls.provider.ImageProvider;
import com.immomo.mls.utils.MainThreadExecutor;

import org.luaj.vm2.LuaValue;

/**
 * Created by XiongFangyu on 2018/8/3.
 */
public class LuaImageButton<U extends UDImageButton> extends LuaImageView<U> implements ILuaImageButton {
    private static final int[] PRESSED_STATE = new int[]{android.R.attr.state_pressed};
    private static final int[] NORMAL_STATE = new int[]{-android.R.attr.state_pressed};
    private String normalImage;
    private String pressImage;
    private Drawable normalDrawable;
    private Drawable pressDrawable;

    public LuaImageButton(Context context, UDImageView metaTable, LuaValue[] initParams) {
        super(context, metaTable, initParams);
    }

    @Override
    public void setImage(final String normal, final String press) {
        final ImageProvider provider = MLSAdapterContainer.getImageProvider();
        if (provider == null)
            return;
        normalDrawable = null;
        pressDrawable = null;
        normalImage = normal;
        pressImage = press;
        MainThreadExecutor.post(new Runnable() {
            @Override
            public void run() {
                setImageWithoutCheck(provider, normal, true);
                setImageWithoutCheck(provider, press, false);
            }
        });
    }

    private void setImageWithoutCheck(ImageProvider provider, String url, boolean normal) {
        boolean isNetworkUrl = URLUtil.isNetworkUrl(url);
        if (!isNetworkUrl) {
            if (TextUtils.isEmpty(url)) {
                updateDrawable(normal, null);
                return;
            }
            Drawable d = provider.loadProjectImage(getContext(), url);
            updateDrawable(normal, d);
            return;
        }
        provider.preload(getContext(), url, getRadius(), new Callback(normal));
    }

    private final class Callback implements DrawableLoadCallback {
        final boolean normal;

        Callback(boolean normal) {
            this.normal = normal;
        }

        @Override
        public void onLoadResult(Drawable drawable) {
            updateDrawable(isNormal(), drawable);
        }

        private boolean isNormal() {
            return normal;
        }
    }

    private void updateDrawable(boolean normal, Drawable d) {
        if (normal) {
            normalDrawable = d;
        } else {
            pressDrawable = d;
        }

        if (normalDrawable != null && pressDrawable != null) {
            StateListDrawable sd = new StateListDrawable();
            sd.addState(NORMAL_STATE, normalDrawable);
            sd.addState(PRESSED_STATE, pressDrawable);
            setImageDrawable(sd);

        } else if (normalDrawable != null) {
            StateListDrawable sd = new StateListDrawable();
            sd.addState(NORMAL_STATE, normalDrawable);
            setImageDrawable(sd);
        }

    }
}
