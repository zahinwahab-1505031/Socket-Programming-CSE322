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
public class RecipientState implements State{
           Server server;

    public RecipientState(Server server) {
        this.server = server;
    }
    public void Handle(String input){
        if(input.contentEquals("DATA")){
            server.pr.println(input);
            try {
                System.out.println(server.br.readLine());
                server.setState(new WriteState(server));
            } catch (IOException ex) {
                System.err.println("Cannot read/write");  }
        }
        else if(input.startsWith("RCPT TO:<")){
              try {
                server.pr.println(input);
                String w1 = server.br.readLine();
                System.out.println(w1);
                if(w1.startsWith("2")){
                    server.setState(new RecipientState(server));
                }
                else server.setState(this);
            } catch (IOException ex) {
                 System.err.println("Cannot read/write");
            }
        }
        else  if(input.contentEquals("RSET")){
            System.out.println("Connection reset: entering to wait state");
            server.setState(new WaitState(server));
        }
        
    }
}
