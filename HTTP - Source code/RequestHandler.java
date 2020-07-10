import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Logger;

public class RequestHandler implements Runnable {

    private final static Logger logger = Logger.getLogger(
            RequestHandler.class.getCanonicalName()); //this puts out a message each time a new request is processed

    private Socket connection;
    private File rootDirectory;

    public RequestHandler(Socket connection, File rootDirectory)
    {
        this.connection=connection;
        if(!rootDirectory.isDirectory())
        {
            throw new IllegalArgumentException("Expected a directory."); //this check is unnecessary, can be removed safely
        }
        this.rootDirectory=rootDirectory;
    }

    private void sendResponseHeader(Writer out, String responseCode, int length, String type) throws IOException {

        //this method sends out the first line of the http response string a.k.a the response header

        out.write(responseCode+"\r\n");
        out.write("Date: "+new Date()+"\r\n");
        out.write("Server: CSE322Server\r\n");
        out.write("Content-length: "+length+"\r\n");
        out.write("Content-type: "+type+"\r\n");
        out.write("\r\n"); //marks the end of the response header
        out.flush();
    }

    @Override
    public void run() {
        try {
            OutputStream base = new BufferedOutputStream(connection.getOutputStream()); //use this to send raw bytes
            Writer out = new OutputStreamWriter(base); //use this to send formatted text

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String methodLine=in.readLine(); //reads the first line of the request

            String[] tokens = methodLine.split("\\s+");
            //uses whitespaces as delimiters for splitting; see Java regex for more info

            String method=tokens[0];
            logger.info(connection.getRemoteSocketAddress() + " " + methodLine);
            String fileName = tokens[1];
            if(fileName.endsWith("/"))
            {
                //client didn't specify a file name
                //add "index.html" to it

                fileName+="index.html";

            }
            if(method.equals("GET"))
            {
                //code for "GET"
                String type=URLConnection.getFileNameMap().getContentTypeFor(fileName);
                File requestedFile = new File(rootDirectory, fileName.substring(1, fileName.length()));
                //the substring() is there to remove the leading "/" from the file name

                if(requestedFile.canRead()&&requestedFile.getCanonicalPath().startsWith(rootDirectory.toString()))
                {
                    byte[] data = Files.readAllBytes(requestedFile.toPath());

                    //send MIME header
                    sendResponseHeader(out, "HTTP/1.0 200 OK", data.length, type);


                    base.write(data);
                    base.flush();
                }
                else
                {
                    //request file either unreadable or does not exist

                    String errorBody=UtilityMethods.createErrorBody("404", "File Not Found");
                    logger.info("404: File Not Found");

                    sendResponseHeader(out, "HTTP/1.0 404 File Not Found", errorBody.length(), "text/html; charset=utf-8");

                    out.write(errorBody);
                    out.flush();
                }
            }
            else {
                if(method.equals("POST"))
                {
                    //code for "POST"

                    StringBuilder request=new StringBuilder();
                    while(in.ready())
                    {
                        request.append((char)(in.read())); //read the entire request in
                    }


                    String[] postTokens = request.toString().split("\\r\\n\\r\\n"); //splitting around the one and only blank line

                    postTokens=postTokens[1].split("=");

                    //now postTokens[1] contains whatever is right to the first "=" sign

                    String body=UtilityMethods.createPostBody(postTokens[1]);

                    sendResponseHeader(out, "HTTP/1.0 200 OK", body.length(), "text/html; charset=utf-8");
                    out.write(body);
                    out.flush();

                }
                else {
                    //code for "unsupported operation"
                    String errorBody=UtilityMethods.createErrorBody("501", "Not Implemented");

                    sendResponseHeader(out, "HTTP/1.0 501 Not Implemented", errorBody.length(), "text/html; charset=utf-8");

                    out.write(errorBody);
                    out.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null)
            {
               try {
                   connection.close();
               } catch (IOException e)
               {
                   e.printStackTrace();
               }
               connection=null;
            }
        }
    }
}
