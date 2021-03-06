package rbl.scheduling;

import com.google.common.eventbus.Subscribe;
import rbl.data.model.logic.Logic;
import rbl.event.ScheduleEvent;
import rbl.event.SystemEvent;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import rbl.event.EventBusService;
import rbl.data.DataBaseService;
import rbl.util.Log;

import java.util.List;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;
import static org.quartz.CronScheduleBuilder.monthlyOnDayAndHourAndMinute;
import static org.quartz.CronScheduleBuilder.weeklyOnDayAndHourAndMinute;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Peter Mösenthin.
 */
public class ScheduleService
{

	public static final String DEBUG_TAG = ScheduleService.class.getSimpleName();

	private static ScheduleService instance = new ScheduleService();

	public static final String SCHEDULER_GROUP = "rbl_scheduler_group";
	public static final String TRIGGER_GROUP = "rbl_trigger_group";

	private Scheduler scheduler = null;

	public static void register()
	{
		EventBusService.register(instance);
	}

	private ScheduleService()
	{
	}

	@Subscribe
	public void handleSystemEvent(SystemEvent e)
	{
		switch (e.getType())
		{
			case START_SCHEDULER:
				start();
				break;
			case STOP_SCHEDULER:
				stop();
				break;
			case RESTART_SCHEDULER:
				restart();
				break;
		}
	}

	@Subscribe
	public void handleScheduleEvent(ScheduleEvent e)
	{
		switch (e.getType())
		{
			case START_TIME_LOG:
				startTimeLogJob(e);
				break;
			case START_RESOURCE_LOG:
				startResourceLogJob(e);
				break;
			case REBUILD_DATABASE:
				buildFromDatabase();
				break;
			case EXTENSION:
				startExtensionDailyTimerJob(e);
				break;
		}
	}

	private void restart()
	{
		stop();
		start();
	}

	private void stop()
	{
		Log.add(DEBUG_TAG, "Stopping...");
		try
		{
			scheduler.shutdown();
		}
		catch (SchedulerException e)
		{
			Log.add(DEBUG_TAG, "Unable to stop scheduler");
		}
		scheduler = null;
	}

	private void start()
	{
		Log.add(DEBUG_TAG, "Starting...");
		try
		{
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		}
		catch (SchedulerException e)
		{
			Log.add(DEBUG_TAG, "Unable to create scheduler");
		}
		if (scheduler != null)
		{
			try
			{
				scheduler.start();
			}
			catch (SchedulerException e)
			{
				Log.add(DEBUG_TAG, "Unable to start scheduler");
			}
		}
	}

