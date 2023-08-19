<p align="center">
  <img src="./docs/finally-logo.png">
</p>

# Finally - Point de vente
Finally est une application "terminal de point de vente" (ou caisse enregistreuse), permettant aux petits commerçants de finaliser facilement les transactions commerciales avec leurs clients. <br>
L'application ne permet que pour l'instant une utilisation simple : gestion des produits, des prix/réductions, ainsi que le paiement. <br>
L'application utilise une boulangerie comme commerce de démonstration.
<br>

**Attention : Il ne s'agit pas d'une application à but lucratif, mais d'un projet de fin de formation pour M2i Formation.**

## Installation de l'application avec Docker
Pour pouvoir lancer l'application, il vous faut Docker (https://www.docker.com) afin d'initialiser la base de données.
Pour ce faire, rendez-vous dans le dossier `backend` de l'application. Dans votre terminal de commandes tapez `docker compose up -d`. <br>

La base de données s'initialisera. L'interface de gestion est disponible à l'url suivant : `http://localhost:5050`.

Les identifiants de connexion à pgAdmin sont :
- Mail : `admin@admin.com`
- Mot de passe : `root`

## Accéder à l'application et documentation de l'API
Pour lancer l'application, retournez dans le dossier racine et dans votre terminal de commande exécutez la commande : `java -jar backend-0.0.1-SNAPSHOT`.

Si votre base de données est démarrée correctement, alors l'application devrait être accessible à l'url `http:/localhost:8080/documentation.html` où vous trouverez la documentation de l'API.

## Equipe de développement
- **Huan Xie** (https://github.com/thehuanxie)
- **Mélanie Hautekeur** (https://github.com/MelanieHautekeur)
- **Sébastien Duhamel** (https://github.com/sebdDev)
- **Etienne Verraest** (https://github.com/etienne-verraest)
