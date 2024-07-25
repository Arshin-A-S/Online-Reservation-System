import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CancelReservation extends JFrame{
    private JTextField textField1;
    private JButton backButton;
    private JButton confirmCancellationButton;
    private JPanel cancelReservationPanel;


    public CancelReservation(UserMenu um){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_reservation_system", "root", "1234");
            confirmCancellationButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    try{
                        String pnr = textField1.getText().strip();
                        String checkPnrQuery = "select pnr from reservation where pnr=?";
                        String deleteReservationQuery ="delete from reservation where pnr=?";
                        PreparedStatement checkPnr = con.prepareStatement(checkPnrQuery);
                        PreparedStatement deleteReservation = con.prepareStatement(deleteReservationQuery);
                        checkPnr.setString(1,pnr);
                        deleteReservation.setString(1, pnr);
                        ResultSet getCheckPnrQuery = checkPnr.executeQuery();
                        //Check if the pnr exist
                        if(getCheckPnrQuery.next()){
                            int recordDeleted = deleteReservation.executeUpdate();
                            if(recordDeleted>0){
                                dispose();
                                JOptionPane.showMessageDialog(CancelReservation.this, "Reservation Cancellation Successful!", "Reservation Cancellation", JOptionPane.INFORMATION_MESSAGE);
                                um.setVisible(true);
                            } else{
                                dispose();
                                JOptionPane.showMessageDialog(CancelReservation.this, "Reservation Cancellation FAILED!", "Reservation Cancellation", JOptionPane.ERROR_MESSAGE);
                                um.setVisible(true);
                            }
                        } else{
                            JOptionPane.showMessageDialog(CancelReservation.this, "RESERVATION WITH THE PNR NUMBER NOT FOUND!", "Reservation Cancellation", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch(SQLException sqlE){
                        sqlE.printStackTrace();
                        JOptionPane.showMessageDialog(CancelReservation.this, "Error: "+sqlE.getMessage(), "Database Query Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });


        } catch(Exception sqlConnE){
            sqlConnE.printStackTrace();
            JOptionPane.showMessageDialog(CancelReservation.this, "Error "+sqlConnE.getMessage(), "Database Query Error", JOptionPane.ERROR_MESSAGE);
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                um.setVisible(true);
            }
        });
        this.setContentPane(this.cancelReservationPanel);
        this.setSize(750,750);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
