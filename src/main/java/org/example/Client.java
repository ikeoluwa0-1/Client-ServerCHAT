package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String username;

    public Client(Socket socket, String username) {
        try{
            this.socket = socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        }
        catch(IOException e) {
            tryClose(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": "+ messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }
        catch (IOException e){
            tryClose(socket, bufferedReader, bufferedWriter);
        }
    }

    public void receiveMessage(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                String messageFromMembers;

                while(socket.isConnected()){
                    try{
                        messageFromMembers = bufferedReader.readLine();
                        System.out.println(messageFromMembers);
                    }
                    catch(IOException e){
                        tryClose(socket, bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void tryClose(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        Scanner scanner= new Scanner(System.in);
        System.out.println("Input your username");
        String username = scanner.nextLine();
        Socket socket = new Socket ("localhost", 6070);
        Client client = new Client(socket, username);
        client.receiveMessage();
        client.sendMessage();
    }
}