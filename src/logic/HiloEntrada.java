/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import classes.MessageType;
import classes.User;
import classes.UserInfo;
import exceptions.ConnectException;
import exceptions.SignInException;
import exceptions.SignUpException;
import exceptions.UpdateException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static logic.DaoFactory.getDao;

/**
 *
 * @author 2dam
 */
public class HiloEntrada extends Thread {

    Socket so;

    public HiloEntrada(Socket so) {
        this.so = so;

    }

    @Override
    public void run() {
        UserInfo userResponse = new UserInfo();
        ObjectOutputStream out = null;
        try {
            UserInfo userInfo;
            User user = null;
            ObjectInputStream in = new ObjectInputStream(so.getInputStream());//recibir mensajes
            out = new ObjectOutputStream(so.getOutputStream());

            userInfo = (UserInfo) in.readObject();

            if (userInfo.getMessage() == MessageType.SIGNIN_REQUEST) {
                user = getDao().signIn(userInfo.getUser());
                userResponse.setMessage(MessageType.SIGNIN_OK);

            }
            if (userInfo.getMessage() == MessageType.SIGNUP_REQUEST) {
                user = getDao().signUp(userInfo.getUser());
                userResponse.setMessage(MessageType.SIGNUP_OK);

            }
            userResponse.setUser(user);

        } catch (IOException ex) {
            Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConnectException ex) {
            userResponse.setMessage(MessageType.CONNECT_EXCEPTION);
            Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignInException ex) {
            userResponse.setMessage(MessageType.SIGNIN_EXCEPTION);
            Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateException ex) {
            userResponse.setMessage(MessageType.UPDATE_EXCEPTION);
            Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignUpException ex) {
            userResponse.setMessage(MessageType.SIGNUP_EXCEPTION);
            Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            int disconnect = 1;
            App clientDisconnected = new App(disconnect);
            try {
                out.writeObject(userResponse);
            } catch (IOException ex) {
                Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
