/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author DougM
 */
public class Main {

    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        do {
            try {
                socket = new Socket("127.0.0.1", 5001);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println(InetAddress.getLocalHost().getHostName());

                do {
                    String cmd = in.readLine();

                    ConsoleThread console = new ConsoleThread(Integer.parseInt(cmd));
                    console.start();
                } while (true);
            } catch (IOException ex) {
            }
        } while (true);
    }
}
