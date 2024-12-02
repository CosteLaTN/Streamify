package com.example.streamifymvp.Presentation.EcranChansonsLDL

import com.example.streamifymvp.Domaine.entitees.Chanson
import com.example.streamifymvp.Domaine.entitees.ListeDeLecture

interface IEcranChansonsLDL {


    fun afficherChansons(chansons: List<Chanson>)


    fun afficherDetailsListeDeLecture(playlist: ListeDeLecture)


    fun afficherMessageAucuneChanson()


    fun afficherMessageErreur(message: String)
}
