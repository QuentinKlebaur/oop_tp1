import java.io.*;

class Commande implements java.io.Serializable {
    public Commande(String command) {
        _command = command.split("#");
    }

    private String[] _command = null;

    /**
    * Return the command
    */
    public String[] getCommand() {
        return _command;
    }

    /**
    * Display the command
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
}