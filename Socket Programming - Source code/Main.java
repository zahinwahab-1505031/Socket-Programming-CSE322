/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline1_sendingemail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gahab
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Scanner sc = new Scanner(System.in);
        File file = new File("ListofSenderReceivers.txt");
        int flag = 0;
        int withAttach = 0;
        File file1 = new File("Mail.txt");

        File file2 = new File("list.txt");
        BufferedReader br2 = new BufferedReader(new FileReader(file2));
        BufferedReader br1 = new BufferedReader(new FileReader(file1));
        String st;
        String st1;
        int firstEmpty = 1;
        if (br2.readLine() != null) {
            withAttach = 1;
            int cnt = 0;
            st = br1.readLine();
            String msg = "";
            br2 = new BufferedReader(new FileReader(file2));
            String filename = br2.readLine();
            String []names = new String[100];
            names[0] = filename;
            String []img_code = new String[100];
            int i=0;
            
           while(filename!=null){
            File attFile = new File(filename);
            byte[] encoded = Files.readAllBytes(attFile.toPath());
            img_code[i] = Base64.getMimeEncoder().encodeToString(encoded);
            i++;
            
            filename = br2.readLine();
            names[i] = filename;
           }
            
            while (st != null) {

                if (st.isEmpty() && firstEmpty == 1) {
                    firstEmpty = 0;
                    msg += "MIME-Version: 1.0\n"
                            + "Content-type: multipart/mixed; boundary=\"simple boundary\"\n"
                            + "\n"
                            + "This is the preamble.  It is to be ignored, though it is a handy place for mail composers to include an explanatory note to non-MIME compliant readers.\n"
                            + "--simple boundary\n";

                } else if (!st.equals(".")) {
                    msg += (st + "\n");
                } else if (st.equals(".")) {
                    int count = 0;
                    while(count<i){
                            msg += "--simple boundary\n"
                           + "Content-Type: image/gif\n"
                            + "Content-Transfer-Encoding: Base64\n"
                            + "Content-Disposition: attachment; filename="+names[count]+"\n"
                            + img_code[count] + "\n";
                        count++;
                    }
                
                            msg+= "--simple boundary--\n"
                            + ".";
                }
                st = br1.readLine();

            }
            Writer writer;
            try {
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream("filename.txt"), "utf-8"));
                writer.write(msg);
                writer.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
            file1 = new File("filename.txt");
        }
            try {
                Server server = new Server();
                server.connect();
                System.out.println("Connection done: Please continue.....");
                while (true) {
                    String input = null;
                    if (flag == 0) {
                        input = sc.nextLine();
                    }
                    if (input.equals("MAIL")) {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        st = br.readLine();
                        System.out.println(st);
                        System.out.println("MAIL FROM:<" + st + ">");
                        server.request("MAIL FROM:<" + st + ">");
                        
                    } else if (input.equals("RCPT")) {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        br.readLine();
                        while ((st = br.readLine()) != null) {

                            System.out.println("RCPT TO:<" + st + ">");
                            server.request("RCPT TO:<" + st + ">");

                        }
                    } else if (input.equals("DATA")) {
                        server.request(input);
                        flag = 1;

                    } else if (input.equals("QUIT")) {
                         server.pr.println(input);
              String w1;
            try {
                w1 = server.br.readLine();
                  System.out.println(w1);
               if(w1.startsWith("2")){
                 try {
                     server.s.close();
                     server.pr.close();
                     server.br.close();
                      break;
                 } catch (IOException ex) {
                     System.out.println(ex);
                 }
                }
                else {
                    System.err.println("Cannot quit");
                    continue;
                }
            } catch (SocketTimeoutException ex) {
                System.err.println(ex);   
            } catch (IOException ex) {
                System.err.println(ex);   
            }
                     

                    }else {
                        server.request(input);
                    }
                    if (flag == 1) {
                        br1 = new BufferedReader(new FileReader(file1));
                        while ((st1 = br1.readLine()) != null) {
                            server.request(st1);
                        }
                        flag = 0;
                    }
                }
            } catch (IOException ex) {
                System.err.println("Cannot connect to the server......"+ex);
            }

        }
    }
