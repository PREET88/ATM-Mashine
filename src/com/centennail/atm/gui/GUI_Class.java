package com.centennail.atm.gui;


import com.centennail.atm.shared.ServerInfo;
import com.centennail.atm.shared.enums.Operation;
import com.centennail.atm.shared.models.AtmOperationDTO;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class GUI_Class extends BorderPane implements ActionListener {

    Label lblCustomerId = new Label("Customer ID");
    Label lblPassword = new Label("Pin Number");
    Label lblAmount = new Label("Amount");
    TextArea textArea = new TextArea("Result...");
    Button btnLogin = new Button("Login");
    Button btnWithdraw = new Button("Withdraw");
    Button btnDeposit = new Button("Deposit");
    Button btnLogOff = new Button("Log Off");

    private String userID = "";
    private int pin = 0;
    private double amount = 0;

    private void setValuesFromControls(){
         userID = tbCustomerId.getText();
         pin = Integer.parseInt(tbPassword.getText());
         amount = Double.parseDouble(tbAmount.getText().isEmpty() ? "0" : tbAmount.getText());

    }

    TextField tbCustomerId = new TextField();
    PasswordField  tbPassword = new PasswordField ();
    TextField tbAmount = new TextField();

    public GUI_Class() {

        setCenter(GUIPane_Class());
        try {
            onLoad();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Pane GUIPane_Class() {
        // this.dataAccess = dataAccess;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 20, 20, 20));

        /* - Declare Controls - */


        /* Set Controls inside GridPane */
        grid.setConstraints(lblCustomerId, 0, 0);
        grid.setConstraints(lblPassword, 0, 1);
        grid.setConstraints(lblAmount, 0, 2);
        //  grid.setConstraints(postalCode,0,3);

        grid.setConstraints(tbCustomerId, 1, 0);
        grid.setConstraints(tbPassword, 1, 1);
        grid.setConstraints(tbAmount, 1, 2);
        // grid.setConstraints(tbPostalCode,1,3);

        grid.setConstraints(btnLogin, 2, 0);
        grid.setConstraints(btnWithdraw, 2, 1);
        grid.setConstraints(btnDeposit, 2, 2);
        grid.setConstraints(btnLogOff,3,2);


        // grid.setConstraints(listView,4,0,1,4);
        grid.setConstraints(textArea, 0, 4, 5, 2);

        btnWithdraw.setDisable(true);
        btnDeposit.setDisable(true);
        btnLogOff.setDisable(true);


        btnLogin.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                boolean validFields = true;

                setValuesFromControls();

            if (userID.isEmpty() || pin == 0)
            {
                JOptionPane.showMessageDialog(null, "Pease enter your Id and Pin!");
                validFields = false;

            }else {

                try {// if PIN is number
                    Integer.parseInt(tbPassword.getText());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "PIN must include numbers only !");
                    tbPassword.setText("");
                    validFields = false;

                }
            }


            if (validFields){
                //????


                AtmOperationDTO atmOperationDTOFromClient = new AtmOperationDTO(Operation.LOGIN, userID, pin);

                //copy

                AtmOperationDTO atmOperationDTOFromServer = MakeServerRequest(atmOperationDTOFromClient);
                // save is user exist
                // display user id and pin fields
                //JOptionPane.showMessageDialog(null, responseAccountDTO);

                if (atmOperationDTOFromServer.isUserExist()) {
                    btnWithdraw.setDisable(false);
                    btnDeposit.setDisable(false);
                    btnLogin.setDisable(true);
                    btnLogOff.setDisable(false);


                    textArea.setText("Welcome " + atmOperationDTOFromServer.getUserId() + "!\nCurrent bulance is: " + atmOperationDTOFromServer.getBalance() + "$\n");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Pin!");

                }
            }
            }
        });

        btnWithdraw.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {

                setValuesFromControls();
                if(Integer.parseInt( tbAmount.getText())> 0) {
                    // if userExist (from LOGIN
                    AtmOperationDTO requestAtmOperationDTO = new AtmOperationDTO(Operation.WITHDRAW, userID, pin, amount);
                    AtmOperationDTO responseAtmOperationDTO = MakeServerRequest(requestAtmOperationDTO);
                   // JOptionPane.showMessageDialog(null, responseAtmOperationDTO);

                    textArea.appendText("New balance is: " + responseAtmOperationDTO.getBalance() + ".$\n");
                    tbAmount.setText("");
                }else {
                    JOptionPane.showMessageDialog(null, "Please Enter Abount!");

                }
            }
        });

        btnDeposit.setOnAction(new EventHandler<javafx.event.ActionEvent>() {


            @Override
            public void handle(javafx.event.ActionEvent event) {
                setValuesFromControls();
                // if userExist (from LOGIN)
                if(Integer.parseInt( tbAmount.getText())> 0) {
                AtmOperationDTO requestAtmOperationDTO = new AtmOperationDTO(Operation.DEPOSIT, userID, pin, amount);
                AtmOperationDTO responseAtmOperationDTO = MakeServerRequest(requestAtmOperationDTO);
               // JOptionPane.showMessageDialog(null, responseAtmOperationDTO);
                textArea.appendText("New balance is: "+ responseAtmOperationDTO.getBalance()+".$\n");
                tbAmount.setText("");
            }else {
                JOptionPane.showMessageDialog(null, "Please Enter Abount!");

            }}
        });

        btnLogOff.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
                                  @Override
                                  public void handle(javafx.event.ActionEvent event) {
                                      textArea.setText("Please Login to the system..");
                                      btnWithdraw.setDisable(true);
                                      btnDeposit.setDisable(true);
                                      btnLogin.setDisable(false);
                                      btnLogOff.setDisable(true);
                                  }
                              });

                grid.getChildren().addAll(lblCustomerId, lblPassword, lblAmount, btnLogin, btnWithdraw, textArea, tbCustomerId, tbPassword, tbAmount, btnDeposit, btnLogOff);

        return grid;
    }

    // Build request via socket to SERVER
    private AtmOperationDTO MakeServerRequest(AtmOperationDTO atmOperationDTOFromClient) {
        AtmOperationDTO atmOperationDTOFromServer = null;
        try {
            Socket socket = new Socket();
            socket = new Socket(ServerInfo.ServerIP, ServerInfo.ServerPort);
            // Output stream first to set a pointer to the beginning of the userDTO object for server to read
            ObjectOutputStream outToServer = new ObjectOutputStream(socket.getOutputStream());
            // set input stream to read the response from the server
            ObjectInputStream inFromServer = new ObjectInputStream(socket.getInputStream());
            // write to output stream to server to read
            outToServer.writeObject(atmOperationDTOFromClient);

            // get response from server and cast
            atmOperationDTOFromServer = (AtmOperationDTO) inFromServer.readObject();
            outToServer.close();
            inFromServer.close();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return atmOperationDTOFromServer;
    }

    public void onLoad() throws Exception {


    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {

    }
}



