package wei.mark.standouttest.accessibility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import static wei.mark.standouttest.quick_tile.QuickTilesService.TAG;
import static wei.mark.standouttest.utils.IntentKeys.INTENT_ACTION_TOGGLE;
import static wei.mark.standouttest.utils.IntentKeys.INTENT_FILTER_ACCESSIBILITY;
import static wei.mark.standouttest.utils.IntentKeys.INTENT_TOGGLE_BARRIER;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, INTENT_ACTION_TOGGLE)) {
            boolean enable = intent.getBooleanExtra(INTENT_TOGGLE_BARRIER, false);
            if (enable)
                enableBarrier(context);
        }
    }

    private void enableBarrier(Context context) {
        Intent intent = new Intent(INTENT_FILTER_ACCESSIBILITY);
        intent.putExtra(INTENT_TOGGLE_BARRIER, true);
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(intent);
    }
}
