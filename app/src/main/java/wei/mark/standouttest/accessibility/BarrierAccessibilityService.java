package wei.mark.standouttest.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.constraint.Group;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import wei.mark.standouttest.R;
import wei.mark.standouttest.ui.common.PatterLockViewImpl;
import wei.mark.standouttest.ui.common.PinLockViewImpl;
import wei.mark.standouttest.ui.lock_screen.ScreenLockType;

import static wei.mark.standouttest.utils.HawkKeys.LOCK_TYPE_INDEX;
import static wei.mark.standouttest.utils.HawkKeys.PATTERN_DOTS;
import static wei.mark.standouttest.utils.HawkKeys.PIN_CODE;

public class BarrierAccessibilityService extends AccessibilityService {

    private FrameLayout mRoot;
    public static final String INTENT_FILTER_ACCESSIBILITY = "barrierServiceEnabled";
    public static final String INTENT_FILTER_ACTIVITY = "barrierActivityToggle";
    public static final String INTENT_ENABLE = "enableBarrier";

    private View background;
    private Group container;
    private ScreenLockType lockType;

    private final ClearFocusTimer clearFocusTimer = new ClearFocusTimer();

    public static boolean isActive = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        mRoot = new FrameLayout(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(INTENT_ENABLE)) {
                    boolean enable = intent.getBooleanExtra(INTENT_ENABLE, false);
                    if (enable)
                        enableUntouchableView();
                    else
                        disableUntouchableView();
                    isActive = enable;
                }
            }
        }, new IntentFilter(INTENT_FILTER_ACCESSIBILITY));
    }

    private void enableUntouchableView() {
        // Create an overlay and display the action bar
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT
        );


        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.full_screen, mRoot);
        wm.addView(mRoot, lp);

        setView(view);
        initLockScreen(view);

        unFocusViewOnTouch(view);
    }


    private void disableUntouchableView() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            wm.removeView(mRoot);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void setView(View view) {
        background = view.findViewById(R.id.background);
        container = background.findViewById(R.id.container);
    }

    private void getLockType() {
        if (Hawk.contains(LOCK_TYPE_INDEX)) {
            lockType = Hawk.get(LOCK_TYPE_INDEX);
        } else {
            lockType = ScreenLockType.NONE;
        }
    }

    private void initLockScreen(View view) {
        getLockType();

        if (lockType == ScreenLockType.PIN) {
            container.setReferencedIds(new int[]{R.id.pin_lock_view, R.id.indicator_dots});
            setPinLock(view);
        } else if (lockType == ScreenLockType.PATTERN) {
            container.setReferencedIds(new int[]{R.id.pattern_lock_view});
            setPatternLock(view);
        } else {
            ImageView icon = view.findViewById(R.id.app_icon);
            icon.setOnClickListener(v -> onBackPressed());
        }
    }

    private boolean doubleBackToExitPressedOnce = false;

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            lockSucceeded();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.double_tap_toast), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
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
        Intent intent = new Intent(INTENT_FILTER_ACTIVITY);
        intent.putExtra(INTENT_ENABLE, false);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
        disableUntouchableView();
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
                ((ColorDrawable) background.getBackground()).getColor()
                        == ContextCompat.getColor(this, R.color.grey_trans)) {
            colorAnimator = ObjectAnimator.ofInt(background, "backgroundColor",
                    ContextCompat.getColor(this, R.color.grey_trans),
                    ContextCompat.getColor(this, R.color.transparent));
            container.setVisibility(View.GONE);
        } else if (!reverse &&
                ((ColorDrawable) background.getBackground()).getColor()
                        == ContextCompat.getColor(this, R.color.transparent)) {
            colorAnimator = ObjectAnimator.ofInt(background, "backgroundColor",
                    ContextCompat.getColor(this, R.color.transparent),
                    ContextCompat.getColor(this, R.color.grey_trans));
            container.setVisibility(View.VISIBLE);
        }

        if (colorAnimator == null)
            return;
        colorAnimator.setDuration(500);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.start();
    }

    public class ClearFocusTimer extends CountDownTimer {

        private static final int DELAY_MILLIS = 2000;
        private static final int INTERVAL = 1000;

        public ClearFocusTimer() {
            super(DELAY_MILLIS, INTERVAL);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (background != null) {
                fadeViewAnimator(true);
            }
        }
    }

    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
        ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }
}
