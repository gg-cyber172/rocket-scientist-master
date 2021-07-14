import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class GUI implements ActionListener {
    int button_id = 0;
    int current_mission = 1;
    ArrayList<Mission> mission_list = new ArrayList<Mission>();

    private JFrame frame;
    private JPanel dashboard;
    private JPanel mission_control;
    private JPanel mission_data;
    private JPanel mission_log;
    private JTextArea log;
    private JTextArea log_queue;
    private ArrayList<JButton> buttons = new ArrayList<JButton>();

    private JLabel mission_title = new JLabel("Apollo 1");
    private JLabel fuel = new JLabel("<html><font color='#bdbffa'>Fuel: </font>0</html>");
    private JLabel destination = new JLabel("<html><font color='#bdbffa'>Destination: </font>None</html>");
    private JLabel start_time = new JLabel("<html><font color='#bdbffa'>Status: </font>0</html>");
    private JLabel months = new JLabel("<html><font color='#bdbffa'>Months Passed: </font>0 months</html>");

    private JLabel stage = new JLabel("<html><font color='#bdbffa'>Stage: </font>Boost</html>");
    private JLabel duration = new JLabel("<html><font color='#bdbffa'>Stage Duration: </font>0</html>");
    private JLabel failure = new JLabel("<html><font color='#bdbffa'>Stage Failure: </font>false</html>");
    private JLabel update = new JLabel("<html><font color='#bdbffa'>Update Available: </font>false</html>");

    private JLabel thruster_supply = new JLabel("<html><font color='#bdbffa'>Thrusters:</font> " + 0 + " units (" + 0 + "/month) </html>");
    private JLabel instrument_supply = new JLabel("<html><font color='#bdbffa'>Instruments:</font> " + 0 + " units (" + 0 + "/month) </html>");
    private JLabel control_systems_supply = new JLabel("<html><font color='#bdbffa'>Control Systems:</font> " + 0 + " units (" + 0 + "/month) </html>");
    private JLabel powerplant_supply = new JLabel("<html><font color='#bdbffa'>Powerplants:</font> " + 0 + " units (" + 0 + "/month) </html>");
    
    private JLabel active_missions = new JLabel("Active Missions: 0");
    private JLabel pending_missions = new JLabel("Pending Missions: 0");
    private JLabel paused_missions = new JLabel("Halted Missions: 0");
    private JLabel total_reports = new JLabel("Sent Reports: 0");
    private JLabel completed = new JLabel("Completed Missions: 0");
    private JLabel delivered_reports = new JLabel("Delivered Reports: 0");
    private JLabel shutdown = new JLabel("Shutdown Missions: 0");

    Color blue = new Color(16, 17, 44);
    Color green = new Color(63, 192, 139);

    public GUI(ArrayList<Mission> mission_list) {
        this.mission_list = mission_list;

        frame = new JFrame();
        dashboard = new JPanel();
        mission_control = new JPanel();
        mission_data = new JPanel();
        mission_log = new JPanel();

        mission_control.setBorder(BorderFactory.createEmptyBorder(50, 50, 30, 50));
        mission_control.setLayout(new GridLayout(0, 5));
        mission_control.setBackground(blue);

        dashboard.setBorder(BorderFactory.createEmptyBorder(0, 50, 45, 100));
        dashboard.setLayout(new GridLayout(0, 1));
        dashboard.setBackground(blue);

        mission_data.setBorder(BorderFactory.createEmptyBorder(0, 50, 40, 50));
        mission_data.setLayout(new GridLayout(0, 1));
        mission_data.setBackground(blue);

        mission_log.setBorder(BorderFactory.createEmptyBorder(0, 50, 50, 50));
        mission_log.setLayout(new GridLayout(0, 1));
        mission_log.setBackground(blue);

        createMissionToggles();
        loadLogs();
        loadMissionData();
        loadDashboardData();
        loadMission(1);

        JPanel center = new JPanel();
        center.setBackground(blue);
        frame.add(mission_control, BorderLayout.NORTH);
        frame.add(dashboard, BorderLayout.EAST);
        frame.add(center, BorderLayout.CENTER);
        frame.add(mission_data, BorderLayout.WEST);
        frame.add(mission_log, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Mission Control");
        frame.pack();
        frame.setVisible(true);
    }

    private void loadLogs() {
        log = new JTextArea(8, 30);
        log.setHighlighter(null);
        log.setEditable(false);

        log_queue = new JTextArea(8, 30);
        log_queue.setHighlighter(null);
        log_queue.setEditable(false);

        JScrollPane log_queue_scroll = new JScrollPane(log_queue, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mission_log.add(log_queue_scroll);
        JScrollPane log_scroll = new JScrollPane(log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mission_log.add(log_scroll);

        log.setOpaque(false);
        log.setBackground(new Color(0, 0, 0, 0));
        log_scroll.getViewport().setOpaque(false);
        log_scroll.setOpaque(false);
        Border border = BorderFactory.createLineBorder(green);
        log_scroll.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        log.setForeground(Color.WHITE);
        log.setFont(new Font("Arial", Font.BOLD, 12));

        log_queue.setOpaque(false);
        log_queue.setBackground(new Color(0, 0, 0, 0));
        log_queue_scroll.getViewport().setOpaque(false);
        log_queue_scroll.setOpaque(false);
        Border border2 = BorderFactory.createLineBorder(green);
        log_queue_scroll.setBorder(BorderFactory.createCompoundBorder(border2, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        log_queue.setForeground(Color.WHITE);
        log_queue.setFont(new Font("Arial", Font.BOLD, 12));
    }

    private void loadMissionData() {
        mission_data.add(mission_title);
        mission_data.add(fuel);
        mission_data.add(destination);
        mission_data.add(start_time);
        mission_data.add(months);
        mission_data.add(new JLabel("----------------------------------------------------------"));
        mission_data.add(stage);
        mission_data.add(duration);
        mission_data.add(failure);
        mission_data.add(update);
        mission_data.add(new JLabel("----------------------------------------------------------"));
        mission_data.add(thruster_supply);
        mission_data.add(instrument_supply);
        mission_data.add(control_systems_supply);
        mission_data.add(powerplant_supply);

        if (mission_list.size() > 10 && mission_list.size() <= 20) {
            for (var i = 0; i < mission_list.size() - 10; i++)
                mission_data.add(new JLabel(""));
        }

        mission_title.setFont(new Font("Arial", Font.BOLD, 15));
        mission_title.setForeground(green);
        fuel.setForeground(Color.white);
        destination.setForeground(Color.white);
        start_time.setForeground(Color.white);
        months.setForeground(Color.white);
        stage.setForeground(Color.white);
        duration.setForeground(Color.white);
        update.setForeground(Color.white);
        failure.setForeground(Color.white);
        thruster_supply.setForeground(Color.white);
        instrument_supply.setForeground(Color.white);
        control_systems_supply.setForeground(Color.white);
        powerplant_supply.setForeground(Color.white);
    }

    private void loadDashboardData() {
        JLabel dashboard_title = new JLabel("All Missions");
        dashboard_title.setFont(new Font("Arial", Font.BOLD, 15));
        dashboard_title.setForeground(green);

        dashboard.add(dashboard_title);
        dashboard.add(active_missions);
        dashboard.add(pending_missions);
        dashboard.add(paused_missions);
        dashboard.add(shutdown);
        dashboard.add(completed);
        dashboard.add(total_reports);
        dashboard.add(delivered_reports);
        dashboard.add(new JLabel("----------------------------------------------------------"));

        if (mission_list.size() <= 20) {
            for (var i = 0; i < mission_list.size(); i++) {
                Mission mission = mission_list.get(i);   
                String mission_id = Integer.toString(mission.id);

                JLabel mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#bdbffa'>Awaiting Start</font></html>");
                if (mission.id >= 10) mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#bdbffa'>Awaiting Start</font></html>");

                if (mission.status && !mission.halted) {
                    mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#3fc08b'>Active</font></html>");
                    if (mission.id >= 10) mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#3fc08b'>Active</font></html>");
                }
                if (mission.halted && !mission.shutdown) {
                    mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#ffc104'>Halted</font></html>");
                    if (mission.id >= 10) mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#ffc104'>Halted</font></html>");
                } 

                if (mission.status && mission.creating_update) {
                    mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#00a1ff'>Creating Updating</font></html>");
                    if (mission.id >= 10) mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#00a1ff'>Updating</font></html>");
                }
    
                if (mission.status && mission.updating) {
                    mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#00a1ff'>Updating</font></html>");
                    if (mission.id >= 10) mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#00a1ff'>Updating</font></html>");
                }

                if (mission.shutdown) {
                    mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#fd8080'>Shutdown</font></html>");
                    if (mission.id >= 10) mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#fd8080'>Shutdown</font></html>");
                }

                if (mission.complete) {
                    mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#bdbffa'>Complete</font></html>");
                    if (mission.id >= 10) mission_label = new JLabel("<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#bdbffa'>Complete</font></html>");
                }

                mission_label.setForeground(Color.white);
                mission.mission_label = mission_label;
                dashboard.add(mission_label);
            }
        } else {
            dashboard.add(new JLabel("Too many missions to display."));
            for (var i = 0; i < 6; i++)
                dashboard.add(new JLabel(""));
        }

        active_missions.setForeground(Color.white);
        pending_missions.setForeground(Color.white);
        paused_missions.setForeground(Color.white);
        completed.setForeground(Color.white);
        shutdown.setForeground(Color.white);
        total_reports.setForeground(Color.white);
        delivered_reports.setForeground(Color.white);
    }

    public void updateDashboard(int active_m, int pending_m, int total_r, int delivered_r, int shutdown_missions, int completed_missions) {
        int halted_count = 0;
        for (var i = 0; i < mission_list.size(); i++)
            if (mission_list.get(i).halted && !mission_list.get(i).creating_update && !mission_list.get(i).updating && !mission_list.get(i).complete && !mission_list.get(i).shutdown) 
                halted_count++;
        int active = active_m - halted_count;
        if (active < 0) active = 0;

        active_missions.setText("<html><font color='#bdbffa'>Active Missions: </font>" + active + "</html>");
        pending_missions.setText("<html><font color='#bdbffa'>Pending Missions: </font>" + pending_m + "</html>");
        paused_missions.setText("<html><font color='#bdbffa'>Halted Missions: </font>" + halted_count + "</html>");
        completed.setText("<html><font color='#bdbffa'>Completed Missions: </font>" + completed_missions + "</html>");
        shutdown.setText("<html><font color='#bdbffa'>Shutdown Missions: </font>" + shutdown_missions + "</html>");
        total_reports.setText("<html><font color='#bdbffa'>Sent Reports: </font>" + total_r + "</html>");
        delivered_reports.setText("<html><font color='#bdbffa'>Delivered Reports: </font>" + delivered_r + "</html>");

        for (var i = 0; i < mission_list.size(); i++) {
            Mission mission = mission_list.get(i);
            String mission_id = Integer.toString(mission.id);

            String mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#bdbffa'>Awaiting Start</font></html>";
            if (mission.id >= 10) mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#bdbffa'>Awaiting Start</font></html>";

            if (mission.status && !mission.halted) {
                mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#3fc08b'>Active</font></html>";
                if (mission.id >= 10) mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#3fc08b'>Active</font></html>";
            }

            if (mission.halted && !mission.shutdown) {
                mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#ffc104'>Halted</font></html>";
                if (mission.id >= 10) mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#ffc104'>Halted</font></html>";
            } 
            
            if (mission.status && mission.creating_update) {
                mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#00a1ff'>Creating Updating</font></html>";
                if (mission.id >= 10) mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#00a1ff'>Updating</font></html>";
            }

            if (mission.status && mission.updating) {
                mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#00a1ff'>Updating</font></html>";
                if (mission.id >= 10) mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#00a1ff'>Updating</font></html>";
            }

            if (mission.shutdown) {
                mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#fd8080'>Shutdown</font></html>";
                if (mission.id >= 10) mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#fd8080'>Shutdown</font></html>";
            }

            if (mission.complete) {
                mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp&nbsp <font color='#bdbffa'>Complete</font></html>";
                if (mission.id >= 10) mission_label = "<html>Apollo " + mission_id + ":&nbsp&nbsp&nbsp<font color='#bdbffa'>Complete</font></html>";
            }

            mission.mission_label.setText(mission_label);
        }
    }

    private void loadMission(int id) {
        current_mission = id;
        String duration_value, failure_value, update_value;

        for (var i = 0; i < mission_list.size(); i++) {
            Mission mission = mission_list.get(i);
            if (mission.id == id) {
                String[] stage_data = mission.stages.getCurrentStage();
                duration_value = stage_data[0];
                failure_value = stage_data[1];
                update_value = stage_data[2];

                mission_title.setText("Apollo " + id);
                fuel.setText("<html><font color='#bdbffa'>Fuel: </font>" +  mission.components.fuel + "</html>");
                destination.setText("<html><font color='#bdbffa'>Destination: </font>" + mission.destination + "</html>");
                months.setText("<html><font color='#bdbffa'>Mission Time: </font>" + mission.months_operating + " months</html>");

                if (mission.status && mission.complete) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#3fc08b'>Complete</font></html>");
                if (mission.status && !mission.halted && !mission.shutdown && !mission.complete) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#3fc08b'>Active</font></html>");
                if (mission.status && mission.halted && !mission.shutdown && !mission.complete) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#ffc104'>Halted</font></html>");
                if (mission.status && mission.creating_update) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#00a1ff'>Creating Updating</font></html>");
                if (mission.status && mission.updating) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#00a1ff'>Updating</font></html>");
                if (mission.status && mission.shutdown) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#fd8080'>Shutdown</font></html>");
                if (!mission.status) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#00a1ff'>Awaiting Start</font> (" + mission.mission_start + ")</html>");

                stage.setText("<html><font color='#bdbffa'>Stage:</font> " + mission.stages.current_stage + "</html>");
                duration.setText("<html><font color='#bdbffa'>Stage Duration:</font> " + duration_value + " months</html>");
                failure.setText("<html><font color='#bdbffa'>Stage Failure:</font> " + failure_value + "</html>");
                update.setText("<html><font color='#bdbffa'>Update Available:</font> " + update_value + "</html>");

                thruster_supply.setText("<html><font color='#bdbffa'>Thrusters:</font> " + mission.components.thrusters + " units (" + mission.components.thrusters_rate + " months) </html>");
                instrument_supply.setText("<html><font color='#bdbffa'>Instruments:</font> " + mission.components.instruments + " units (" + mission.components.instruments_rate + " months) </html>");
                control_systems_supply.setText("<html><font color='#bdbffa'>Control Systems:</font> " + mission.components.control_systems + " units (" + mission.components.control_systems_rate + " months) </html>");
                powerplant_supply.setText("<html><font color='#bdbffa'>Powerplants:</font> " + mission.components.powerplants + " units (" + mission.components.powerplants_rate + " months) </html>");

                log.setText("");
                log_queue.setText("");
                log_queue.append("Transmitting Reports\n---\n");
                log.append("Delivered Reports\n---\n");
                for (var j = 0; j < mission.report_log.size(); j++) {
                    Report report = mission.report_log.get(j);
                    if (report.transmitting) {
                        log_queue.append("[" + mission.id + "] " + report.name + " (" + report.size + " bytes | command: " + report.command_response + " | transmission: " +  Integer.toString(report.transmission_time) + " secs)\n");
                    } else {
                        log.append("[" + mission.id + "] " + report.name + " (" + report.size + " bytes | command: " + report.command_response + " | transmission: " +  Integer.toString(report.transmission_time) + " secs)\n");
                    }
                }
            }
        }
    }

    public void updateReportList() {
        for (var i = 0; i < mission_list.size(); i++) {
            Mission mission = mission_list.get(i);
            if (mission.id == current_mission) {
                months.setText("<html><font color='#bdbffa'>Mission Time: </font>" + mission.months_operating + " months</html>");

                if (mission.status && mission.complete) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#3fc08b'>Complete</font></html>");
                if (mission.status && !mission.halted && !mission.shutdown && !mission.complete) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#3fc08b'>Active</font></html>");
                if (mission.status && mission.halted && !mission.shutdown && !mission.complete) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#ffc104'>Halted</font></html>");
                if (mission.status && mission.creating_update) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#00a1ff'>Creating Updating</font></html>");
                if (mission.status && mission.updating) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#00a1ff'>Updating</font></html>");
                if (mission.status && mission.shutdown) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#fd8080'>Shutdown</font></html>");
                if (!mission.status) start_time.setText("<html><font color='#bdbffa'>Status: </font><font color='#00a1ff'>Awaiting Start</font> (" + mission.mission_start + ")</html>");

                String duration_value, failure_value, update_value;
                String[] stage_data = mission.stages.getCurrentStage();
                duration_value = stage_data[0];
                failure_value = stage_data[1];
                update_value = stage_data[2];
                stage.setText("<html><font color='#bdbffa'>Stage:</font> " + mission.stages.current_stage + "</html>");
                duration.setText("<html><font color='#bdbffa'>Stage Duration:</font> " + duration_value + " months</html>");
                failure.setText("<html><font color='#bdbffa'>Stage Failure:</font> " + failure_value + "</html>");
                update.setText("<html><font color='#bdbffa'>Update Available:</font> " + update_value + "</html>");

                log.setText("");
                log_queue.setText("");
                log.append("Delivered Reports\n---\n");
                log_queue.append("Transmitting Reports\n---\n");
                for (var j = 0; j < mission.report_log.size(); j++) {
                    Report report = mission.report_log.get(j);
                    if (report.transmitting) {
                        log_queue.append("[" + mission.id + "] " + report.name + " (" + report.size + " bytes | command: " + report.command_response + " | transmission: " + Integer.toString(report.transmission_time) + " secs)\n");
                    } else {
                        log.append("[" + mission.id + "] " + report.name + " (" + report.size + " bytes | command: " + report.command_response + " | transmission: " +  Integer.toString(report.transmission_time) + " secs)\n");
                    }
                }
            }
        }
    }

    private void createMissionToggles() {
        for (var i = 0; i < mission_list.size(); i++) {
            button_id++;
            JButton button = new JButton("Apollo " + Integer.toString(button_id));
            button.setName(Integer.toString(button_id));
            buttons.add(button);
            button.addActionListener(this);

            button.setForeground(Color.white);
            button.setBackground(green);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 12));

            Border line = new LineBorder(blue);
            Border margin = new EmptyBorder(5, 5, 5, 5);
            Border compound = new CompoundBorder(line, margin);
            button.setBorder(compound);

            mission_control.add(button);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (var i = 0; i < buttons.size(); i++) 
            if (e.getSource() == buttons.get(i))
                loadMission(Integer.parseInt(buttons.get(i).getName()));
    }
}