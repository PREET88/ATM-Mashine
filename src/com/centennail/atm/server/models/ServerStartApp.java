package com.centennail.atm.server.models;

import com.centennail.atm.server.models.api.AtmBL;
import com.centennail.atm.server.models.data.UserData;
import com.centennail.atm.server.models.data.UsersList;
import com.centennail.atm.shared.ServerInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStartApp {
    public static void main(String[] args) {

        UsersList userDataList = new UsersList();
        userDataList.add(new UserData("artem", 1234, 500));
        userDataList.add(new UserData("costa", 1234, 1000));
       userDataList.add(new UserData("roma",  1234, 1500));
        
        AtmBL atmBL = new AtmBL(userDataList);

        int port = ServerInfo.ServerPort;

        try {
            ServerSocket listener = new ServerSocket(port);
            Socket socket;
            while (true) {
                socket = listener.accept();
                ServerThread serverThread = new ServerThread(socket, atmBL);
                Thread t = new Thread(serverThread);
                t.start();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
