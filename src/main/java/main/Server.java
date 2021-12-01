package main;

import ui.UIServer;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
    //Networking in and out streams
    private ObjectOutputStream output;
    private ObjectInputStream input;
    //main.Server side socket where we can receive messages from the client
    private ServerSocket server;
    private Socket connection;
    UIServer uiServer = new UIServer();
    public boolean isStarted;

    public Server(){

    }
    public void startRunning(){
        try{
            server = new ServerSocket(6789, 100);
            while(true){
                try{
                    waitForConnection();
                    setStreams();
                    whileChatting();
                    isStarted = true;
                }
                catch(EOFException eofException){
                    showMessage("\n Server ended connection");
                    isStarted = false;
                }
                finally{
                    closeAll();
                    isStarted = false;
                }
            }
        }
        catch(IOException ioException){
            ioException.printStackTrace();
            isStarted = false;
        }
    }
    //Communicate with end user about connection status
    private void waitForConnection() throws IOException{
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
        while(!message.equals("END CHAT SESSION"));
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
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessage("\nSERVER - " + message);
        }
        catch(IOException ioException){
            uiServer.chatWindow.append("\n ERROR: UNABLE TO SEND MESSAGE");
        }
    }

    //Updates chat window
    private void showMessage(final String text){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        uiServer.chatWindow.append(text);
                    }
                }
        );
    }

    private void ableToType(final boolean tof){
        SwingUtilities.invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        uiServer.userText.setEditable(tof);
                    }
                }
        );
    }
}
