import java.io.*;
import java.util.Arrays;

class Commande implements java.io.Serializable {
    public Commande(String command) {
        _command = command.split("#");
    }

    private String[] _command = null;

    /**
    * Renvoie les éléments de la commande
    */
    public String[] getCommand() {
        return _command;
    }

    /**
    * Affiche la commande
    */
    public void dump() {
        String elem = "";

        for (int i = 0; i < _command.length; ++i) {
            if (i == 0)
                elem = "Command";
            else
                elem = "Arg" + Integer.toString(i); 
            elem += ": " + _command[i];
            System.out.println(elem);
        }
    }

    /**
    * Renvoie le nom de la commande
    */
    public String getName() {
        if (_command.length >= 1)
            return _command[0];
        return "";
    }

    /**
    * Renvoie les arguments de la commande
    */
    public String[] getArgs() {
        if (_command.length >= 2)
            return Arrays.copyOfRange(_command, 1, _command.length);
        return new String[0];
    }
}