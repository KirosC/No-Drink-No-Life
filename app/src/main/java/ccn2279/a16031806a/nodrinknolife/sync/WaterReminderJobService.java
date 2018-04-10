package ccn2279.a16031806a.nodrinknolife.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class WaterReminderJobService extends JobService {
    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = WaterReminderJobService.this;
                ReminderTasks.executeTask(context, ReminderTasks.ACTION_CHARGING_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, true);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) {
            mBackgroundTask.cancel(true);
        }
        return true;
    }
}
