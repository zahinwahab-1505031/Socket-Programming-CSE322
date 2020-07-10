import java.io.File;

public class Main {
    public static void main(String[] args){
        CSE322Server server = new CSE322Server(new File(args[0]), Integer.parseInt(args[1]));
        //put the root directory for the server in args[0] and port in args[1]

        server.serverStart();

    }
}
