import java.util.Random;

public class Stages {
    private Random random = new Random();
    public String current_stage = "Boost";

    // Boost Stage
    public int boost_duration = 0;
    public Boolean boost_failure = random.nextInt(9) == 0;
    public Boolean boost_update = random.nextInt(3) == 0;

    // Interplanetary Transit
    public int transit_duration = random.nextInt(15) + 5;
    public Boolean transit_failure = random.nextInt(9) == 0;
    public Boolean transit_update = random.nextInt(3) == 0;

    // Entry/Landing Stage
    public int landing_duration = 0;
    public Boolean landing_failure = random.nextInt(9) == 0;
    public Boolean landing_update = random.nextInt(3) == 0;

    // Exploration Stage
    public int exploration_duration = random.nextInt(15) + 5;
    public Boolean exploration_failure = random.nextInt(9) == 0;
    public Boolean exploration_update = random.nextInt(3) == 0;

    public boolean currentStageStatus() {
        if (current_stage.equals("Boost")) {
            return boost_failure;
        } else if (current_stage.equals("Transit")) {
            return transit_failure;
        } else if (current_stage.equals("Exploration")) {
            return exploration_failure;
        } else return landing_failure;
    }

    public void fixCurrentStage() {
        if (current_stage.equals("Boost")) {
            boost_failure = false;
        } else if (current_stage.equals("Transit")) {
            transit_failure = false;
        } else if (current_stage.equals("Exploration")) {
            exploration_failure = false;
        } else landing_failure = false;
    }

    public boolean updateAvailable() {
        if (current_stage.equals("Boost")) {
            return boost_update;
        } else if (current_stage.equals("Transit")) {
            return transit_update;
        } else if (current_stage.equals("Exploration")) {
            return exploration_update;
        } else return landing_update;
    }

    public String[] getCurrentStage() {
        int duration = 0;
        Boolean failure = false, update = false;

        if (this.current_stage == "Boost") {
            duration = this.boost_duration;
            failure = this.boost_failure;
            update = this.boost_update;
        } else if (this.current_stage == "Transit") {
            duration = this.transit_duration;
            failure = this.transit_failure;
            update = this.transit_update;
        } else if (this.current_stage == "Landing") {
            duration = this.landing_duration;
            failure = this.landing_failure;
            update = this.landing_update;
        } else if (this.current_stage == "Exploration") {
            duration = this.exploration_duration;
            failure = this.exploration_failure;
            update = this.exploration_update;
        } 
        
        String[] result = {Integer.toString(duration), Boolean.toString(failure), Boolean.toString(update)};
        return result;
    }
}
