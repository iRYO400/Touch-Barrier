package workshop.akbolatss.tools.barrier.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
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
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import workshop.akbolatss.tools.barrier.R;
import workshop.akbolatss.tools.barrier.ui.common.PatterLockViewImpl;
import workshop.akbolatss.tools.barrier.ui.lock_screen.ScreenLockType;

import static workshop.akbolatss.tools.barrier.utils.HawkKeys.LOCK_TYPE_INDEX;
import static workshop.akbolatss.tools.barrier.utils.HawkKeys.PATTERN_DOTS;
import static workshop.akbolatss.tools.barrier.utils.IntentKeys.INTENT_FILTER_ACCESSIBILITY;
import static workshop.akbolatss.tools.barrier.utils.IntentKeys.INTENT_TOGGLE_BARRIER;

public class BarrierAccessibilityService extends AccessibilityService {

    /**
     * Root view of barrier. Instantiates when service is connected
     */
    private FrameLayout mRoot;
    /**
     * Background of Barrier view
     */
    private View background;
    /**
     * Group of one or several views. Used at creating to easily control visibility of group views
     */
    private Group container;
    /**
     * Type of lock {@link ScreenLockType}
     */
    private ScreenLockType lockType;
    /**
     * Var of lock view. Can be {@link PatternLockView}, or...
     */
    private View lockView;

    private final ClearFocusTimer clearFocusTimer = new ClearFocusTimer();

    public static MutableLiveData<Boolean> isBarrierEnabled = new MutableLiveData<>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        mRoot = new FrameLayout(this);
        isBarrierEnabled.setValue(false);

        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processIntent(intent);
            }
        }, new IntentFilter(INTENT_FILTER_ACCESSIBILITY));
    }

    private void processIntent(Intent intent) {
        if (intent.hasExtra(INTENT_TOGGLE_BARRIER)) {
            boolean enable = intent.getBooleanExtra(INTENT_TOGGLE_BARRIER, false);
            if (enable)
                enableUntouchableView();
            else
                disableUntouchableView();
        }
    }

    private void enableUntouchableView() {
        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            return;
        }

        isBarrierEnabled.setValue(true);

        View view = createView();

        initView(view);
        initLockScreen(view);

        unFocusViewOnTouch(view);

        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(it);
    }

    /**
     * Create an overlay based on {@link WINDOW_SERVICE}
     *
     * @return created view
     */
    private View createView() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT
        );


        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.barrier_view, mRoot);
        wm.addView(mRoot, lp);
        return view;
    }


    private void disableUntouchableView() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        try {
            isBarrierEnabled.setValue(false);
            wm.removeView(mRoot);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void initView(View view) {
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
        container.setReferencedIds(new int[]{R.id.app_icon});
        if (lockType == ScreenLockType.PATTERN) {
            container.setReferencedIds(new int[]{R.id.pattern_lock_view, R.id.app_icon});
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

    private void setPatternLock(View view) {
        lockView = view.findViewById(R.id.pattern_lock_view);
        PatternLockView patternLockView = (PatternLockView) lockView;
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
                patternLockView.clearPattern();
                lockSucceeded();
            }
        };
        patternLockView.addPatternLockListener(patternListener);
    }

    private void lockSucceeded() {
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
        colorAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (lockType == ScreenLockType.PATTERN)
                    ((PatternLockView) lockView).clearPattern();
            }
        });
        colorAnimator.setDuration(500);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.start();
    }

    public class ClearFocusTimer extends CountDownTimer {

        private static final int DELAY_MILLIS = 4000;
        private static final int INTERVAL = 1000;

        ClearFocusTimer() {
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
}
