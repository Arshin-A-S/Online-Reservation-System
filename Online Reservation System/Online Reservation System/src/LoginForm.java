import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JFrame{
    private JPanel loginPanel;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;


    public LoginForm(MainMenu mm){

        //Database Connection
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_reservation_system", "root", "1234");

            loginButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    try {
                        String username = idField.getText().strip();
                        String pass = new String(passwordField.getPassword()).strip();
                        int userId;
                        String checkPass;
                        String getPassQuery = "select passwd,user_id from users where username=?;";
                        PreparedStatement passQueryStmt= con.prepareStatement(getPassQuery);
                        passQueryStmt.setString(1, username);
                        ResultSet rs = passQueryStmt.executeQuery();
                        if(!rs.next()){
                            JOptionPane.showMessageDialog(LoginForm.this, "Error: Enter the correct username", "Unknown Username", JOptionPane.ERROR_MESSAGE);
                        }else{
                            checkPass = rs.getString("passwd");
                            userId = rs.getInt("user_id");
                            if (checkPass.equals(pass)) {
                                dispose();
                                setVisible(false); //TEST THIS FEATURE
                                new UserMenu(mm, userId, username);
                            }else{
                                JOptionPane.showMessageDialog(LoginForm.this, "Error: Enter the correct password", "Incorrect Password", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }catch(SQLException sqlE){
                        sqlE.printStackTrace();
                        JOptionPane.showMessageDialog(LoginForm.this, "Error "+sqlE.getMessage(), "Database Query Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(LoginForm.this, "Error "+e.getMessage(), "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                mm.setVisible(true);
            }
        });

        this.setContentPane(this.loginPanel);
        this.setSize(750,750);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