	/**
	 * Read Logic from database and set up all the scheduling
	 */
	private void buildFromDatabase()
	{
		Log.add(DEBUG_TAG, "Building schedule from database");
		try
		{
			scheduler.clear();
			List logics = DataBaseService.getInstance().getList(DataBaseService.DataType.LOGIC);
			Log.add(DEBUG_TAG, "Found " + logics.size() +  " logics");

			logics.forEach((o) -> addLogicJob((Logic) o));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void addLogicJob(Logic logic){

		if(Logic.EXECUTION_FREQUENCY_IMMEDIATELY == logic.getExecFrequency()){
			return;
		}

		ScheduleBuilder sb = getScheduleForLogic(logic);

		addJob(
				getJobDetailForLogic(logic),
				getLogicTrigger(logic, sb)
		);
	}

	private ScheduleBuilder getScheduleForLogic(Logic logic){
		ScheduleBuilder sb = null;

		switch (logic.getExecFrequency())
		{
			case Logic.EXECUTION_FREQUENCY_MINUTELY:
				sb = simpleSchedule()
						.withIntervalInMinutes(1).repeatForever();
				break;

			case Logic.EXECUTION_FREQUENCY_DAILY:
				sb = dailyAtHourAndMinute(logic.getExecHour(), logic.getExecMinute());
				break;

			case Logic.EXECUTION_FREQUENCY_WEEKLY:
				sb = weeklyOnDayAndHourAndMinute(
						logic.getExecDay(),
						logic.getExecHour(),
						logic.getExecMinute()
				);
				break;

			case Logic.EXECUTION_FREQUENCY_MONTHLY:
				monthlyOnDayAndHourAndMinute(
						logic.getExecDay(),
						logic.getExecHour(),
						logic.getExecMinute()
				);
				break;
		}

		return sb;
	}

	private JobDetail getJobDetailForLogic(Logic logic)
	{
		return newJob(LogicExecutionJob.class)
				.withIdentity("logic_id" + logic.getLogicId() + "_dbjob")
				.withDescription(logic.getLogicName() + "_dbjob")
				.usingJobData("id", logic.getLogicId())
				.build();
	}

	private Trigger getLogicTrigger(Logic logic, ScheduleBuilder scheduleBuilder)
	{
		return newTrigger()
				.withIdentity("logic_id" + logic.getLogicId() + "_trigger", TRIGGER_GROUP)
				.withSchedule(scheduleBuilder)
				.startNow()
				.build();
	}

	/**
	 * Start a job to log the availiable memory
	 *
	 * @param e
	 */
	private void startResourceLogJob(ScheduleEvent e)
	{
		Log.add(DEBUG_TAG, "Starting resource log");
		JobDetail job = newJob(ResourceLogJob.class)
				.withIdentity(e.getIdentity(), SCHEDULER_GROUP)
				.withDescription("ResourceLogJob")
				.build();
		TriggerBuilder trigger = newTrigger()
				.withIdentity(e.getIdentity() + "trigger", TRIGGER_GROUP)
				.startNow();
		SimpleScheduleBuilder schedule = simpleSchedule().repeatForever();
		if (e.getInterval().containsKey(RepeatInterval.SECOND))
		{
			schedule.withIntervalInSeconds(e.getInterval().get(RepeatInterval.SECOND));
		}
		trigger.withSchedule(schedule);
		addJob(job, trigger.build());
	}

	/**
	 * Start a job to log the current time
	 *
	 * @param e
	 */
	private void startTimeLogJob(ScheduleEvent e)
	{
		Log.add(DEBUG_TAG, "Starting time log job");
		JobDetail job = newJob(TimeLogJob.class)
				.withIdentity(e.getIdentity(), SCHEDULER_GROUP)
				.withDescription("TimeLogJob")
				.build();
		TriggerBuilder trigger = newTrigger()
				.withIdentity(e.getIdentity() + "trigger", TRIGGER_GROUP)
				.startNow();
		SimpleScheduleBuilder schedule = simpleSchedule().repeatForever();
		if (e.getInterval().containsKey(RepeatInterval.SECOND))
		{
			schedule.withIntervalInSeconds(e.getInterval().get(RepeatInterval.SECOND));
		}
		trigger.withSchedule(schedule);
		addJob(job, trigger.build());
	}

	private void startExtensionDailyTimerJob(ScheduleEvent e)
	{
		Log.add(DEBUG_TAG, "Starting time log job");
		JobDetail job = newJob(e.getJob().getClass())
				.withIdentity(e.getIdentity(), SCHEDULER_GROUP)
				.withDescription("ExtensionJob")
				.build();
		TriggerBuilder trigger = newTrigger()
				.withIdentity(e.getIdentity() + "trigger", TRIGGER_GROUP)
				.startNow();
		DailyTimeIntervalScheduleBuilder schedule = DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule();
		TimeOfDay tod = TimeOfDay.hourAndMinuteOfDay(
				e.getInterval().get(RepeatInterval.HOUR),
				e.getInterval().get(RepeatInterval.MINUTE));
		Log.add(DEBUG_TAG, "Creating ExtensionJob @" + tod.toString());
		schedule.endingDailyAt(tod);
		trigger.withSchedule(schedule);
		addJob(job, trigger.build());
	}

	/**
	 * Add a job to the scheduler
	 *
	 * @param job
	 * @param trigger
	 */
	private void addJob(JobDetail job, Trigger trigger)
	{
		Log.add(DEBUG_TAG, "Add job: " + job.toString());
		try
		{
			if (scheduler != null)
			{
				scheduler.scheduleJob(job, trigger);
			}
		}
		catch (SchedulerException e)
		{
			Log.add(DEBUG_TAG, "Unable to add job.", e);
		}
	}

}
