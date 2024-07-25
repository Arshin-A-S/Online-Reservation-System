import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class MainMenu extends JFrame{
    private JButton loginUserButton;
    private JButton exitButton;
    private JButton registerUserButton;
    private JLabel menuLabel;
    private JPanel mainPanel;

    public MainMenu(){
        loginUserButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                new LoginForm(MainMenu.this);
            }
        });

        registerUserButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
                new RegisterUser(MainMenu.this);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        this.setContentPane(this.mainPanel);
        this.setTitle("Online Reservation");
        this.setSize(750,750);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
