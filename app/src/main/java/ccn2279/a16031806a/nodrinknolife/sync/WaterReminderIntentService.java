package ccn2279.a16031806a.nodrinknolife.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;


/**
 * Updated by Kiros Choi on 2018/04/11.
 *
 * This class uses IntentService so it will not block the main thread.
 */
public class WaterReminderIntentService extends IntentService {
    public WaterReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        ReminderTasks.executeTask(this, action);
    }
}