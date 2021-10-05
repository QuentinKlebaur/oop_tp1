    /**
    * Permet d'arrêter le serveur
    */
public class StopThread extends Thread {

    /**
    * Prend le serveur à arrêter en paramètre
    */
    public StopThread(ApplicationServeur server) {
        _server = server;
    }

    private ApplicationServeur _server;
    
    /**
    * Arrête le serveur quand le thread est lancé
    */
    public void run() {
        _server.stop();
    }
}