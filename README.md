# Programmation objet avancée : TP1

Repository du projet sur Github: https://github.com/QuentinKlebaur/oop_tp1

## I - Usage

### Lancer le projet

Le programme se compile et se lance grâce à un Makefile à la racine de ce répertoire.
Les commandes sont les suivantes:
- **make**: Compile le serveur et le client
- **make server**: Compile les fichiers sources du serveur et déplace les fichiers ".class" dans un dossier build
- **make client**: Compile les fichiers sources du client et déplace les fichiers ".class" dans un dossier build
- **make clean**: Vide les dossiers build du client et du serveur
- **make clean_server**: Vide le dossier build du serveur
- **make clean_client**: Vide le dossier build du client
- **make run_server**: Execute le projet du serveur
- **make run_client**: Execute le projet du client

### Changer les arguments d'entré de chaque projet

#### Serveur

Dans le Makefile se trouvant dans le répertoire "Server", on peut changer la valeur des variables suivantes pour changer les arguments d'entré du programme:
- **PORT**: Le port du serveur
- **INPUT_FOLDER**: Le répertoire d'entré du projet
- **CLASS_FOLDER**: Le répertoire des classes
- **OUTPUT_FILE**: Le fichier de sortie

#### Client

Dans le Makefile se trouvant dans le répertoire "Client", on peut changer la valeur des variables suivantes pour changer les arguments d'entré du programme:
- **HOST**: L'addresse du serveur auquel le client doit se connecter
- **PORT**: Le port du serveur auquel le client doit se connecter
- **COMMAND_FILE**: Le fichier de commandes à lire
- **OUTPUT_FILE**: Le fichier de sortie

#### Précision

Tous les chemins de fichiers renseignés aux différents programmes sont relatifs à l'endroit où se situe les fichiers ".class" du programme. C'est-à-dire "Client/build/" et "Server/build/"

## II - Documentation

#### Serveur

La documentation du serveur peut être visualisé en ouvrant le fichier **"./Server/Documentation/index.html"**

#### Client

La documentation du client peut être visualisé en ouvrant le fichier **"./Client/Documentation/index.html"**
