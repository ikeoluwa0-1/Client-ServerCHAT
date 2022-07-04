package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.example.*;

public class Server {

        private ServerSocket serverSocket;
        public Server(ServerSocket serverSocket){
            this.serverSocket = serverSocket;
        }

        public void startServer(){
            try{
                while(!serverSocket.isClosed()){
                    Socket socket= serverSocket.accept();
                    System.out.println("A new username just joined the group");
                    MultiClient multiClient = new MultiClient(socket);

                    Thread thread = new Thread(multiClient);
                    thread.start();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        public static void main(String[] args) throws IOException {
            ServerSocket serverSocket = new ServerSocket(6070);
            Server server = new Server(serverSocket);
            server.startServer();
        }


}
