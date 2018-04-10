package ccn2279.a16031806a.nodrinknolife.sync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class ReminderUtilities {
    private static final int REMINDER_INTERVAL_MINUTES = 15;
    private static final long REMINDER_INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toMillis(REMINDER_INTERVAL_MINUTES));
    private static boolean sInitialized;

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {
        if (sInitialized) {
            return;
        }

        ComponentName serviceComponent = new ComponentName(context, ccn2279.a16031806a.nodrinknolife.sync.WaterReminderJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(REMINDER_INTERVAL_SECONDS);
        } else {
            builder.setPeriodic(REMINDER_INTERVAL_SECONDS);
        }

        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        sInitialized = true;
    }
}
