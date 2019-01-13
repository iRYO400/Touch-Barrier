package wei.mark.standouttest;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.constraint.Group;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import wei.mark.standouttest.ui.common.PatterLockViewImpl;
import wei.mark.standouttest.ui.common.PinLockViewImpl;
import wei.mark.standouttest.ui.lock_screen.ScreenLockType;
import wei.mark.standouttest.ui.settings.SettingsActivity;

import static wei.mark.standouttest.utils.HawkKeys.CLOSE_ON_UNLOCK;
import static wei.mark.standouttest.utils.HawkKeys.LOCK_TYPE_INDEX;
import static wei.mark.standouttest.utils.HawkKeys.PATTERN_DOTS;
import static wei.mark.standouttest.utils.HawkKeys.PIN_CODE;
import static wei.mark.standouttest.utils.WindowKeys.MAIN_WINDOW_ID;

public class FullScreenWindow
        extends StandOutWindow {

    /**
     * If true the window service is started
     */
    public static MutableLiveData<Boolean> isShown = new MutableLiveData<>();
    /**
     * If true the window is invisible
     */
    public static MutableLiveData<Boolean> isHidden = new MutableLiveData<>();

    private final ClearFocusTimer clearFocusTimer = new ClearFocusTimer();

    private View rootView;
    private Group container;
    private ScreenLockType lockType;

    @Override
    public String getAppName() {
        return "FullScreenWindow";
    }

    @Override
    public int getAppIcon() {
        return R.drawable.ic_layers_active_24dp;
    }

    @Override
    public void createAndAttachView(int id, FrameLayout frame) {
        // create a new layout from body.xml
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.full_screen, frame);

        setView(view);
        if (isLocked()) {
            initLockScreen(view);
        }

        unFocusViewOnTouch(view);
    }

    private void setView(View view) {
        rootView = view.findViewById(R.id.background);
        container = rootView.findViewById(R.id.container);
    }

    private boolean isLocked() {
        if (Hawk.contains(LOCK_TYPE_INDEX)) {
            lockType = Hawk.get(LOCK_TYPE_INDEX);
            return true;
        }
        return false;
    }

    private void initLockScreen(View view) {
        if (lockType == ScreenLockType.PIN) {
            container.setReferencedIds(new int[]{R.id.pin_lock_view, R.id.indicator_dots});
            setPinLock(view);
        } else if (lockType == ScreenLockType.PATTERN) {
            container.setReferencedIds(new int[]{R.id.pattern_lock_view});
            setPatternLock(view);
        }
    }

    private void setPinLock(View view) {
        PinLockView pinLockView = view.findViewById(R.id.pin_lock_view);
        pinLockView.attachIndicatorDots(view.findViewById(R.id.indicator_dots));
        PinLockListener pinListener = new PinLockViewImpl() {
            @Override
            public void onComplete(String pin) {
                String hawkPin = Hawk.get(PIN_CODE);
                if (TextUtils.equals(pin, hawkPin))
                    lockSucceeded();
            }
        };
        pinLockView.setPinLockListener(pinListener);
        view.findViewById(R.id.indicator_dots).setVisibility(View.VISIBLE);
    }

    private void setPatternLock(View view) {
        PatternLockView patternLockView = view.findViewById(R.id.pattern_lock_view);
        PatternLockViewListener patternListener = new PatterLockViewImpl() {
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                boolean hasDifference = false;
                ArrayList<PatternLockView.Dot> patternList = Hawk.get(PATTERN_DOTS);
                if (patternList.size() != pattern.size())
                    hasDifference = true;

                for (int i = 0; i < patternList.size(); i++) {
                    if (hasDifference)
                        break;

                    if (patternList.get(i).getId() != pattern.get(i).getId()) {
                        hasDifference = true;
                        break;
                    }
                }
                if (hasDifference) {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    return;
                }
                patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                lockSucceeded();
            }
        };
        patternLockView.addPatternLockListener(patternListener);
    }

    private void lockSucceeded() {
        if (Hawk.contains(CLOSE_ON_UNLOCK))
            if (Hawk.get(CLOSE_ON_UNLOCK))
                close(getApplicationContext(), FullScreenWindow.class, MAIN_WINDOW_ID);
            else
                hide(getApplicationContext(), FullScreenWindow.class, MAIN_WINDOW_ID);
    }

    private void unFocusViewOnTouch(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clearFocusTimer.cancel();
                        fadeViewAnimator(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        clearFocusTimer.cancel();
                        return true;
                    case MotionEvent.ACTION_UP:
                        clearFocusTimer.start();
                        return true;
                }
                return false;
            }
        });
        clearFocusTimer.start();
    }

    private void fadeViewAnimator(boolean reverse) {
        ObjectAnimator colorAnimator = null;
        if (reverse &&
                ((ColorDrawable) rootView.getBackground()).getColor()
                        == ContextCompat.getColor(FullScreenWindow.this, R.color.grey_trans)) {
            colorAnimator = ObjectAnimator.ofInt(rootView, "backgroundColor",
                    ContextCompat.getColor(FullScreenWindow.this, R.color.grey_trans),
                    ContextCompat.getColor(FullScreenWindow.this, R.color.transparent));
            container.setVisibility(View.GONE);
        } else if (!reverse &&
                ((ColorDrawable) rootView.getBackground()).getColor()
                        == ContextCompat.getColor(FullScreenWindow.this, R.color.transparent)) {
            colorAnimator = ObjectAnimator.ofInt(rootView, "backgroundColor",
                    ContextCompat.getColor(FullScreenWindow.this, R.color.transparent),
                    ContextCompat.getColor(FullScreenWindow.this, R.color.grey_trans));
            container.setVisibility(View.VISIBLE);
        }

        if (colorAnimator == null)
            return;
        colorAnimator.setDuration(500);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.start();
    }

    @Override
    public StandOutLayoutParams getParams(int id, Window window) {
        return new StandOutLayoutParams(id);
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
        return getString(R.string.title_notification);
    }

    @Override
    public String getPersistentNotificationMessage(int id) {
        return null;
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
        clearFocusTimer.cancel();
        return super.onHide(id, window);
    }

    @Override
    public boolean onClose(int id, Window window) {
        isShown.setValue(false);
        isHidden.setValue(true);
        clearFocusTimer.cancel();
        return super.onClose(id, window);
    }

    public class ClearFocusTimer extends CountDownTimer {

        private static final int DELAY_MILLIS = 3000;
        private static final int INTERVAL = 1000;

        public ClearFocusTimer() {
            super(DELAY_MILLIS, INTERVAL);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (rootView != null) {
                fadeViewAnimator(true);
            }
            getWindow(MAIN_WINDOW_ID).onFocus(false);
        }
    }
}
