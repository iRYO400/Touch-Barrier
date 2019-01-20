package workshop.akbolatss.tools.barrier.accessibility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static workshop.akbolatss.tools.barrier.utils.IntentKeys.INTENT_ACTION_TOGGLE;
import static workshop.akbolatss.tools.barrier.utils.IntentKeys.INTENT_FILTER_ACCESSIBILITY;
import static workshop.akbolatss.tools.barrier.utils.IntentKeys.INTENT_TOGGLE_BARRIER;

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
