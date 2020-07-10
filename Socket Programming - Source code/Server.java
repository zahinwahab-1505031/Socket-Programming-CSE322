/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline1_sendingemail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gahab
 */
public class Server {
    public State state;
     static Socket s = null;
     static BufferedReader br = null;
     static PrintWriter pr = null;
       String mailServer = "webmail.buet.ac.bd";
            InetAddress mailHost;
            InetAddress localHost;
    public Server() throws IOException{
        try {
            this.state = new ClosedState(this);
             mailHost = InetAddress.getByName(mailServer);
             localHost = InetAddress.getLocalHost();
            s = new Socket();
            s.connect(new InetSocketAddress(mailHost, 25), 20000);
            s.setSoTimeout(20000);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pr = new PrintWriter(s.getOutputStream(), true);
        } catch (UnknownHostException ex) {
            
            System.err.println("Cannot find the host");
        }
        catch (SocketTimeoutException ex) {
            
            System.err.println("Cannot connect host within 20s");
        }
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public void connect() throws IOException{
           String ID = br.readLine();
       
        state.Handle(ID);
    }
     public void request(String input) {
       
        state.Handle(input);
    }
}
