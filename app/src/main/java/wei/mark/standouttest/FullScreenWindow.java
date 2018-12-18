package wei.mark.standouttest;

import android.app.PendingIntent;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import wei.mark.standouttest.ui.settings.SettingsActivity;

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
        layout.setOnClickListener(
                v -> new Handler().postDelayed(
                        () -> getWindow(MAIN_WINDOW_ID).onFocus(false), 1500));
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
}
