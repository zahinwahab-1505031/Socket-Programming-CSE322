/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline1_sendingemail;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gahab
 */
public class ReadyToDeliverState implements State{
           Server server;

    public ReadyToDeliverState(Server server) {
        this.server = server;
    }
    public void Handle(String input){
   try {
       String reply = server.br.readLine();
       System.out.println(reply);
                   if( reply.startsWith("2")) {
                       server.setState(new WaitState(server));
                   }         } catch (IOException ex) {
                  System.err.println("Cannot read from input stream");}
        
    }
}
