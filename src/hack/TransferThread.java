/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DougM
 */
public class TransferThread extends Thread {

    private Socket fileSocket;
    private File file;

    public TransferThread(String fileName) {
        super("Transfer Thread");

        file = new File(fileName);
    }

    @Override
    public void run() {
        if (file.exists()) {
            try {
                fileSocket = new Socket("127.0.0.1", 5002);

                OutputStream output = fileSocket.getOutputStream();

                OutputStreamWriter os = new OutputStreamWriter(fileSocket.getOutputStream());

                os.write(file.getName() + "\n");
                os.flush();

                os.write((int) file.length() + "\n");
                os.flush();

                Md5 hash;

                hash = new Md5(file.getName());

                os.write(hash.getHash() + "\n");
                os.flush();

                FileInputStream fis = new FileInputStream(file);

                byte[] bytes = new byte[fileSocket.getSendBufferSize()];

                int bytesRead = 0;

                while ((bytesRead = fis.read(bytes)) > 0) {
                    output.write(bytes, 0, bytesRead);
                }

                os.flush();
                os.close();
                fis.close();
                fileSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(TransferThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(TransferThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
