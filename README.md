# Système de Transfert de Fichiers Sécurisé

Ce projet implémente un **système de transfert de fichiers sécurisé** client-serveur en Java. Il permet à un client d’envoyer un fichier à un serveur de manière chiffrée, avec vérification d’intégrité.

## Fonctionnalités

- Authentification client (login/mot de passe)  
- Négociation des métadonnées du fichier (nom, taille, empreinte SHA-256)  
- Transfert du fichier chiffré en AES (mode AES/ECB/PKCS5Padding)  
- Vérification du hachage SHA-256 pour assurer l’intégrité du fichier  
- Gestion des connexions clients en parallèle (multithreading)  

## Structure du projet 
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
│       └── (vide)
└── received_files/   ← créé automatiquement par le serveur

## Installation et exécution

1. **Cloner le dépôt GitHub**  
   ```bash
   git clone https://github.com/Douaa03/secure_file_transfer.git
   cd secure_file_transfer

