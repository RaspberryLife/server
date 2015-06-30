package scheduling;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import util.Log;

/**
 * Created by Peter Mösenthin.
 */
public class ResourceLogJob implements Job
{

	public static final String DEBUG_TAG = ResourceLogJob.class.getSimpleName();

	/**
	 * Write the current memory usage to the log output
	 *
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		Runtime runtime = Runtime.getRuntime();

		long maxMemory = runtime.maxMemory();
		long allocatedMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();

		int mib = 1024 * 1024; // MiB

		float percentage = maxMemory / allocatedMemory;

		Log.add(DEBUG_TAG, "JVM MEMORY:"
				+ " Max available: " + maxMemory / mib + " MiB //"
				+ " Memory available: " + allocatedMemory / mib + " MiB //"
				+ " Free for use: " + freeMemory / mib + " MiB");
	}

}
