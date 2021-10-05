import java.io.*;
import java.net.*;

public class ClientSessionThread extends Thread {
    /**
    * Créer une session pour un client en prenant un socket et la classe du serveur
    */
    public ClientSessionThread(ApplicationServeur server, Socket socket) {
        _server = server;
        _socket = socket;
    }

    private ApplicationServeur _server;
    private Socket _socket;

    /**
    * Lance le Thread et s'occupe d'interagir avec un client
    */
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
        _server.addClientResponse();

        while (true) {
            try {
                Commande command = null;
                String response = "OK";

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
                    break;
                } else {
                    _server.addLog(_socket, command);
                    try {
                        _server.traiteCommande(command);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        response = "KO: wrong arguments";
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        response = "KO: could not be loaded";
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        response = "KO: can't access the class, attribute or method";
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        response = "KO: compilation of files failed";
                    }
                    String tmpResponse = _server.popClientResponse();

                    if (!tmpResponse.isEmpty())
                        response = tmpResponse;
                    
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
            }
        }
        _server.removeClientResponse();
    }
}