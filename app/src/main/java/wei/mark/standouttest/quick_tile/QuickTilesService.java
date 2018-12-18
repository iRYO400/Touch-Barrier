package wei.mark.standouttest.quick_tile;

import android.annotation.TargetApi;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import wei.mark.standout.StandOutWindow;
import wei.mark.standouttest.FullScreenWindow;
import wei.mark.standouttest.R;
import wei.mark.standouttest.SimpleWindow;

import static wei.mark.standouttest.utils.WindowKeys.MAIN_WINDOW_ID;

@TargetApi(Build.VERSION_CODES.N)
public class QuickTilesService
        extends TileService {

    public static final String TAG = "QuickTilesService";

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        setLabelByState(Tile.STATE_INACTIVE);
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        setLabelByState(Tile.STATE_UNAVAILABLE);
        stopBarrier();
    }

    /**
     * Called when the Quick Settings is on main focus, e.g. opened
     */
    @Override
    public void onStartListening() {
        super.onStartListening();
//        if (FullScreenWindow.isShown != null && FullScreenWindow.isShown.getValue() != null)
//            setLabelByState(FullScreenWindow.isShown.getValue() ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
    }

    /**
     * Called when the Quick Settings is closed
     */
    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        if (isLocked()) {
            Toast.makeText(this, "Please, unlock the device.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (getQsTile().getState() == Tile.STATE_ACTIVE) {
            stopBarrier();
            setLabelByState(Tile.STATE_INACTIVE);
        } else if (getQsTile().getState() == Tile.STATE_INACTIVE) {
            startBarrier();
            setLabelByState(Tile.STATE_ACTIVE);
        }
    }

    private void startBarrier() {
        StandOutWindow.show(this, FullScreenWindow.class, MAIN_WINDOW_ID);
    }

    private void stopBarrier() {
        StandOutWindow.close(this, FullScreenWindow.class, MAIN_WINDOW_ID);
    }

    private void setLabelByState(int state) {
        switch (state) {
            case Tile.STATE_ACTIVE:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_layers_active_24dp));
                FullScreenWindow.isShown.setValue(true);
                break;
            case Tile.STATE_INACTIVE:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_layers_inactive_24dp));
                FullScreenWindow.isShown.setValue(false);
                break;
            case Tile.STATE_UNAVAILABLE:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_layers_unavailable_24dp));
                FullScreenWindow.isShown.setValue(false);
                break;
        }
        getQsTile().setState(state);
        getQsTile().updateTile();
    }
}
