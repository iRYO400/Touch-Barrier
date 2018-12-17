package wei.mark.standouttest;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.ui.Window;

import static wei.mark.standouttest.utils.WindowKeys.MAIN_WINDOW_ID;

public class FullScreenWindow
        extends StandOutWindow {

    public static MutableLiveData<Boolean> isShown = new MutableLiveData<>();

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
                        getWindow(MAIN_WINDOW_ID).onFocus(false);
                    }
                }, 1500);
            }
        });
    }

    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        return new StandOutLayoutParams(id, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                StandOutLayoutParams.LEFT, StandOutLayoutParams.TOP);
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return "Click to close the SimpleWindow";
    }

    @Override
    public Intent getPersistentNotificationIntent(int id) {
        return StandOutWindow.getCloseIntent(this, FullScreenWindow.class, id);
    }

    @Override
    public boolean onShow(int id, Window window) {
        isShown.setValue(true);
        return super.onShow(id, window);
    }

    @Override
    public boolean onClose(int id, Window window) {
        isShown.setValue(false);
        return super.onClose(id, window);
    }

    @Override
    public boolean onCloseAll() {
        isShown.setValue(false);
        return super.onCloseAll();
    }
}
