/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline1_sendingemail;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gahab
 */
public class BeginState implements State {
      Server server;

    public BeginState(Server server) {
        this.server = server;
    }

    public void Handle(String input){
        System.out.println(input);
        if(input.startsWith("HELO")){
            server.pr.println(input);
            try {
                  //here 
                  String st = server.br.readLine();
                System.out.println(st);
             
        /*    server.pr.println("AUTH LOGIN") ;
            System.out.println(server.br.readLine()) ;
            server.pr.println(new String(Base64.getEncoder().encode("spondon".getBytes())));
            System.out.println(server.br.readLine()) ;
            server.pr.println(new String(Base64.getEncoder().encode("Spondon77".getBytes())));
      //
      String reply = server.br.readLine();
            System.out.println(reply) ;*/
            // not required for webmail.buet.ac.bd
                if(st.startsWith("2")){
                    server.setState(new WaitState(server));
                }
                
            } catch(SocketTimeoutException ex){
                System.err.println(ex);
            } 
            catch (IOException ex) {
                System.err.println("Cannot read from input stream");
            }
        }
        else server.setState(this);
    }
}
