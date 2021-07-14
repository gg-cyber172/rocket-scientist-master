import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;

public class Mission {
    public Mission(int id, BufferedWriter bw) {
        this.id = id;
        this.buffer = bw;
        network.start();
    }

    private Random random = new Random();
    public BufferedWriter buffer;  
    public JLabel mission_label;

    public int id;
    public int mission_start = random.nextInt(20) + 5;
    public int update_time_left = 0;
    public int reports_delivered = 0;
    public int months_operating = 0;
    public int reports_sent = 0;

    public boolean complete = false;
    public boolean updating = false;
    public boolean creating_update = false;
    public boolean shutdown = false;
    public boolean status = false;
    public boolean halted = false;
    public boolean failure = false;
    public boolean transmitting_response = false;

    public ArrayList<Report> report_log = new ArrayList<Report>();
    public Components components = new Components(this);
    public Stages stages = new Stages();
    public String destination = this.getDestination();
    public Network network = new Network(this);

    private String getDestination() {
        if (components.fuel > 0 && components.fuel <= 881) return new String("Mercury");
        if (components.fuel > 881 && components.fuel <= 1500) return new String("Mars");
        if (components.fuel > 1500 && components.fuel <= 2100) return new String("Venus");
        if (components.fuel > 2100 && components.fuel <= 2700) return new String("Jupiter");
        if (components.fuel > 2700 && components.fuel <= 3400) return new String("Saturn");
        if (components.fuel > 3400 && components.fuel <= 4200) return new String("Uranus");
        if (components.fuel > 4200 && components.fuel <= 4500) return new String("Neptune");
        return new String("Pluto");
    }

    public void checkForStageFailure(){
        if (stages.currentStageStatus()) {
            failure = true;
            if (stages.updateAvailable()) {
                update_time_left = random.nextInt(5) + 1;
                creating_update = true;
            }
        }
    }
}