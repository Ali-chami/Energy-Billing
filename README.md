# Projet Energy Billing

Ce projet est une application de facturation d'énergie réalisé dans le cadre d'un test technique pour ekWateur. 
Il permet de calculer la facture énergétique d'un client particulier ou professionnel en fonction de sa consommation sur un mois calendaire avec les tarifs en vigueur.

## Prérequis

- Java 13 ou une version supérieure
- Maven

## Configuration

Avant d'exécuter l'application, assurez-vous d'avoir configuré correctement les tarifs énergétiques dans le fichier `energy_prices.properties` situé dans le répertoire `src/main/resources`. Modifiez les valeurs des propriétés selon les tarifs en vigueur.

## Compilation

Pour compiler le projet, exécutez la commande suivante depuis la racine du projet :

mvn clean package
