package caro.view;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Notification extends JFrame {

    private JPanel contentPane;
    private JLabel lblTitle;
    private JTextArea view;

    /**
     * Create the frame.
     */
    public Notification() {
        setTitle("Thông báo");
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage("file\\img\\icon3.png"));
        setBounds(100, 100, 517, 397);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(31, 31, 51));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblTitle = new JLabel("Alert");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(245, 245, 220));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(10, 11, 491, 35);
        contentPane.add(lblTitle);

        view = new JTextArea();
        view.setMargin(new Insets(5, 5, 5, 5));
        view.setEditable(false);
        view.setForeground(new Color(245, 245, 220));
        view.setBackground(new Color(31, 31, 51));
        view.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBounds(10, 57, 491, 300);
        contentPane.add(scrollPane);
    }

    public void show(String title, String notificationTitle, String info) {
        setTitle(title);
        lblTitle.setText(notificationTitle);
        view.setText(info);
        view.setCaretPosition(0);
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
