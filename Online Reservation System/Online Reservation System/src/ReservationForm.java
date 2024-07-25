import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Random;

public class ReservationForm extends JFrame{
    private JLabel reserveLabel;
    private JTextField nameField;
    private JTextField ageField;
    private JTextField dobField;
    private JTextField phoneField;
    private JTextField trainNumField;
    private JTextField trainNameField;
    private JTextField dateField;
    private JTextField fromField;
    private JTextField toField;
    private JTextField classField;
    private JTextField seatField;
    private JButton reserveButton;
    private JButton cancelButton;
    private JPanel reservePanel;
    private static int num = 1;
    static boolean trainFound = false;
    public ReservationForm(UserMenu um, int id){
        Random rand = new Random();
        this.trainNameField.setEditable(false);
        this.fromField.setEditable(false);
        this.toField.setEditable(false);

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_reservation_system", "root", "1234");

            trainNumField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    try {
                        int trainNum = Integer.parseInt(trainNumField.getText().strip());
                        String getTrainNameQuery = "select train_name, from_city, to_city from train where train_no=?";
                        PreparedStatement getTrainName = con.prepareStatement(getTrainNameQuery);
                        getTrainName.setInt(1, trainNum);
                        ResultSet trainNameResult = getTrainName.executeQuery();

                        if (trainNameResult.next()) {
                            trainNameField.setText(trainNameResult.getString("train_name"));
                            fromField.setText(trainNameResult.getString("from_city"));
                            toField.setText(trainNameResult.getString("to_city"));
                            ReservationForm.trainFound=true;
                        } else {
                            trainNameField.setText("");
                            fromField.setText("");
                            toField.setText("");
                        }
                    } catch (SQLException | NumberFormatException ex) {
                        trainNameField.setText("");
                        fromField.setText("");
                        toField.setText("");
                        ReservationForm.trainFound=false;
                    }
                }
            });
            reserveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        String name = nameField.getText().strip();
                        int age = Integer.parseInt(ageField.getText().strip());
                        String dob = dobField.getText().strip();
                        String phone = phoneField.getText().strip();
                        int trainNum = Integer.parseInt(trainNumField.getText().strip());
                        String date = dateField.getText().strip();
                        String from = fromField.getText().strip();
                        String to = toField.getText().strip();
                        String trainClass = classField.getText().strip();
                        int seat = Integer.parseInt(seatField.getText().strip());
                        String trainName = trainNameField.getText().strip();



                        if(!trainFound){
                            JOptionPane.showMessageDialog(ReservationForm.this, "Error: No trains found for the number! Enter correct train number", "Database Query Error", JOptionPane.ERROR_MESSAGE);
                        } else{
                            String getSeatQuery = "select reserved from seat where train_no=? and class=? and seat_no=?";
                            PreparedStatement getSeat = con.prepareStatement(getSeatQuery);
                            getSeat.setInt(1, trainNum);
                            getSeat.setString(2,trainClass);
                            getSeat.setInt(3, seat);
                            ResultSet seatResult = getSeat.executeQuery();
                            String seatReserved;
                            if(!seatResult.next()){
                                JOptionPane.showMessageDialog(ReservationForm.this, "Error: No data found for the seat number! Enter correct seat number", "Unknown Seat Number", JOptionPane.ERROR_MESSAGE);
                            } else{
//                                trainName=trainNameResult.getString("train_name");

                                //Generate PNR
                                int pnr = (ReservationForm.num++)+rand.nextInt(1000,9999);
                                //Reserve Query
                                String insertRecordsQuery = "insert into reservation values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                PreparedStatement reserve = con.prepareStatement(insertRecordsQuery);
                                reserve.setInt(1, pnr);
                                reserve.setInt(2, id);
                                reserve.setInt(3, trainNum);
                                reserve.setString(4, trainName);
                                reserve.setString(5,name);
                                reserve.setInt(6,age);
                                reserve.setString(7,dob);
                                reserve.setString(8,phone);
                                reserve.setString(9,date);
                                reserve.setString(10,from);
                                reserve.setString(11,to);
                                reserve.setString(12,trainClass);
                                reserve.setInt(13, seat);

                                int reserved = reserve.executeUpdate();
                                if(reserved>0){
                                    dispose();
                                    ReservationForm.trainFound=false;
                                    JOptionPane.showMessageDialog(ReservationForm.this, "Reservation Successful!", "Reservation", JOptionPane.INFORMATION_MESSAGE);
                                    um.setVisible(true);
                                } else{
                                    JOptionPane.showMessageDialog(ReservationForm.this, "Reservation Failed!\n Try Again!", "Reservation", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    } catch(SQLException sqlE){
                        sqlE.printStackTrace();
                        JOptionPane.showMessageDialog(ReservationForm.this, "Error: "+sqlE.getMessage(), "Database Query Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch(Exception sqlConnE){
            sqlConnE.printStackTrace();
            JOptionPane.showMessageDialog(ReservationForm.this, "Error "+sqlConnE.getMessage(), "Database Query Error", JOptionPane.ERROR_MESSAGE);
        }

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                um.setVisible(true);
            }
        });
        this.setContentPane(this.reservePanel);
        this.setSize(750,750);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
