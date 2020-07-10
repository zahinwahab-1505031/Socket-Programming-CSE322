/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline1_sendingemail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gahab
 */
public class WriteState implements State{
   
           Server server;

    public WriteState(Server server) {
        this.server = server;
    }
 

    public void Handle(String input){
        if(input.contentEquals(".")){
          server.pr.println(input);
          server.setState(new ReadyToDeliverState(server));
         server.request(input);
     
        }
        else{
            server.pr.println(input);
        }
       
        
    }
}
