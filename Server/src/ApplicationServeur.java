import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;
import java.text.*;

public class ApplicationServeur
{
    /**
    * prend le numéro de port, crée un SocketServer sur le port
    */
    public ApplicationServeur (int port) throws IOException {
        _socket = new ServerSocket(port);
        _clients = new LinkedList<Socket>();
        _running = true;
    }

    private ServerSocket _socket;
    private List<Socket> _clients;
    private PrintWriter _logger;
    private File _inputFolder;
    private File _classesFolder;
    private boolean _running;

    /**
    * Permet d'initialiser les fichiers et dossiers avec lesquels le programme va interagir
    */
    public void initialize(String inputFolderPath, String classesFolderPath, String outputFilePath) throws IOException {
        _inputFolder = new File(inputFolderPath);
        _classesFolder = new File(classesFolderPath);
        File outputFile = new File(outputFilePath);

        if (!_inputFolder.isDirectory())
            throw new FileNotFoundException('\"' + inputFolderPath + '\"' + " is not a folder");
        if (!_classesFolder.isDirectory())
            throw new FileNotFoundException('\"' + classesFolderPath + '\"' + " is not a folder");
        if (!outputFile.isFile())
            throw new FileNotFoundException('\"' + outputFilePath + '\"' + " is not a file");
        _logger = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)));
        addLog("Server is running on the port [" + Integer.toString(_socket.getLocalPort()) + "]");
    }

    /**
    * Arrête le serveur
    */
    public void stop() {
        addLog("Server has been stopped");
        _running = false;
    }

    /**
    * écrit les logs
    */
    public void addLog(String message) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String log = "[" + now + "] " + message;

        System.out.println(log);
    }

    /**
    * écrit un log en précisant les port du client
    */
    public void addLog(Socket socket, String message) {
        addLog("[" + Integer.toString(socket.getPort()) + "] " + message);
    }

    /**
    * écrit les logs d'une commande
    */
    public void addLog(Socket socket, Commande command) {
        addLog(socket, "Command received:");
        command.dump();
    }

    /**
    * Se met en attente de connexions des clients. Suite aux connexions, elle lit
    * ce qui est envoyé à travers la Socket, recrée l’objet Commande envoyé par
    * le client, et appellera traiterCommande(Commande uneCommande)
    */
    public void aVosOrdres() {
        while (_running) {
            try {
                Socket client = _socket.accept();
                _clients.add(client);
                addLog(client, "New client connected");

                new ClientSessionThread(this, client).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
    * prend uneCommande dument formattée, et la traite. Dépendant du type de commande,
    * elle appelle la méthode spécialisée
    */
    public void traiteCommande(Commande uneCommande) {

    }

    /**
    * traiterLecture : traite la lecture d’un attribut. Renvoies le résultat par le
    * socket
    */
    public void traiterLecture(Object pointeurObjet, String attribut) {

    }

    /**
    * traiterEcriture : traite l’écriture d’un attribut. Confirmes au client que l’écriture
    * s’est faite correctement.
    */
    public void traiterEcriture(Object pointeurObjet, String attribut, Object valeur) {

    }
 
    /**
    * traiterCreation : traite la création d’un objet. Confirme au client que la création
    * s’est faite correctement.
    */
    public void traiterCreation(Class classeDeLobjet, String identificateur) {

    }
 
    /**
    * traiterChargement : traite le chargement d’une classe. Confirmes au client que la création
    * s’est faite correctement.
    */
    public void traiterChargement(String nomQualifie) {

    }
 
    /**
    * traiterCompilation : traite la compilation d’un fichier source java. Confirme au client
    * que la compilation s’est faite correctement. Le fichier source est donné par son chemin
    * relatif par rapport au chemin des fichiers sources.
    */
    public void traiterCompilation(String cheminRelatifFichierSource) {

    }

    /**
    * traiterAppel : traite l’appel d’une méthode, en prenant comme argument l’objet
    * sur lequel on effectue l’appel, le nom de la fonction à appeler, un tableau de nom de
    * types des arguments, et un tableau d’arguments pour la fonction. Le résultat de la
    * fonction est renvoyé par le serveur au client (ou le message que tout s’est bien
    * passé)
    */
    public void traiterAppel(Object pointeurObjet, String nomFonction, String[] types,
        Object[] valeurs) {

        }

    /**
    * programme principal. Prend 4 arguments: 1) numéro de port, 2) répertoire source, 3)
    * répertoire classes, et 4) nom du fichier de traces (sortie)
    * Cette méthode doit créer une instance de la classe ApplicationServeur, l’initialiser
    * puis appeler aVosOrdres sur cet objet
    */
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Wrong number of arguments");
            return;
        }
        ApplicationServeur server;

        try {
            int port = Integer.parseInt(args[0]);
            String inputFolder = args[1];
            String classesFolder = args[2];
            String outputFile = args[3];
            server = new ApplicationServeur(port);

            server.initialize(inputFolder, classesFolder, outputFile);

            Runtime.getRuntime().addShutdownHook(new StopThread(server));
            server.aVosOrdres();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}