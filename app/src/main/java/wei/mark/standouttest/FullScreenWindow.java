package wei.mark.standouttest;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import wei.mark.standouttest.ui.settings.SettingsActivity;

import static wei.mark.standouttest.utils.WindowKeys.MAIN_WINDOW_ID;

public class FullScreenWindow
        extends StandOutWindow {

    private static String DEFAULT_PIN = "1996";
    /**
     * If true the window service is started
     */
    public static MutableLiveData<Boolean> isShown = new MutableLiveData<>();
    /**
     * If true the window is invisible
     */
    public static MutableLiveData<Boolean> isHidden = new MutableLiveData<>();

    private final ClearFocusTimer clearFocusTimer = new ClearFocusTimer();

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

        PinLockView mPinLockView = view.findViewById(R.id.pin_lock_view);
        mPinLockView.attachIndicatorDots(view.findViewById(R.id.indicator_dots));
        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (TextUtils.equals(pin, DEFAULT_PIN))
                    close(getApplicationContext(), FullScreenWindow.class, MAIN_WINDOW_ID);
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });

        unFocusViewOnClick(view);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void toggleImmersiveMode() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void unFocusViewOnClick(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!clearFocusTimer.isTimerScheduled) {
                    clearFocusTimer.start();
                }
                return false;
            }
        });
        clearFocusTimer.start();
    }

    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        return new StandOutLayoutParams(id, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                StandOutLayoutParams.LEFT, StandOutLayoutParams.TOP);
    }

    @Override
    public int getFlags(int id) {
        return StandOutFlags.FLAG_WINDOW_HIDE_ENABLE;
    }

    @Override
    public NotificationCompat.Action getCloseAction(int id) {
        Intent intent = StandOutWindow.getCloseIntent(this, FullScreenWindow.class, id);
        NotificationCompat.Action.Builder builder
                = new NotificationCompat.Action.Builder(R.drawable.ic_layers_inactive_24dp,
                "Close",
                PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        return builder.build();
    }

    @Override
    public NotificationCompat.Action getToggleAction(int id) {
        Intent intent = StandOutWindow.getToggleVisIntent(this, FullScreenWindow.class, id);
        NotificationCompat.Action.Builder builder
                = new NotificationCompat.Action.Builder(R.drawable.ic_layers_active_24dp,
                "Toggle",
                PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        return builder.build();
    }

    @Override
    public NotificationCompat.Action getSettingsAction() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        NotificationCompat.Action.Builder builder
                = new NotificationCompat.Action.Builder(R.drawable.ic_settings_black_24dp,
                "Settings",
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        return builder.build();
    }

    @Override
    public String getPersistentNotificationTitle(int id) {
        return "Title";
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return "Click to close the SimpleWindow";
    }

    @Override
    public boolean onShow(int id, Window window) {
        isShown.setValue(true);
        isHidden.setValue(false);
        return super.onShow(id, window);
    }

    @Override
    public boolean onHide(int id, Window window) {
        isHidden.setValue(true);
        return super.onHide(id, window);
    }

    @Override
    public boolean onClose(int id, Window window) {
        isShown.setValue(false);
        isHidden.setValue(true);
        return super.onClose(id, window);
    }

    public class ClearFocusTimer extends CountDownTimer {

        private static final int DELAY_MILLIS = 2000;
        private static final int INTERVAL = 1000;

        private boolean isTimerScheduled = false;

        public ClearFocusTimer() {
            super(DELAY_MILLIS, INTERVAL);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            getWindow(MAIN_WINDOW_ID).onFocus(false);
            isTimerScheduled = false;
        }
    }
}
