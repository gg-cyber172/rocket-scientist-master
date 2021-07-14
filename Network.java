import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Network extends Thread {
    public Mission mission;
    private Random random = new Random();

    public Boolean low_band_availablity = random.nextInt(4) != 0;
    public Boolean medium_band_availablity = random.nextInt(9) != 0;
    public Boolean high_band_availablity = random.nextInt(99) != 0;

    public ArrayBlockingQueue<Report> reportQueue = new ArrayBlockingQueue<Report>(1000);
    public ExecutorService networkThreads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public ExecutorService network = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public Semaphore semHigh = new Semaphore(1);
    public Semaphore semMed = new Semaphore(1);
    public Semaphore semLow = new Semaphore(1);

    private float low_speed = 0.0025f*10000;
    private float medium_speed = 2f*1000;
    private float high_speed = 2000f*3000;

    public Network(Mission mission) {
        this.mission=mission;
    }

    public void checkIfResponseNeeded(Report report){
        if (report.command_response) mission.halted = true;
    }

    public void sendReport(Semaphore sem, float networkSpeed, Report report){
        try {
            sem.acquire();
            mission.report_log.add(report);
            networkThreads.execute(new SendData(networkSpeed, report, sem));
            checkIfResponseNeeded(report);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void run() {
        try {
            while (!mission.shutdown && !mission.complete) {
                if (reportQueue.size() > 0) {
                    Report report = reportQueue.peek();
                    if (semHigh.availablePermits() == 1 && high_band_availablity) {
                        sendReport(semHigh, high_speed, reportQueue.poll());
                    } else if (semMed.availablePermits() == 1 && medium_band_availablity && report.size <= 20000) {
                        sendReport(semHigh, medium_speed, reportQueue.poll());
                    } else if (semLow.availablePermits() == 1 && low_band_availablity && report.size <= 250) {
                        sendReport(semHigh, low_speed, reportQueue.poll());
                    }
                }
            }
            networkThreads.shutdown();
        }
        catch(Exception e) {
            System.out.println(e);
        }

    }
    
    class SendData implements Runnable {
        private float speed;
        private Report report;
        public Semaphore networkInUse;

        public SendData(float speed, Report report, Semaphore networkInUse){
            this.speed=speed;
            this.report=report;
            this.networkInUse=networkInUse;
        }

        public void run() {
            try {
                mission.reports_sent++;
                report.transmission_time = (int) (report.size/speed);
                TimeUnit.SECONDS.sleep((long) (report.size/speed + (mission.months_operating / 15)));
                networkInUse.release();

                if (speed == 0.0025f*10000) report.speed = "low";
                else if (speed == 2f*1000) report.speed = "medium";
                else if (speed == 2000f*3000) report.speed = "high";

                try {
                    mission.buffer.write("Mission Component " + report.name + " with (Thread ID) " + mission.id + " makes request to network " + report.speed + " after " + mission.months_operating + " months, transmission time " + round((float) report.size/speed) + " secs\n");
                    // mission.buffer.write("[" + mission.id + "] " + report.name + " (" + report.size + " bytes | command: " + report.command_response + " | transmission: " + round((float) report.size/speed) + " secs)\n");
                } catch (Exception e) {
                    System.out.println(e);
                }

                if (report.type == "response") {
                    mission.transmitting_response = false;
                    mission.halted = false;
                }

                if (report.type == "update") {
                    mission.updating = false;
                    mission.stages.fixCurrentStage();
                    mission.failure = false;
                }

                report.transmitting = false;
                mission.reports_delivered++;
            } catch(Exception e) {
                System.out.println(e);
            }
        }
    }

    public float round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }
}
