package rbl.scheduling;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import rbl.util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class LogicExecutionJob implements Job
{

	public static final String DEBUG_TAG = LogicExecutionJob.class.getSimpleName();

	/**
	 * Run Logic instructions
	 *
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		// Get action id, load it from database and execute it
		Log.add(DEBUG_TAG, "Got triggered!" + context.getJobDetail().getJobDataMap().get("id"));
	}
}
