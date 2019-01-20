package workshop.akbolatss.tools.barrier.quick_tile;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import workshop.akbolatss.tools.barrier.R;
import workshop.akbolatss.tools.barrier.accessibility.BarrierAccessibilityService;

import static workshop.akbolatss.tools.barrier.accessibility.AccessibilityServiceHelper.isAccessibilityServiceEnabled;
import static workshop.akbolatss.tools.barrier.utils.IntentKeys.INTENT_FILTER_ACCESSIBILITY;
import static workshop.akbolatss.tools.barrier.utils.IntentKeys.INTENT_TOGGLE_BARRIER;

@TargetApi(Build.VERSION_CODES.N)
public class QuickTilesService
        extends TileService {

    public static final String TAG = "DEBUG_TAG";

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        setLabelByState(Tile.STATE_INACTIVE);
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        setLabelByState(Tile.STATE_UNAVAILABLE);
    }

    /**
     * Called when the Quick Settings is on main focus, e.g. opened
     */
    @Override
    public void onStartListening() {
        super.onStartListening();
        if (isAccessibilityServiceEnabled(this, BarrierAccessibilityService.class)) {
            setLabelByState(Tile.STATE_INACTIVE);
        } else {
            setLabelByState(Tile.STATE_UNAVAILABLE);
        }
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
            Toast.makeText(this, getString(R.string.unlock_screen_to_toggle), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(INTENT_FILTER_ACCESSIBILITY);
        intent.putExtra(INTENT_TOGGLE_BARRIER, true);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
    }

    private void stopBarrier() {
        Intent intent = new Intent(INTENT_FILTER_ACCESSIBILITY);
        intent.putExtra(INTENT_TOGGLE_BARRIER, false);
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(intent);
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
