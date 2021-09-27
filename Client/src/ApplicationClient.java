import java.net.*;
import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;

class ApplicationClient
{
    private Socket _socket;
    private BufferedReader commandesReader;
    private PrintWriter  sortieWriter;
    BufferedReader _inStream = null;
    ObjectOutputStream _outStream = null;

    /**
    * écrit les logs
    */
    public void addLog(String message) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String log = "[" + now + "] " + message;

        System.out.println(log);
    }

    /**
    * écrit les logs d'une commande
    */
    public void addLog(Commande command) {
        addLog("Command sent:");
        command.dump();
    }

    /**
    * Permet au client de se connecter au serveur
    */
    public void connect(String host, int port) throws UnknownHostException, IOException {
        _socket = new Socket(host, port);
        _inStream = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        _outStream = new  ObjectOutputStream(_socket.getOutputStream());
    }

    /**
    * prend le fichier contenant la liste des commandes, et le charge dans une
    * variable du type Commande qui est retournée
    */
    public Commande saisisCommande(BufferedReader fichier) {
        Commande command = null;
        
        try {
            String line = fichier.readLine();
            if (line == null)
                return null;
            command = new Commande(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return command;
    }

    /**
    * initialise : ouvre les différents fichiers de lecture et écriture
    */
    public void initialise(String fichCommandes, String fichSortie) throws IOException {
        String error = "";
        File commandFile = new File(fichCommandes);
        File outputFile = new File(fichSortie);

        if (!commandFile.isFile())
            throw new  FileNotFoundException('\"' + fichCommandes + '\"' + " is not a file");
        if (!outputFile.isFile())
            throw new  FileNotFoundException('\"' + fichSortie + '\"' + " is not a file");
        commandesReader = new BufferedReader(new FileReader(fichCommandes));
        sortieWriter = new PrintWriter(new BufferedWriter(new FileWriter(fichSortie)));
    }

    /**
    * prend une Commande dûment formatée, et la fait exécuter par le serveur. Le résultat de
    * l’exécution est retournée. Si la commande ne retourne pas de résultat, on retourne null.
    * Chaque appel doit ouvrir une connexion, exécuter, et fermer la connexion. Si vous le
    * souhaitez, vous pourriez écrire six fonctions spécialisées, une par type de commande
    * décrit plus haut, qui seront appelées par traiteCommande(Commande uneCommande)
    */
    public Object traiteCommande(Commande uneCommande) {
        Object object = new Object();

        try {
            _outStream.writeObject(uneCommande);
            addLog(uneCommande);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String response = _inStream.readLine();
            addLog("Response received \"" + response + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
    * cette méthode vous sera fournie plus tard. Elle indiquera la séquence d’étapes à exécuter
    * pour le test. Elle fera des appels successifs à saisisCommande(BufferedReader fichier) et
    * traiteCommande(Commande uneCommande).
    */
    public void scenario() {
        sortieWriter.println("Debut des traitements:");
        Commande prochaine = saisisCommande(commandesReader);

        while (prochaine != null) {
            sortieWriter.println("\tTraitement de la commande " + prochaine + " ...");
            Object resultat = traiteCommande(prochaine);
            sortieWriter.println("\t\tResultat: " + resultat);
            prochaine = saisisCommande(commandesReader);
        }
        sortieWriter.println("Fin des traitements");
    }

    /**
    * programme principal. Prend 4 arguments: 1) “hostname” du serveur, 2) numéro de port,
    * 3) nom fichier commandes, et 4) nom fichier sortie. Cette méthode doit créer une
    * instance de la classe ApplicationClient, l’initialiser, puis exécuter le scénario
    */
    public static void main(String[] args) {
        ApplicationClient client;

        if (args.length != 4) {
            System.out.println("Wrong number of arguments");
            return;
        }

        try {
            int port = Integer.parseInt(args[1]);
            String host = args[0];
            String commandFile = args[2];
            String outputFile = args[3];
            client = new ApplicationClient();

            client.initialise(commandFile, outputFile);
            client.connect(host, port);
            
            client.scenario();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}