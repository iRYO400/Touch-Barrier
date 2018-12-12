package wei.mark.standouttest;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import wei.mark.standout.StandOutWindow;
import wei.mark.standouttest.floatingfolders.FloatingFolder;

public class MainActivity extends AppCompatActivity {
    public final static int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CODE);
            return;
        }

        showOverlay();
    }

    private void showOverlay() {
        StandOutWindow.closeAll(this, SimpleWindow.class);
        StandOutWindow.closeAll(this, FullScreenWindow.class);
        StandOutWindow.closeAll(this, MultiWindow.class);
        StandOutWindow.closeAll(this, WidgetsWindow.class);
        StandOutWindow.closeAll(this, FloatingFolder.class);

        // Remove comments as needed to test different parts of the library
//        StandOutWindow.show(this, MostBasicWindow.class, StandOutWindow.DEFAULT_ID);
//        StandOutWindow.show(this, SimpleWindow.class, StandOutWindow.DEFAULT_ID);
        StandOutWindow.show(this, FullScreenWindow.class, FullScreenWindow.FULL_SCREEN_WINDOW_ID);
//        StandOutWindow.show(this, MultiWindow.class, StandOutWindow.DEFAULT_ID);
//        StandOutWindow.show(this, WidgetsWindow.class, StandOutWindow.DEFAULT_ID);
//        FloatingFolder.showFolders(this);

        toggleImmersiveMode();

        finish();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void toggleImmersiveMode() {


//        if (immersiveOff) {
//            newUiOptions = newUiOptions ^ View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            newUiOptions = newUiOptions ^ View.SYSTEM_UI_FLAG_FULLSCREEN;
//            newUiOptions = newUiOptions ^ View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//            newUiOptions = newUiOptions ^ View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        } else {
//            newUiOptions = newUiOptions | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//            newUiOptions = newUiOptions | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            newUiOptions = newUiOptions | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//            newUiOptions = newUiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//        }

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            // if so check once again if we have permission */
            if (Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this)) {
                showOverlay();
            } else {
                finish();
            }
        }
    }
}
