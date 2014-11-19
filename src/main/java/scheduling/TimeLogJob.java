package scheduling;

import data.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Calendar;

/**
 * Created by Peter Mösenthin.
 */
public class TimeLogJob implements Job {

    public static final String DEBUG_TAG = TimeLogJob.class.getSimpleName();

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        Log.add(DEBUG_TAG, "The time is " + c.getTime().toString());
    }

}
