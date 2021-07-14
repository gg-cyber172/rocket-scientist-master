import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MissionControl {
    private static final int total_missions = 10;
    private static BufferedWriter buffered_log = null;
    private static Random random = new Random();

    private static GUI gui;
    private static int mission_count = 0;
    private static ArrayList<Mission> mission_list = new ArrayList<Mission>();
    private static ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());

    private static int active_mission = 0; 
    private static int pending_missions = total_missions; 
    private static int shutdown_missions = 0; 
    private static int completed_missions = 0; 

    private static boolean close = false;

    public static void main(String[] args) {
        initializeBufferedWriter();

        for (var i = 0; i < total_missions; i++) {
            mission_count++;
            Mission mission = new Mission(mission_count, buffered_log);
            mission_list.add(mission);

            MissionCycle mission_cycle = new MissionCycle(mission);
            executor.scheduleAtFixedRate(mission_cycle, 0, 1000, TimeUnit.MILLISECONDS);
        }

        logMissionDetails();
        gui = new GUI(mission_list);
        missionUpdates();
    }

    private static void missionUpdates() {
        Timer timer = new Timer();
        TimerTask missionUpdates = new TimerTask() {
            @Override
            public void run() { 
                if (!close) {
                    gui.updateReportList();

                    int delivered_reports = 0, total_reports = 0;
                    for (var i = 0; i < mission_list.size(); i++) {
                        Mission mission = mission_list.get(i);
                        delivered_reports += mission.reports_delivered;
                        total_reports += mission.reports_sent;
                    }
                    gui.updateDashboard(active_mission, pending_missions, total_reports, delivered_reports, shutdown_missions, completed_missions);
                    if ((shutdown_missions + completed_missions) == total_missions) closeLog();
                }
            }
        };
        timer.schedule(missionUpdates, 0, 1000);
    }

    private static void closeLog() {
        Timer timer = new Timer();
        TimerTask closeBuffer = new TimerTask() {
            @Override
            public void run() {
                if (!close) {
                    try{
                        if(buffered_log != null) {
                            buffered_log.close();
                            executor.shutdown();
                            close = true;
                        }
                    } catch(Exception ex){
                        System.out.println("Error in closing the BufferedWriter"+ex);
                    }
                } 
            }
        };
        timer.schedule(closeBuffer, 10000, 1);
    }

    private static void initializeBufferedWriter() {
        try {
            File file = new File("./mission_log.dat");
            if (!file.exists())
                file.createNewFile();
            else System.out.println("File already exists, overwriting contents.");
            FileWriter fw = new FileWriter(file);
            buffered_log = new BufferedWriter(fw);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void logMissionDetails() {
        try {
            for (var i = 0; i < mission_list.size(); i++) {
                buffered_log.write("Apollo " + mission_list.get(i).id + "\n");
                buffered_log.write("------------------------------" + "\n");
                buffered_log.write("Fuel: " + mission_list.get(i).components.fuel + "\n");
                buffered_log.write("Destination: " + mission_list.get(i).destination + "\n");
                buffered_log.write("Months to Launch: " + mission_list.get(i).mission_start + "\n");
                buffered_log.write("---" + "\n");
                buffered_log.write("Stage: Boost" + "\n");
                buffered_log.write("Stage Duration: " + mission_list.get(i).stages.boost_duration + " month(s)" + "\n");
                buffered_log.write("Stage Failure: " + mission_list.get(i).stages.boost_failure + "\n");
                buffered_log.write("Update Available: " + mission_list.get(i).stages.boost_update + "\n");
                buffered_log.write("---" + "\n");
                buffered_log.write("Stage: Transit" + "\n");
                buffered_log.write("Stage Duration: " + mission_list.get(i).stages.transit_duration + " month(s)" + "\n");
                buffered_log.write("Stage Failure: " + mission_list.get(i).stages.transit_failure + "\n");
                buffered_log.write("Update Available: " + mission_list.get(i).stages.transit_update + "\n");
                buffered_log.write(("---" + "\n"));
                buffered_log.write("Stage: Landing" + "\n");
                buffered_log.write("Stage Duration: " + mission_list.get(i).stages.landing_duration + " month(s)" + "\n");
                buffered_log.write("Stage Failure: " + mission_list.get(i).stages.landing_failure + "\n");
                buffered_log.write("Update Available: " + mission_list.get(i).stages.landing_update + "\n");
                buffered_log.write("---" + "\n");
                buffered_log.write("Stage: Exploration" + "\n");
                buffered_log.write("Stage Duration: " + mission_list.get(i).stages.exploration_duration + " month(s)" + "\n");
                buffered_log.write("Stage Failure: " + mission_list.get(i).stages.exploration_failure + "\n");
                buffered_log.write("Update Available: " + mission_list.get(i).stages.exploration_update + "\n");
                buffered_log.write("---" + "\n");
                buffered_log.write("Thrusters: " + mission_list.get(i).components.thrusters + " units" + " (" + mission_list.get(i).components.thrusters_rate + "/month)\n");
                buffered_log.write("Instruments: " + mission_list.get(i).components.instruments + " units" + " (" + mission_list.get(i).components.instruments_rate + "/month)\n");
                buffered_log.write("Control Systems: " + mission_list.get(i).components.control_systems + " units" + " (" + mission_list.get(i).components.control_systems_rate + "/month)\n");
                buffered_log.write("Powerplants: " + mission_list.get(i).components.powerplants + " units" + " (" + mission_list.get(i).components.powerplants_rate + "/month)\n");
                buffered_log.write("\n\n");
            }
            buffered_log.write("Mission Reports\n---\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static class MissionCycle implements Runnable {
        private Mission mission;

        private MissionCycle(Mission mission) {
            this.mission = mission;
        }

        private void sendReportBurst() {
            if (random.nextInt(4) == 0) mission.network.reportQueue.add(new Report("thrusters"));
            if (random.nextInt(6) == 0) mission.network.reportQueue.add(new Report("instruments"));
            if (random.nextInt(4) == 0) mission.network.reportQueue.add(new Report("control systems"));
            if (random.nextInt(4) == 0) mission.network.reportQueue.add(new Report("powerplants"));
        }

        public void run() { 
            if(mission.mission_start == 0 && !mission.status) {
                synchronized(this) {
                    active_mission++;
                    pending_missions--;
                }
                mission.status = true;
                mission.checkForStageFailure();
            } else if (!mission.status)
                mission.mission_start = mission.mission_start - 1;

            if (mission.failure && !mission.shutdown) {
                if (!mission.stages.updateAvailable()) {
                    mission.shutdown = true;
                    mission.network.reportQueue.add(new Report("shutdown"));
    
                    synchronized (this) {
                        if (mission.status) {
                            active_mission--;
                            shutdown_missions++;
                        }
                    }
                } 
            }

            if (mission.creating_update) {
                if (mission.update_time_left <= 0) {
                    mission.creating_update = false;
                    mission.updating = true;
                    mission.network.reportQueue.add(new Report("update"));
                } else mission.update_time_left--;
            }

            if (mission.halted && !mission.transmitting_response) {
                mission.network.reportQueue.add(new Report("response"));
                mission.transmitting_response = true;
            }

            if (mission.status && !mission.halted && !mission.shutdown && !mission.complete && !mission.creating_update && !mission.updating) {
                mission.months_operating++;

                if (mission.stages.current_stage.equals("Boost") && !mission.stages.boost_failure) {
                    mission.stages.current_stage="Transit";
                    mission.checkForStageFailure();
                    sendReportBurst();
                } else if(mission.stages.current_stage.equals("Transit") && mission.months_operating > mission.stages.transit_duration && !mission.stages.transit_failure) {
                    mission.stages.current_stage="Landing";
                    mission.checkForStageFailure();
                    sendReportBurst();
                } else if(mission.stages.current_stage.equals("Landing") && !mission.stages.landing_failure) {
                    mission.stages.current_stage="Exploration";
                    mission.checkForStageFailure();
                    sendReportBurst();
                } else if(mission.stages.current_stage.equals("Exploration") && (mission.months_operating > (mission.stages.transit_duration + mission.stages.exploration_duration)) && !mission.stages.exploration_failure) {
                    if (!mission.complete) mission.network.reportQueue.add(new Report("complete"));
                    mission.complete = true;
                    synchronized (this) {
                        active_mission--;
                        completed_missions++;
                    }
                }

                ArrayList<Report> reports = mission.components.generateReports();
                for (int i = 0; i < reports.size(); i++) {
                    Report report = reports.get(i);
                    mission.network.reportQueue.add(report);
                }
            }
        }
    }
}
