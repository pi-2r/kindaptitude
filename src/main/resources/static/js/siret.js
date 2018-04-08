
// Vérification du numéro de TVA Intracommunautaire et du SIRET
// Programmation agecsa
// http://www.agecsa.com
// 6 MAI 2013




// Retire tout ce qui n'est pas chiffre
function EpureNbr(sTexte) {
    var i=0;
    var lChaine=""+sTexte;
    while (i<eval(lChaine.length)) 	{
        if (lChaine.charCodeAt(i)<48 || lChaine.charCodeAt(i)>57) {
            lChaine=lChaine.substring(0,i)+lChaine.substring(i+1,lChaine.length);
            i--;
        }
        i++;
    }
    return lChaine;
}

function ValidSIREN(sSiren) {
    var lChaine=""+sSiren
    lChaine=""+EpureNbr(lChaine);
    //Test de la taille
    if (lChaine.length!=9) return "Le code SIREN n'a pas 9 chiffres";

    //Test de la clef
    var Tampon_Siren = "";
    var i=0;
    var val=0;

    // Formule :
    // On multiplie par 2 les chiffres de rang pair, on conserve les chiffres de rang impair
    // Rang :     1 2 3  4 5 6 7 8 9
    // SIRET :    4 0 4  8 3 3 0 4 8
    // Résultat : 4 0 4 16 3 6 0 8 8
    // On additionne tous ces chiffres un par un. Le modulo 10 de cette somme doit être 0
    // 4 + 4 + 1 + 6 + 3 + 6 + 8 + 8 = 40 effectivement divisible par 10 sans reste.
    // Le calcul est identique pour le SIRET mais sur 14 chiffres.

    for (i=0; i<9;i++) {
        val=lChaine.charAt(i);
        if ((i+1) % 2 == 0) {
            Tampon_Siren = Tampon_Siren + val * 2;
        }
        else {
            Tampon_Siren = Tampon_Siren + val;
        }
    }
    var Cumul_Siren = 0;
    i=0;
    while (i < eval(Tampon_Siren.length)) {
        Cumul_Siren=Cumul_Siren + eval(Tampon_Siren.charAt(i));
        i++;
    }
    //return Cumul_Siren;
    if (Cumul_Siren % 10 == 0) return "Le SIREN est valide"; else return "Le No SIREN n'est pas correct";
}

function ChercherSiren(sSIREN) {
    sUrl="http://www.societe.com/cgi-bin/recherche?rncs="+sSIREN
    NewWin=window.open(sUrl,'Recherche');
    console.log(NewWin);
}

function CalculTVA(sSiren) {
    var lChaine=""+sSiren
    lChaine=""+EpureNbr(lChaine);

    sClef="";
    sClef=( ( (eval(sSiren) % 97) *3 ) + 12 ) % 97
    // Modif Janvier 2012
    if (eval(sClef)<10) {
        sClef="0"+sClef ;
    }

    return sClef;
}

function verifiersiren(siren) {
    var sResult ="";
    sResult = ValidSIREN(siren);
    if (sResult == "Le SIREN est valide") {
        document.changeBusiness.tva.value    = "FR" + CalculTVA(siren) + " " + siren;
        //ChercherSiren(siren);
        //document.VERIF.TVA2.value    = CalculTVA(siren) + siren;
    }
    /*
    else {
        document.VERIF.SIREN2.value  = "";
        document.VERIF.TVA1.value    = "";
        document.VERIF.TVA2.value    = "";
    }*/
    if (sResult == "Le SIREN est valide"){
        return true;
    }
}
