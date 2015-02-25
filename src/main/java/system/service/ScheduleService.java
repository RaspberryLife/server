package system.service;

import com.google.common.eventbus.Subscribe;
import data.model.ExecutionFrequency;
import data.model.Logic;
import event.ScheduleEvent;
import event.SystemEvent;
import org.quartz.*;
import scheduling.InstructionJob;
import scheduling.ResourceLogJob;
import scheduling.TimeLogJob;
import util.Log;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

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
            case REBUILD_DATABASE:
                buildFromDatabase();
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

    /**
     * Read Logic from database and set up all the scheduling 
     */
    private void buildFromDatabase(){
        Log.add(DEBUG_TAG, "Building schedule from database");
        try{
            scheduler.clear();
            List l = DataBaseService.getInstance().readAll(DataBaseService.DataType.LOGIC);
            for(Object o : l){
                Logic lc = (Logic) o;
                JobDetail job = newJob(InstructionJob.class)
                        .withIdentity(lc.getName() + "dbjob")
                        .withDescription(lc.getName() + "dbjob")
                        .usingJobData("id", lc.getLogic_id())
                        .build();
                ScheduleBuilder sb = null;
                switch (lc.getExecution_frequency().getType()) {
                    case ExecutionFrequency.TYPE_IMMEDIATELY:
                        sb = simpleSchedule()
                                .withIntervalInSeconds(1);
                        break;
                    case ExecutionFrequency.TYPE_MINUTELY:
                        sb = simpleSchedule()
                                .withIntervalInMinutes(1);
                        break;
                }

                Trigger trigger = newTrigger()
                        .withIdentity(lc.getName() + " logic", TRIGGER_GROUP)
                        .withSchedule(sb)
                        .startNow()
                        .build();
                addJob(job, trigger);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Start a job to log the availiable memory
     * @param e
     */
    private void startResourceLogJob(ScheduleEvent e) {
        Log.add(DEBUG_TAG, "Starting resource log");
        JobDetail job = newJob(ResourceLogJob.class)
                .withIdentity(e.getIdentity(), SCHEDULER_GROUP)
                .withDescription("ResourceLogJob")
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


    /**
     * Start a job to log the current time 
     * @param e
     */
    private void startTimeLogJob(ScheduleEvent e) {
        Log.add(DEBUG_TAG, "Starting time log job");
        JobDetail job = newJob(TimeLogJob.class)
                .withIdentity(e.getIdentity(), SCHEDULER_GROUP)
                .withDescription("TimeLogJob")
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

    /**
     * Add a job to the scheduler
     * @param job
     * @param trigger
     */
    private void addJob(JobDetail job, Trigger trigger){
        Log.add(DEBUG_TAG, "Adding job " + job.getDescription());
        try {
            if (scheduler != null) {
                scheduler.scheduleJob(job, trigger);
            }
        } catch (SchedulerException e) {
            Log.add(DEBUG_TAG, "Unable to add job.",e);
        }
    }


}
