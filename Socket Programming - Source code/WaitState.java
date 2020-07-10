/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline1_sendingemail;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gahab
 */
public class WaitState implements State {
       Server server;

    public WaitState(Server server) {
        this.server = server;
    }
    public void Handle(String input){
        if(input.startsWith("MAIL FROM:<")){
            try {
                server.pr.println(input);
                String w1 = server.br.readLine();
                System.out.println(w1);
                if(w1.startsWith("2")){
                    server.setState(new NoRecipientState(server));
                }
                else {
                    System.err.println("Cannot add sender");
                    server.setState(this);
                }
            }catch (SocketTimeoutException ex) {
                System.err.println(ex);   
            } catch (IOException ex) {
                System.err.println(ex);   
            }
            
        }
     
        else  {
            System.err.println("Cannot find sender/recipient");
            server.setState(this);
        }
    }
}
