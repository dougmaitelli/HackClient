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
    
    private FlushTimer flushTimer;
    private String lastReadLine = "";
    private String currentPath;

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
                
                lastReadLine += (char) buffer;
                
                if (flushTimer != null) {
                	flushTimer.interrupt();
                }
                
                if (buffer == '\n') {
                	proccess.getOut().flush();
                	
                	lastReadLine = "";
                } else {
                	flushTimer = new FlushTimer();
                    flushTimer.start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class FlushTimer extends Thread {
    	
    	@Override
    	public void run() {
    		try {
    			Thread.sleep(100);
    		} catch (InterruptedException ex) {
    			return;
    		}
    		
    		proccess.getOut().flush();
    		currentPath = lastReadLine;
    		flushTimer = null;
    	}
    }
    
    public String getCurrentPath() {
		return currentPath;
	}
}
