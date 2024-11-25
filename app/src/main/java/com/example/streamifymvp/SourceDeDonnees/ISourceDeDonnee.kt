package com.example.streamifymvp.SourceDeDonnees

import Profil
import com.example.streamifymvp.Domaine.Modeles.ShowDate

interface ISourceDeDonnee {
    fun obtenirProfil(): Profil?
    fun modifierNomUtilisateur(nouveauNom: String): Boolean
    fun modifierUsername(nouveauUsername: String): Boolean
    fun obtenirHistoriqueRecherche(): List<String>
    fun obtenirToutesLesDatesDeShow(): List<ShowDate>
}
