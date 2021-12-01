package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIServer extends JFrame{

    JFrame mainFrame = new JFrame("Ancient Instant Message");
    JPanel canvas = new JPanel();
    public JTextField userText;
    public JTextArea ourWindow;
    public JTextArea chatWindow = new JTextArea();

    public UIServer(){
        canvas.setBackground(Color.cyan);
        canvas.setLayout(new BorderLayout());
        addElements();
        mainFrame.setSize(300, 300);
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainFrame.add(canvas);
        mainFrame.setVisible(true);
    }

    public void addElements(){
        //User text field for sending messages
        userText = new JTextField();
        userText.setSize(300, 300);
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        //sendMessage(event.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        //Adding to canvas
        canvas.add(userText, BorderLayout.NORTH);
        canvas.add(new JScrollPane(chatWindow));
        chatWindow.setSize(300, 150);

    }
}
