import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSE322Server {

    private static int MAX_CLIENTS=10;

    private static final Logger logger = Logger.getLogger(
            CSE322Server.class.getCanonicalName()); //we use this logger to generate logs

    private final File rootDirectory;
    private final int port;

    public CSE322Server(File rootDirectory, int port) {
        this.rootDirectory = rootDirectory;
        if(!rootDirectory.isDirectory()) throw new IllegalArgumentException("Directory expected");
        this.port=port;
    }

    public void serverStart()
    {
        ExecutorService threadPool= Executors.newFixedThreadPool(MAX_CLIENTS);
        //using a threadpool has the added advantage of not having to worry about managing the threads, the system does that for you

        try (ServerSocket server = new ServerSocket(port)) {
            logger.info("Listening on port " + server.getLocalPort()+", Root Directory: "+rootDirectory);
            while(true)
            {
                try{
                    Socket connection = server.accept();
                    Runnable newThread = new RequestHandler(connection, this.rootDirectory);
                    threadPool.submit(newThread);
                }catch(IOException e)
                {
                    e.printStackTrace();
                }

            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
