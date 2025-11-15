# Secure File Transfer System (Java, TCP, AES, SHA-256)

Système de transfert de fichiers sécurisé développé en Java, basé sur une architecture Client–Serveur utilisant TCP, des threads, le chiffrement symétrique AES et la vérification d’intégrité via SHA-256.

---

## 📌 Fonctionnalités Principales

-  Authentification du client (login / password)
-  Chiffrement du fichier via **AES/ECB/PKCS5Padding**
-  Vérification d’intégrité via **SHA-256**
-  Transfert réseau **TCP** en 3 phases
-  Serveur **multithread** (un thread par client)
-  Sauvegarde des fichiers reçus côté serveur

---

## Architecture du Projet

secure-file-transfer/
│
├── pom.xml
├── src/
│   ├── main/java/
│   │   ├── com/secure/server/
│   │   │   ├── SecureFileServer.java
│   │   │   └── ClientTransferHandler.java
│   │   ├── com/secure/client/
│   │   │   └── SecureFileClient.java
│   │   └── com/secure/crypto/
│   │       ├── AESUtils.java
│   │       └── HashUtils.java
│   └── main/resources/
│
└── received_files/   ← fichiers reçus et déchiffrés

---

#  Protocole de Communication (3 Phases)

## 🔹 **Phase 1 : Authentification**

Le client envoie :
login
password

Réponse du serveur :
- ` AUTH_OK`
- `AUTH_FAIL` (puis fermeture de la connexion)

---

## 🔹 **Phase 2 : Négociation**

Le client envoie :
- nom du fichier
- taille (en octets) du fichier chiffré
- hash SHA-256 du fichier original

Réponse :
" ` EADY_FOR_TRANSFER `

---

## 🔹 **Phase 3 : Transfert + Vérification**

Le client envoie :
- le fichier **chiffré AES**

Le serveur :
1. Déchiffre le fichier  
2. Le sauvegarde dans `/received_files`  
3. Calcule SHA-256 localement  
4. Compare avec celui envoyé  

Réponse finale :
- `TRANSFER_SUCCESS`
- `TRANSFER_FAIL`

---

# 🔐 Détails Cryptographiques

### ✔ **Chiffrement AES**
- Algorithme : `AES`
- Mode : `ECB`
- Padding : `PKCS5Padding`
- Clé : 128 bits, partagée entre client et serveur
- API : `javax.crypto`

### ✔ **Hachage SHA-256**
Utilisation de :
```java
MessageDigest.getInstance("SHA-256");
````

Permet de vérifier que le fichier reçu n’a subi aucune modification.

---

# Exécution du Projet

##  1. Compiler avec Maven

```bash
mvn clean package
```

Génère :

```
target/secure-file-transfer-1.0-jar-with-dependencies.jar
```

---

##  2. Lancer le Serveur

```bash
java -cp target/secure-file-transfer-1.0-jar-with-dependencies.jar com.secure.server.SecureFileServer
```

---

##  3. Lancer le Client

```bash
java -cp target/secure-file-transfer-1.0-jar-with-dependencies.jar com.secure.client.SecureFileClient
```

---

#  Exemple d'Exécution du Client

```
IP Serveur: 127.0.0.1
Port: 5000
Login: admin
Password: 1234
Chemin du fichier: C:\Users\PC\Desktop\test.pdf

[CLIENT] Réponse serveur : TRANSFER_SUCCESS
```

---

#  Identifiants Fourni (Hardcoded)

| Login | Password |
| ----- | -------- |
| admin | 1234     |
| user  | pass     |

---

#  Description des Classes

### **SecureFileServer**

* Démarre un serveur TCP
* Écoute les connexions
* Spawn un thread `ClientTransferHandler` par client

### **ClientTransferHandler**

* Authentifie le client
* Gère la négociation
* Déchiffre, sauvegarde et vérifie le fichier
* Envoie le statut final

### **SecureFileClient**

* Interface console
* Hachage SHA-256
* Chiffrement AES
* Envoi du fichier au serveur

### **AESUtils**

* Encrypt/Decrypt AES

### **HashUtils**

* Calcul SHA-256

---

# Améliorations Possibles

* Utilisation de AES/CBC + IV aléatoire
* Ajout d’un protocole Diffie-Hellman pour l’échange de clé
* Interface graphique (JavaFX)
* Journalisation (Log4j2)
* Reprise de transfert / fichiers volumineux
* Support multi-fichiers simultanés

---

# Licence

Projet destiné à l’apprentissage — libre d’utilisation académique.



