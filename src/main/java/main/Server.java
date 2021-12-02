package main;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.AncestorListener;

public class Server extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public Server(){
        super("Instant Messaging Server");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sendMessage(e.getActionCommand());
                        userText.setText("");
                    }
                }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(600, 600);
        setVisible(true);
    }
    public void startRunning(){
        try{
            server = new ServerSocket(6788, 100);
            while(true){
                try{
                    waitForConnection();
                    setStreams();
                    whileChatting();
                }
                catch(EOFException eofException){
                    showMessage("\n Server ended connection");
                }
                finally{
                    closeAll();
                }
            }
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
    //Communicate with end user about connection status
    public void waitForConnection() throws IOException{
        showMessage("Waiting for someone to connect...\n");
        connection = server.accept();
        showMessage("Now connected to " + connection.getInetAddress().getHostName());
    }
    //Set up networking streams
    private void setStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nStreams are now set up\n");
    }

    private void whileChatting() throws IOException{
        String message = "You are now connected";
        sendMessage(message);
        ableToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }
            catch(ClassNotFoundException classNotFoundException){
                showMessage("\nUndecipherable message\n");
            }
        }
        while(!message.equals("SERVER - END"));
    }

    private void closeAll(){
        showMessage("\n Closing connections...\n");
        ableToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void sendMessage(String message){
        try{
            output.writeObject("\nSERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
        }
        catch(IOException ioException){
            chatWindow.append("\n ERROR: UNABLE TO SEND MESSAGE");
        }
    }

    //Updates chat window
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        chatWindow.append(text);
                    }
                }
        );
    }

    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        userText.setEditable(tof);
                    }
                }
        );
    }
}
