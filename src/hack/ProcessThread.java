/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hack;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DougM
 */
public class ProcessThread extends Thread {

    private ConsoleThread proccess;
    private BufferedReader proccessReader;

    public ProcessThread(ConsoleThread proc, BufferedReader reader) {
        super("Process Thread");

        proccess = proc;
        proccessReader = reader;
    }

    @Override
    public void run() {
        try {
            int buffer;

            while ((buffer = proccessReader.read()) != -1) {
                proccess.getOut().write(buffer);
                proccess.getOut().flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
