# Programmation objet avancée : TP1

Repository du projet sur Github: https://github.com/QuentinKlebaur/oop_tp1

## I - Utilisation

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

## III - Notes de conception

### Introduction

Dans cette partie j'explique partiellement comment j'ai conçu mon programme et des choix technique que j'ai fait.

### Le serveur

#### Communication

Afin de faire communiquer mes deux programmes j'utilise des socket, car ayant déjà travaillé avec des technologies similaires dans d'autres langages, je suis familier avec cette technologie. Java possède une implémentation facile à utiliser de socket, donc grâce à un ServerSocket, le serveur va attendre que des clients se connectent et créera pour chacun d'eux un **ClientSessionThread**.

Le **ClientSessionThread** va s'occuper de communiquer avec le client tout au long de son cycle de vie. Lorsque le client se déconnecte, le thread se termine.
Lorsqu'il échange avec le client, il covertie le message reçu en classe **Commande** et fait traiter la commande par le **ApplicationServeur**.

#### Les commandes

La classe **Commande** hérite de **Serializable** afin d'être envoyé par le client au serveur. Cette classe permet également de décomposer une commande en tableau d'arguments, ce qui facilite l'analyse de cette dernière.

#### Interprétation des commandes

Les **Commandes** sont traitées par le **ApplicationServeur** grâce à la fonction **traiteCommande** qui va appeler une fonction *manage{nom de la commande}* qui va transformer la commande en arguments utilisables par les fonctions d'éxecutions des différentes commandes qui sont demandées dans le sujet.

#### Compilation des fichiers

Pour compiler des fichier on éxecute la commande *javac* grâce à **Process** qui permet d'éxecuter des commandes comme si on utilisait un terminal. On précise à la classe **Process** les chemins des fichiers à compiler.

#### Charger les fichiers

Pour charger une classe dynamiquement j'utilise la classe **ClassLoader** qui permet de récupérer une classe **Class** à partir d'un fichier compilé. La **Class** est ensuite stocké pour être réutilisé plus tard.

#### Créer une instance de classe

En utilisant les **Class** stockés on peut instancier des classes chargées dynamiquements. Les instances sont stockés dans une map et mis en relation avec leur identificateur sous forme de **String**. Cela va permettre de facilement récupérer une instance d'un objet grâce à son identificateur.

#### Lecture d'un attribut

Afin de lire un attribut, j'ai créé les fonction **getField** et **getField** qui permettent respectivement de récupérer un **Field** et une **Method** d'un **Object** en fonction de son nom. Cela permet ensuite de de récupérer la valeur du **Field** et de la **Method** en passant l'**Object** en paramètre.

#### Ecriture d'un attribut

Le fonctionnement de l'écriture ressemble à l'écriture car au lieu de récupérer la valeur, il faut récupérer la valeur.

#### Appel de fonction

La partie d'appel de fonction est plus compliqué d'un point de vue algorithmique car il faut vérifier si les arguments données sont soit d'un type statique ou soit d'un type dynamique. Dans le cas d'un type statique, il faut convertir la **String** reçu en le type qui est demandé, ceci est le but de la fonction **toObject**. Si l'argument attendu est un type dynamique, il faut récupérer l'instance demandé.
Une fois les arguments récupérés/générés il faut les passer à la **Method** demandé.
Malheureusement lorsqu'une fonction a besoin d'un type dynamique, il n'est pas reconnu par la fonction et l'appel de la méthode échoue.

#### Les logs

J'ai également créé des fonctions **addLog** afin de d'écrire dans le fichier de log et dans le terminal.

### Le client

#### Communication

Le client se connecte au serveur grâce à un **Socket**. Il alterne entre écriture et lecture du **Socket** en envoyant des **Commande** sérialisées.

#### Lecture des commandes

Les commandes sont simplement lues ligne par ligne depuis le fichier source, puis transformées en **Commande** pour être envoyées au serveur.

### Conclusion

Le Java est un langage que je n'avais jamais utilisé auparavant, mais grâce à mes connaissances en programmation orientée objet et tous les outils que proposent le Java, il a été assez intuitif de réaliser les différentes parties de ce projet, bien qu'il soit très dense. Pour moi la grande nouveauté fut d'utiliser la métaprogrammation. Un autre challenge fut de respecter parfaitement l'architecture proposé et de garder les fonctions comme elles ont été prototypées dans le sujet.
Malheureusement le produit final n'est pas parfaitement fonctionnel à cause de ce soucis de cast des classes dynamiques dans les méthodes dynamiques.