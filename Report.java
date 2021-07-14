import java.util.Random;

public class Report {
    private Random random = new Random();

    public String name;
    public int size;
    public String type;
    public Boolean command_response;
    public Boolean transmitting = true;
    public int transmission_time;
    public String speed = "high";

    public Report(String name) {
        this.name = name;
        int rand = random.nextInt(8);
        this.command_response = (rand == 0 || rand == 1 || rand == 2);

        if (name == "command") {
            this.size = 100;
            this.type = "command";
            this.command_response = false;
        } else if (name == "instruments") {
            this.size = random.nextInt(104857600 - 102400) + 102400;
            this.type = "data";
        } else if (name == "shutdown") {
            this.size = 100;
            this.type = "shutdown";
            this.command_response = false;
        } else if (name == "complete") {
            this.size = 100;
            this.type = "complete";
            this.command_response = false;
        } else if (name == "response") {
            this.size = random.nextInt(24857600 - 102400) + 102400;
            this.type = "response";
            this.command_response = false;
        } else if (name.equals("update")){
            this.size = random.nextInt(104857600 - 102400) + 102400;
            this.type = "update";
            this.command_response = false;
        } else {
            this.size = random.nextInt(10000);
            this.type = "telemetry";
        }
    }
}
