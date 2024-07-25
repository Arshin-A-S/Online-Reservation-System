import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ShowReservations extends JFrame{
    private JPanel showReservationsPanel;
    private JButton closeButton;
    private JTextArea textArea1;

    public ShowReservations(UserMenu um, int userId){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_reservation_system", "root", "1234");
            String getReservationsQuery = "select pnr,name,train_no,travel_date,from_city,to_city from reservation where user_id=?";
            PreparedStatement getReservations = con.prepareStatement(getReservationsQuery);
            getReservations.setInt(1, userId);
            ResultSet reservations = getReservations.executeQuery();
            StringBuilder addText = new StringBuilder();

            if(!reservations.isBeforeFirst()){
                addText.append("No reservations found!");
            }else{
                int count = 1;
                while(reservations.next()){
                    addText.append(count).append(") PNR:").append(reservations.getInt("pnr"))
                            .append(", Train No.: ").append(reservations.getInt("train_no"))
                            .append(", Passenger Name:").append(reservations.getString("name"))
                            .append(", Date:").append(reservations.getString("travel_date"))
                            .append(", FROM:").append(reservations.getString("from_city"))
                            .append(", TO:").append(reservations.getString("to_city")).append("\n");
                    count++;
                }
            }
            textArea1.append(new String(addText));
        } catch(Exception SqlConnE){
            SqlConnE.printStackTrace();
            JOptionPane.showMessageDialog(ShowReservations.this, "Error "+SqlConnE.getMessage(), "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                um.setVisible(true);
            }
        });
        this.textArea1.setEditable(false);
        this.setContentPane(this.showReservationsPanel);
        this.setSize(750,750);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
