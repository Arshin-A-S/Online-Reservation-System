import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserMenu extends JFrame{
    private JButton reserveButton;
    private JButton cancelReservationButton;
    private JButton exitButton;
    private JButton logOutButton;
    private JPanel userMenuPanel;
    private JLabel welcomeLabel;
    private JButton showReservationsButton;

    public UserMenu(MainMenu mm, int id, String name){

        this.welcomeLabel.setText("Welcome "+name);

        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ReservationForm(UserMenu.this, id);
            }
        });

        showReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ShowReservations(UserMenu.this, id);
            }
        });

        cancelReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CancelReservation(UserMenu.this);
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                mm.setVisible(true);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.setContentPane(this.userMenuPanel);
        this.setSize(750,750);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
