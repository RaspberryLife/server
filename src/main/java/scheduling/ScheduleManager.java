package scheduling;

import util.Log;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Peter Mösenthin.
 */
public class ScheduleManager {

    public static final String DEBUG_TAG = ScheduleManager.class.getSimpleName();

    public void test(){
        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            Log.add(DEBUG_TAG, "Unable to create scheduler");
        }
        if(scheduler != null){
            try {
                scheduler.start();
            } catch (SchedulerException e) {
                Log.add(DEBUG_TAG, "Unable to start scheduler");
            }
        }

        JobDetail job = newJob(TimeLogJob.class)
                .withIdentity("rbl_test_job", "rbl_scheduler_group_1")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(60)
                        .repeatForever())
                .build();

        try {
            if (scheduler != null) {
                scheduler.scheduleJob(job, trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
