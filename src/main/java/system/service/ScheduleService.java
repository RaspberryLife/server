package system.service;

import com.google.common.eventbus.Subscribe;
import event.ScheduleEvent;
import event.SystemEvent;
import org.quartz.*;
import scheduling.ResourceLogJob;
import scheduling.TimeLogJob;
import util.Log;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by Peter Mösenthin.
 */
public class ScheduleService {

    public static final String DEBUG_TAG = ScheduleService.class.getSimpleName();

    private static ScheduleService instance = new ScheduleService();

    public static final String SCHEDULER_GROUP = "rbl_scheduler_group";
    public static final String TRIGGER_GROUP = "rbl_trigger_group";

    private  Scheduler scheduler = null;

    public static void register() {
        EventBusService.register(instance);
    }

    private ScheduleService(){
    }


    @Subscribe
    public void handleSystemEvent(SystemEvent e){
        switch (e.getType()){
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
    public void handleScheduleEvent(ScheduleEvent e){
        switch (e.getType()){
            case START_TIME_LOG:
                startTimeLogJob(e);
                break;
            case START_RESOURCE_LOG:
                startResourceLogJob(e);
                break;
        }
    }


    private void restart() {
        stop();
        start();
    }

    private void stop() {
        Log.add(DEBUG_TAG, "Stopping...");
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            Log.add(DEBUG_TAG, "Unable to stop scheduler");
        }
        scheduler = null;
    }

    private void start() {
        Log.add(DEBUG_TAG, "Starting...");
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
    }

    private void startResourceLogJob(ScheduleEvent e) {
        JobDetail job = newJob(ResourceLogJob.class)
                .withIdentity(e.getIdentity(), SCHEDULER_GROUP)
                .build();
        Trigger trigger = newTrigger()
                .withIdentity(e.getIdentity() + "trigger", TRIGGER_GROUP)
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(e.getInterval())
                        .repeatForever())
                .build();
        addJob(job, trigger);
    }

    private void startTimeLogJob(ScheduleEvent e) {
        JobDetail job = newJob(TimeLogJob.class)
                .withIdentity(e.getIdentity(), SCHEDULER_GROUP)
                .build();
        Trigger trigger = newTrigger()
                .withIdentity(e.getIdentity() + "trigger", TRIGGER_GROUP)
                .startNow()
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(e.getInterval())
                        .repeatForever())
                .build();
        addJob(job, trigger);
    }

    private void addJob(JobDetail job, Trigger trigger){
        try {
            if (scheduler != null) {
                scheduler.scheduleJob(job, trigger);
            }
        } catch (SchedulerException e) {
            Log.add(DEBUG_TAG, "Unable to add job.");
        }
    }


}
