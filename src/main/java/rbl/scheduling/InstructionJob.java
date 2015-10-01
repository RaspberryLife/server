package rbl.scheduling;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Peter Mösenthin.
 */
public class InstructionJob implements Job
{

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
	}
}
