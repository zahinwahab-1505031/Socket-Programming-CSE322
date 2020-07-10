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
public class ClosedState implements State {
    Server server;

    public ClosedState(Server server) {
        this.server = server;
    }
    
    public void Handle(String input){
         System.out.println(input);
      
         if(input.startsWith("2")){
             State st = new BeginState(server);
             server.setState(st);}
         else {
             server.setState(new ClosedState(server));
             try {
                 server.connect();
             } catch (SocketTimeoutException ex) {
                 System.err.println(ex);
            }catch (IOException ex) {
                 System.err.println(ex);
            }
         }
    }
}
