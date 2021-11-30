import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
    //UI elements that include our client text field and chat window
    private JTextField userText;
    private JTextArea ourWindow;
    //Networking in and out streams
    private ObjectOutputStream output;
    private ObjectInputStream input;
    //Server side socket where we can receive messages from the client
    private ServerSocket server;
    private Socket connection;

    public Server(){
        System.out.println("cya");
    }
}
