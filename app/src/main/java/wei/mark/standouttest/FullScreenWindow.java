package wei.mark.standouttest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.ui.Window;

public class FullScreenWindow
        extends StandOutWindow {

    public static final int FULL_SCREEN_WINDOW_ID = 0;

    @Override
    public String getAppName() {
        return "FullScreenWindow";
    }

    @Override
    public int getAppIcon() {
        return android.R.drawable.ic_menu_close_clear_cancel;
    }


    @Override
    public void createAndAttachView(int id, FrameLayout frame) {
        // create a new layout from body.xml
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.full_screen, frame, true);

        unFocusViewOnClick(view);
    }

    private void unFocusViewOnClick(View view) {
        LinearLayout layout = view.findViewById(R.id.linearLayout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getWindow(FULL_SCREEN_WINDOW_ID).onFocus(false);
                    }
                }, 1500);
            }
        });
    }
    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return new StandOutLayoutParams(id, metrics.widthPixels, metrics.heightPixels,
                StandOutLayoutParams.CENTER, StandOutLayoutParams.CENTER);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return "Click to close the SimpleWindow";
    }

    @Override
    public Intent getPersistentNotificationIntent(int id) {
        return StandOutWindow.getCloseIntent(this, FullScreenWindow.class, id);
    }
}
