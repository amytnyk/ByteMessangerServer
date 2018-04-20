package alexm.bytemessanger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            byte[] bytes = null;
            input.read(bytes);
            String response = null;
            Utils utils = new Utils();
            String[] creds = bytes.toString().substring(1).split("\\:");
            if (bytes.toString().charAt(0) == 'l') {
            	// Login
                String login = creds[0];
                String password = creds[1];
                response = utils.getKey(login, password);
            }
            else if (bytes.toString().charAt(0) == 'r') {
            	// Register 
            	String login = creds[0];
                String password = creds[1];
                response = utils.createAccount(login, password);
            }
            else {
            	// Unknown
            	response = "UN";
            }
            
            output.write(response.getBytes());
            output.close();
            input.close();
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}
