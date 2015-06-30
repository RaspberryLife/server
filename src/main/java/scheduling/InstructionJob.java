package scheduling;

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
		/*
        int id = (Integer) context.get("id");
        Logic l  = (Logic) DataBaseService.getInstance().readId(DataBaseService.DataType.LOGIC, id);
        LogicInitiator li = l.getLogic_initiator().get(0);
        LogicReceiver lr = l.getLogic_receiver().get(0);
        */
	}
}
