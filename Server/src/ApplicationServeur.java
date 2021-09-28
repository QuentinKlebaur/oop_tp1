import java.io.*;
import java.net.*;
import java.lang.*;
import java.lang.reflect.*;
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
        _loadedClasses = new LinkedList<Class>();
        _instances = new HashMap<String, Object>();
        _responses = new HashMap<Long, String>();
        _running = true;
    }

    private ServerSocket _socket;
    private List<Socket> _clients;
    private List<Class> _loadedClasses;
    private Map<String, Object> _instances;
    private Map<Long, String> _responses;
    private PrintWriter _logger;
    private File _inputFolder;
    private File _classesFolder;
    private boolean _running;

    /**
    * Add a response for a ClientSessionThread
    */
    void addClientResponse() {
        _responses.put(Thread.currentThread().getId(), "");
    }

    /**
    * Update a response for a ClientSessionThread
    */
    void updateClientResponse(String message) {
        _responses.put(Thread.currentThread().getId(), message);
    }

    /**
    * Pop a response for a ClientSessionThread
    */
    String popClientResponse() {
        String response = _responses.get(Thread.currentThread().getId());

        _responses.put(Thread.currentThread().getId(), "");
        return response;
    }

    /**
    * Remove a response for a ClientSessionThread
    */
    void removeClientResponse() {
       // _responses.remove(Thread.currentThread().getId());
    }

    
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
    * Gère le passage d'arguments pour la commande compilation
    */
    public void manageCompilation(String[] args) {
        if (args.length != 2)
            throw new IllegalArgumentException();
        
        String[] paths = args[0].split(",");
        String classFolder = args[1];

        for (int i = 0; i < paths.length; ++i)
            traiterCompilation(paths[i]);
    }

    /**
    * Gère le passage d'arguments pour la commande chargement
    */
    public void manageChargement(String[] args) throws ClassNotFoundException {
        if (args.length != 1)
            throw new IllegalArgumentException();
        traiterChargement(args[0]);
    }

    /**
    * Renvoie une classe chargée si elle existe grâce au nom de celle-ci. Dans le cas contraire, renvoie null
    */
    private Class getLoadedClass(String name) {
        for (Class classType : _loadedClasses) {
            if (classType.getName().equals(name))
                return classType;
        }
        return null;
    }

    /**
    * Gère le passage d'arguments pour la commande creation
    */
    public void manageCreation(String[] args) throws IllegalAccessException {
        if (args.length != 2)
            throw new IllegalArgumentException();
        String className = args[0];
        String identificator = args[1];
        Class classType = getLoadedClass(className);

        if (classType == null || identificator.isEmpty())
            throw new IllegalArgumentException();
        traiterCreation(classType, identificator);
    }

    /**
    * Gère le passage d'arguments pour la commande lecture
    */
    public void manageLecture(String[] args) throws IllegalAccessException {
        if (args.length != 2)
            throw new IllegalArgumentException();
        String identificator = args[0];
        String attribute = args[1];
        Object object = _instances.get(identificator);

        if (object == null)
            throw new IllegalArgumentException();
        traiterLecture(object, attribute);
    }

    /**
    * Gère le passage d'arguments pour la commande écriture
    */
    public void manageEcriture(String[] args) throws IllegalAccessException {
        if (args.length != 3) {
            System.out.println("NUMBER OF ARGUMENTS");
            throw new IllegalArgumentException();
        }
        String identificator = args[0];
        String attribute = args[1];
        Object value = args[2];
        Object object = _instances.get(identificator);

        if (object == null) {
            System.out.println("OBJECT DOES NOT EXISTS");
            throw new IllegalArgumentException();
        }
        traiterEcriture(object, attribute, value);
    }

    /**
    * Gère le passage d'arguments pour la commande fonction
    */
    public void manageFonction(String[] args) {
        System.out.println("FONCTION");
    }

    /**
    * prend uneCommande dument formattée, et la traite. Dépendant du type de commande,
    * elle appelle la méthode spécialisée
    */
    public void traiteCommande(Commande uneCommande) throws ClassNotFoundException, IllegalAccessException {
        switch (uneCommande.getName()) {
            case "compilation":
                manageCompilation(uneCommande.getArgs());
                break;

            case "chargement":
                manageChargement(uneCommande.getArgs());
                break;

            case "creation":
                manageCreation(uneCommande.getArgs());                
                break;

            case "lecture":
                manageLecture(uneCommande.getArgs());

                break;

            case "ecriture":
                manageEcriture(uneCommande.getArgs());
                break;

            case "fonction":
                manageFonction(uneCommande.getArgs());
                break;

            default:
                break;
        }
    }

    /**
    * Recherche un attribut d'une classe. Renvoie null s'il n'est pas trouvé
    */
    private Field getField(Class classType, String name) {
        for (Field field : classType.getDeclaredFields())
            if (field.getName().equals(name))
                return field;
        return null;
    }

    /**
    * Recherche une méthode d'un objet. Renvoie null s'il n'est pas trouvé
    */
    private Method getMethod(Class classType, String name) {
        for (Method method : classType.getDeclaredMethods())
            if (method.getName().equals(name))
                return method;
        return null;
    }

    /**
    * traiterLecture : traite la lecture d’un attribut. Renvoies le résultat par le
    * socket
    */
    public void traiterLecture(Object pointeurObjet, String attribut) throws IllegalAccessException {
        Field searchedField = getField(pointeurObjet.getClass(), attribut);
        String getMethodName = "get" + Character.toUpperCase(attribut.charAt(0)) + attribut.substring(1);
        Method searchedMethod = null;
        Object result = null;

        if (searchedField == null)
            throw new IllegalArgumentException();
        
        try {
            result = searchedField.get(pointeurObjet);
        } catch (IllegalAccessException e) {
            searchedMethod = getMethod(pointeurObjet.getClass(), getMethodName);
            if (searchedMethod == null || searchedMethod.getGenericParameterTypes().length != 0)
                throw new IllegalAccessException();
            try {
                result = searchedMethod.invoke(pointeurObjet);
            } catch (IllegalAccessException ex) {
                throw new IllegalAccessException();
            } catch (InvocationTargetException ex) {
                throw new IllegalAccessException();
            }
        }
        addLog("The value of the attribute \"" + attribut + "\" is \"" + result + "\"");
        updateClientResponse(result.toString());
    }

    /**
    * traiterEcriture : traite l’écriture d’un attribut. Confirmes au client que l’écriture
    * s’est faite correctement.
    */
    public void traiterEcriture(Object pointeurObjet, String attribut, Object valeur) throws IllegalAccessException {
        Field searchedField = getField(pointeurObjet.getClass(), attribut);
        String getMethodName = "set" + Character.toUpperCase(attribut.charAt(0)) + attribut.substring(1);
        Method searchedMethod = null;

        if (searchedField == null)
            throw new IllegalArgumentException();
        try {
            Class type = searchedField.getType();

            searchedField.set(pointeurObjet, valeur);
        } catch (IllegalAccessException e) {
            searchedMethod = getMethod(pointeurObjet.getClass(), getMethodName);
            if (searchedMethod == null || searchedMethod.getGenericParameterTypes().length != 1)
            throw new IllegalAccessException();
            try {
                Class type = Class.forName(searchedMethod.getGenericParameterTypes()[0].getTypeName());
                
                searchedMethod.invoke(pointeurObjet, type.cast(valeur));
            } catch (IllegalAccessException ex) {
                throw new IllegalAccessException();
            } catch (InvocationTargetException ex) {
                throw new IllegalAccessException();
            } catch (ClassNotFoundException ex) {
                throw new IllegalAccessException();
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException();
        }
        addLog("The value of the attribute \"" + attribut + "\" has been changed by \"" + valeur + "\"");
    }
 
    /**
    * traiterCreation : traite la création d’un objet. Confirme au client que la création
    * s’est faite correctement.
    */
    public void traiterCreation(Class classeDeLobjet, String identificateur) throws IllegalAccessException {
        try {
            Object o = classeDeLobjet.newInstance();

            _instances.put(identificateur, o);
            addLog("New instance of \"" + classeDeLobjet.getName() + "\" named \"" + identificateur + "\" has beed created");
        } catch(InstantiationException e) {
            throw new IllegalArgumentException();
        }
    }
 
    /**
    * traiterChargement : traite le chargement d’une classe. Confirmes au client que la création
    * s’est faite correctement.
    */
    public void traiterChargement(String nomQualifie) throws ClassNotFoundException {
        try {
            URL[] urlList = new URL[] {_classesFolder.toURI().toURL()};

            ClassLoader classLoader = new URLClassLoader(urlList);
            Class newClass = classLoader.loadClass(nomQualifie);

            addLog("Class loaded: " + newClass.getName());
            _loadedClasses.add(newClass);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }
 
    /**
    * traiterCompilation : traite la compilation d’un fichier source java. Confirme au client
    * que la compilation s’est faite correctement. Le fichier source est donné par son chemin
    * relatif par rapport au chemin des fichiers sources.
    */
    public void traiterCompilation(String cheminRelatifFichierSource) {
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("javac", "-d", _classesFolder.getPath(),  cheminRelatifFichierSource);

        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        addLog("Compilation of file: " + cheminRelatifFichierSource);
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