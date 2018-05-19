package com.centennail.atm.server.models;

import com.centennail.atm.server.models.api.AtmBL;
import com.centennail.atm.shared.models.AtmOperationDTO;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    private final AtmBL atmBL;
    private Socket socket;

    ServerThread(Socket socket, AtmBL atmBL) {
        this.socket = socket;
        this.atmBL = atmBL;
    }

    @Override
    public void run() {
        try {

            System.out.println("Connected!");
            ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Getting accountDTO from client...");


            // get object from client (from stream)
            AtmOperationDTO atmOperationDTOFromClient = (AtmOperationDTO) inFromClient.readObject();

            System.out.println("Got accountDTO: " + atmOperationDTOFromClient);

            double balance = 0;
            // Proccess request from Client

            switch (atmOperationDTOFromClient.getOperation()) {
                case LOGIN:
                    System.out.println("Checking if user: " + atmOperationDTOFromClient.getUserId() + " exists...");
                    boolean isUserExist = atmBL.isUserExist(atmOperationDTOFromClient.getUserId(), atmOperationDTOFromClient.getPin());
                    if (isUserExist) {
                        atmOperationDTOFromClient.setIsUserExist(isUserExist);
                        balance = atmBL.getUserBalance(atmOperationDTOFromClient.getUserId());
                        atmOperationDTOFromClient.setBalance(balance);
                    }
                    break;
                case DEPOSIT:
                    // OR
                case WITHDRAW:
                    System.out.println("Performing <" + atmOperationDTOFromClient.getOperation() + "> ...");
                    balance = atmBL.performATMOperation(atmOperationDTOFromClient.getUserId(), atmOperationDTOFromClient.getOperation(), atmOperationDTOFromClient.getAmount());
                    atmOperationDTOFromClient.setBalance(balance);
                    break;
            }

            System.out.println("Return response to client: " + atmOperationDTOFromClient);
            // serialize and send response to client
            outToClient.writeObject(atmOperationDTOFromClient);

            socket.close();
            // close streams
            inFromClient.close();
            outToClient.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
