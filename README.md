- [Exécution](#execution_001)
- [Fonctionnalités](#fontions_001)
- [Contexte](#org2602fda)
- [MVP](#org067d6e3)
- [Objectif](#orgeb6e119)
- [Prétraitements](#org906660d)

<a id="execution_001"></a>

# Exécution
Le programme se lance par la classe *Main* présente dans le package *fr.simplon.devweb2019.vincent.javaprojectbooks*.


**Installation :**

Importer le projet dans l'IDE, et lancer la classe Main avec ou sans arguments.

**Exemples de paramètres d'exécution :**

- Avec trois livres correctements nommés : 

    *"./books/Smith/Smith-Richesse_des_Nations_4.txt" "./books/Spinoza/Spinoza-Ethique.txt" "./books/Spinoza/Spinoza-Traite_Politique.txt"*

- Avec trois livres dont un mal nommé : 
    
    *"./books/Smith/Smith-Richesse_des_Nations_4.txt" "./book/Spinoza/Spinoza-Ethique.txt" "./books/Spinoza/Spinoza-Traite_Politique.txt"*


**Postulat de départ :**

On considère que tous les livres de la bibliothèques sont stockés dans le répertoire /books ou l'un de ses sous-répertoires.


<a id="fontions_001"></a>
# Fonctionnalités
## Démarrage de l'application
A chaque lancement de l'application, les actions suivantes sont effectuées :
- Contrôle des arguments reçus : les fichiers reçus doivent exister dans le système d'exploitation. Si un ou plusieurs fichiers n'ont pas été trouvé, l'application en affiche la liste pour en informer l'utilisateur, puis se ferme.

- Suppression de tous les fichiers du répertoire */prepreocess* (un prétraitrement est lancé à chaque ajout de nouveau livre, afin de stocker dans le répertoire */preprocess* la liste des mots au format un mot par ligne.
  
- Intégration des fichiers reçus : intégration des fichiers reçus dans la bilbiothèque par création d'un objet *Livre* pour chaque fichier reçu.

    Ces derniers sont initialisés avec les données suivantes :

    - Le nom du livre (nom du fichier).
    - Le chemin absolu du livre dans l'espace de stockage.
    - Le chemin absolu de l'extraction des mots du livre dans l'espace de stockage.
    - Le nombre de lignes du livre.
    - Le nombre de mots du livre.
    - Le nombre de mots uniques du livre.
    - Une table de hashage des couples mot / comptage utilisation pour chaque mot du livre.

- Ouverture du menu général de l'application   

## Menu général
Le menu général présente les options suivantes :
- (1) Lister les fichiers
- (2) Ajouter un fichier
- (3) Supprimer un fichier
- (4) Afficher des informations sur un livre
- (5) Quitter le programme 


Pour gérer les transitions de menu, à défaut de ne pouvoir faire un *clear* de la console de sortie java, le développeur a décidé de simuler cette fonctionnalité en effectuant plusieurs *retour chariot* pour faire défiler la console de sortie et la "nettoyer".

### 1 - Lister les fichiers
Affiche la liste des livres de la bibliothèque.

**Exemple :**

>  --------------------------------------------------------------------------------------------
>  Liste des livres présents dans la bibliothèque :
>  
>    - Smith-Richesse_des_Nations_1.txt
>
>    - Spinoza-Ethique.txt
>
>    - Spinoza-Reforme.txt
>  
>  +++ Appuyez sur <Entrée> pour continuer...
>
>  --------------------------------------------------------------------------------------------

### 2 - Ajouter un fichier
Présente sous forme de liste d'options la liste de tous les fichiers non déja associés à la bibliothèque, situés dans le répertoire */books* ou l'un de ses sous-répertoires.

La méthode de parcours des répertoires est récursive.

Après validation de l'option choisie, le livre sélectionné est ajouté dans la bibliothèque. 

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
> Liste des livres disponibles :
> 
> (1) /home/bnp-renault-009/simplon/briefs/java-project-books/books/Smith/Smith-Richesse_des_Nations_2.txt
>
> (2) /home/bnp-renault-009/simplon/briefs/java-project-books/books/Smith/Smith-Richesse_des_Nations_3.txt
>
> (3) /home/bnp-renault-009/simplon/briefs/java-project-books/books/Smith/Smith-Richesse_des_Nations_4.txt
>
> (4) /home/bnp-renault-009/simplon/briefs/java-project-books/books/Smith/Smith-Richesse_des_Nations_5.txt
>
> (5) /home/bnp-renault-009/simplon/briefs/java-project-books/books/Spinoza/Spinoza-Traite_Politique.txt
>
> +++ Veuillez saisir le numéro du fichier à ajouter : 5
>
> Ajout dans la bibliothèque du fichier /home/bnp-renault-009/simplon/briefs/java-project-books/books/Spinoza/Spinoza-Traite_Politique.txt...en cours...
>
> Ajout dans la bibliothèque du fichier /home/bnp-renault-009/simplon/briefs/java-project-books/books/Spinoza/Spinoza-Traite_Politique.txt ...terminé.
>
> Le livre suivant a été ajouté dans la bibliothèque :
> 
> Spinoza-Traite_Politique.txt
>
>  --------------------------------------------------------------------------------------------

### 3 - Supprimer un fichier
Présente sous forme de liste d'options la liste des livres de la bibliothèque.

Après validation de l'option choisie, le livre sélectionné est supprimé de la bibliothèque.

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
> Liste des livres présents dans la bibliothèque :
>  
>  (1) Smith-Richesse_des_Nations_1.txt
>
>  (2) Spinoza-Ethique.txt
>
>  (3) Spinoza-Reforme.txt
>
>  (4) Spinoza-Traite_Politique.txt
>
>  
>  +++ Veuillez saisir le numéro du fichier à supprimer : 1
>  
>  Le livre suivant a été supprimé de la bibliothèque : 
>
>  Smith-Richesse_des_Nations_1.txt
>  
>  +++ Appuyez sur <Entrée> pour continuer...
>
>  --------------------------------------------------------------------------------------------

### 4 - Afficher les informations d'un livre
Présente sous forme de liste d'options la liste des livres de la bibliothèque.

Après validation de l'option choisie, un sous-menu suivant est proposé pour permettre d'extraire des informations détaillées pour le livre sélectionné :
- (1) Affiche le nombre de lignes du fichier
- (2) Affiche le nombre de mots du fichier
- (3) Affiche les 50 mots les plus fréquents et leur nombre d'occurrences
- (4) Affiche les mots qui sont présents seulement dans ce fichier et aucun des autres fichiers
- (5) Affiche pour chacun des autres fichiers le pourcentage de mots de l'autre fichier qui sont présents dans le fichier sélectionnés, par ordre décroissant de ce pourcentage.
- (6) Affiche le nombre de mots unique du fichier

- (7) Retour au menu précédent

### 4.1 - Affiche le nombre de lignes du fichier
Affiche le nombre total de ligne du livre (la méthode utilise le fichier original).

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
> Le livre contient 2560 lignes. ( ==> Spinoza-Ethique.txt )
>
>  --------------------------------------------------------------------------------------------

### 4.2 - Affiche le nombre de mots du fichier
Affiche le nombre total de mots du fichier (la méthode utilise le fichier prétraité avec un mot par ligne).

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
> Le livre contient 99863 mots. ( ==> Spinoza-Ethique.txt )
>
>  --------------------------------------------------------------------------------------------

### 4.3 - Affiche les 50 mots les plus fréquents et leur nombre d'occurrences
Affiche les 50 premiers mots les plus fréquents et leur nombre d'occurrences.

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
> Liste des 50 mots les plus fréquents dans le livre courant ( ==> Spinoza-Ethique.txt )
>  
>  de (4616), la (3289), l (2836), et (2442), est (2195), que (1927), d (1803), à (1758), en (1694), qu (1540), par (1428), il (1355), une (1350), nous (1274), les (1198), qui (1184), le (1142), prop (964), un (916), ce (887), ou (833), ne (800), des (787), même (775), plus (735), elle (715), chose (677), c (651), n (642), âme (611), être (597), corps (588), a (582), dieu (573), dans (561), nature (538), se (520), pas (511), du (499), proposition (477), pour (455), choses (453), comme (438), autre (433), peut (428), donc (426), s (421), cette (418), tant (416), sont (413)
>
>  --------------------------------------------------------------------------------------------

### 4.4 - Affiche les mots qui sont présents seulement dans ce fichier et aucun des autres fichiers
Affiche les mots qui sont présents seulement dans ce fichier et aucun des autres fichiers.

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
>Liste des mots uniques à ce livre : 1896 mot(s) ( ==> Spinoza-Ethique.txt )
> 
> 600, ab, abonde, absente, absentes, abstenons, abstienne, abstient, abstraction, ...
>
>  --------------------------------------------------------------------------------------------


### 4.5 - Affiche pour chacun des autres fichiers le pourcentage de mots de l'autre fichier qui sont présents dans le fichier sélectionné, par ordre décroissant de ce pourcentage.
Affiche pour chacun des autres fichiers le pourcentage de mots **uniques** de l'autre fichier qui sont présents dans le fichier sélectionné, par ordre décroissant de ce pourcentage.

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
>Taux d'utilisation des mots du livre courant ( ==> Spinoza-Ethique.txt ) dans les autres livres de la bibliothèque :
> 
> 44.82 % ==> Spinoza-Traite_Politique.txt
>
> 34.86 % ==> Spinoza-Reforme.txt
>
>  --------------------------------------------------------------------------------------------
 
### 4.6 - Affiche le nombre de mots unique du fichier
Affiche le nombre de mots unique du fichier.

**Exemple :**
>  --------------------------------------------------------------------------------------------
>
>Le livre contient 4248 mots différents. ( ==> Spinoza-Ethique.txt )
>
>  --------------------------------------------------------------------------------------------


<a id="org2602fda"></a>

# Contexte

Le programme doit permettre de travailler sur un ensemble de fichiers texte correspondant à des livres. On voudra pouvoir avoir des informations sur le contenu des ces livres.


<a id="org067d6e3"></a>

# MVP

Le programme doit accepter un nombre quelconque d'arguments qui sont des noms de fichiers. Ensuite, il doit présenter le menu suivant :

<div class="VERBATIM">
1.  Lister les fichiers
2.  Ajouter un fichier
3.  Supprimer un fichier
4.  Afficher des informations sur un livre
5.  Quitter le programme

</div>

Pendant toute l'exécution du programme, celui-ci maintient une liste des noms de fichiers, initialisée par les arguments du programme, qu'il est possible de consulter et modifier avec les trois premières options.

Le quatrième choix :

1.  affiche la liste des fichiers
2.  propose de choisir d'un de ces fichiers
3.  propose le sous-menu suivant :

<div class="VERBATIM">
1.  Afficher le nombre de lignes du fichier
2.  Afficher le nombre de mots du fichier

</div>


<a id="orgeb6e119"></a>

# Objectif

Parmi les informations proposées pour un fichier, ajouter :

-   afficher les 50 mots les plus fréquents et leur nombre d'occurrences
-   afficher les mots qui sont présents seulement dans ce fichier et aucun des autres fichiers
-   Afficher pour chacun des autres fichiers le pourcentage de mots de l'autre fichier qui sont présents dans le fichier sélectionnés, par ordre décroissant de ce pourcentage.


<a id="org906660d"></a>

# Prétraitements

Les fichiers de texte contiennent, en plus des mots, des signes, notamment de ponctuation, qu'on voudra éliminer. Pour faciliter le mini-projet, on peut le faire avec un prétraitement, par exemple en utilisant le programme suivant pour générer des fichiers ne contenant que les mots (un par ligne), d'un fichier texte :

```java
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookStatistics {
    public static void main(String[] args)throws FileNotFoundException {
        Pattern p = Pattern.compile("\\w+", Pattern.UNICODE_CHARACTER_CLASS);
        try(Scanner sc = new Scanner(new File(args[0]));
            PrintStream fileOut = new PrintStream(new FileOutputStream(args[1]))){
            for(int i=0; sc.hasNextLine(); ++i){
                for(Matcher m1 = p.matcher(sc.nextLine()); m1.find();) {
                    fileOut.println(m1.group());
                }
            }
        }
    }
}
```

On peut aussi réaliser ce programme en python :

```python
import sys
import re
with open(sys.argv[1]) as input, open(sys.argv[2], "w") as output:
    for line in input:
        for word in re.findall(r'\w+', line):
            output.write(word.lower())
            output.write('\n')
```

Si vous le voulez, vous pouvez vous inspirer de ce code pour que votre programme puisse traiter directement les fichiers de texte en réalisant vous-même l'extraction des mots.
