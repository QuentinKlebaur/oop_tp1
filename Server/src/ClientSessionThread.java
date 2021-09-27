import java.io.*;
import java.net.*;

class ClientSessionThread extends Thread {
    public ClientSessionThread(ApplicationServeur server, Socket socket) {
        _server = server;
        _socket = socket;
    }

    private ApplicationServeur _server;
    private Socket _socket;

    public void run() {
        ObjectInputStream inStream = null;
        DataOutputStream outStream = null;

        try {
            inStream = new ObjectInputStream(_socket.getInputStream());
            outStream = new DataOutputStream(_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                Commande command = null;

                try {
                    command = (Commande) inStream.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (EOFException e) {
                    command = null;
                }

                if (command == null) {
                    _socket.close();
                    _server.addLog(_socket, "Client quit");
                    return;
                } else {
                    _server.addLog(_socket, command);
                    // DO something

                    String response = "OK";
                    
                    try {
                        outStream.writeBytes(response + "\n");
                        outStream.flush();
                        _server.addLog(_socket, "Response: \"" + response + "\"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}