import java.util.*;

public class Components {
    public Components(Mission mission) {
        this.mission = mission;
    }

    private Mission mission;
    private Random random = new Random();
    
    public int fuel = random.nextInt(5000);
    public int thrusters = random.nextInt(90) + 10;
    public int instruments = random.nextInt(90) + 10;
    public int control_systems = random.nextInt(90) + 10;
    public int powerplants = random.nextInt(90) + 10;

    public final int thrusters_rate = random.nextInt(10) + 2;
    public final int instruments_rate = random.nextInt(15) + 5;
    public final int control_systems_rate = random.nextInt(10) + 2;
    public final int powerplants_rate = random.nextInt(10) + 2;

    public float thrusters_rate_count = 0; 
    private float instruments_rate_count = 0; 
    private float control_systems_rate_count = 0; 
    private float powerplants_rate_count = 0; 

    public ArrayList<Report> generateReports() {
        ArrayList<Report> result = new ArrayList<Report>();
        if (!mission.halted) {
            if (this.thrusters_rate == this.thrusters_rate_count) {
                this.thrusters_rate_count = 0;
                Report thruster_telemetry = new Report("thrusters");
                result.add(thruster_telemetry);
            } else this.thrusters_rate_count += 1;

            if (this.instruments_rate == this.instruments_rate_count) {
                this.instruments_rate_count = 0;
                Report instruments_data = new Report("instruments");
                result.add(instruments_data);
            } else this.instruments_rate_count += 1;

            if (this.control_systems_rate == this.control_systems_rate_count) {
                this.control_systems_rate_count = 0;
                Report control_systems_telemetry = new Report("control systems");
                result.add(control_systems_telemetry);
            } else this.control_systems_rate_count += 1;

            if (this.powerplants_rate == this.powerplants_rate_count) {
                this.powerplants_rate_count = 0;
                Report powerplants_telemetry = new Report("powerplants");
                result.add(powerplants_telemetry);
            } else this.powerplants_rate_count += 1;
        }
        return result;
    }
} 
