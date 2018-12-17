package wei.mark.standouttest.quick_tile;

import android.annotation.TargetApi;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import wei.mark.standout.StandOutWindow;
import wei.mark.standouttest.R;
import wei.mark.standouttest.SimpleWindow;

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
        stopBarrier();
        setLabelByState(Tile.STATE_UNAVAILABLE);
    }

    /**
     * Called when the Quick Settings is on main focus, e.g. opened
     */
    @Override
    public void onStartListening() {
        super.onStartListening();
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
        StandOutWindow.closeAll(this, SimpleWindow.class);
        StandOutWindow.show(this, SimpleWindow.class, StandOutWindow.DEFAULT_ID);
    }

    private void stopBarrier() {
        StandOutWindow.closeAll(this, SimpleWindow.class);
    }

    private void setLabelByState(int state) {
        switch (state) {
            case Tile.STATE_ACTIVE:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_layers_active_24dp));
                break;
            case Tile.STATE_INACTIVE:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_layers_inactive_24dp));
                break;
            case Tile.STATE_UNAVAILABLE:
                getQsTile().setIcon(Icon.createWithResource(this, R.drawable.ic_layers_unavailable_24dp));
                break;
        }
        getQsTile().setState(state);
        getQsTile().updateTile();
    }
}
