package org.example;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MultiClient implements Runnable{
    public static ArrayList<MultiClient> multiClientArrayList = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public MultiClient(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = bufferedReader.readLine();

            multiClientArrayList.add(this);
            broadcastMessage(clientUsername + " has joined the group chat");
        } catch (IOException e) {
            tryClose(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                tryClose(socket, bufferedReader, bufferedWriter);
            }
        }
    }



    public void broadcastMessage(String messageToSend)  {
        for (MultiClient multiClient: multiClientArrayList) {
            try {
                if (!multiClient.clientUsername.equals(clientUsername)) {
                    multiClient.bufferedWriter.write(messageToSend);
                    multiClient.bufferedWriter.newLine();
                    multiClient.bufferedWriter.flush();
                }

            }catch (IOException e) {
                tryClose(socket, bufferedReader, bufferedWriter);
            }
        }

    }

    public void displayClientExit(){
        multiClientArrayList.remove(this);
        broadcastMessage(clientUsername + " just left the group chat");
    }

    public void tryClose(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        displayClientExit();
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
    }








