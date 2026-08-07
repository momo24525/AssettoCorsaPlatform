package it.webapp.ac_community_ita.entity;

public enum RegistrationStatus {
    PENDING,      // richiesta inviata, in attesa di conferma
    CONFIRMED,    // iscrizione confermata, il posto è garantito
    WAITLIST,     // evento pieno, utente in lista d'attesa
    CANCELLED,    // l'utente ha annullato l'iscrizione
    REJECTED      // iscrizione rifiutata (es. da un admin/moderatore)
}