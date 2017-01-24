package de.trispeedys.resourceplanning.entity.enumeration;

public enum MessageQueueType
{
    // initiale oder nachfolgende Erinnerung zur Positionswahl an Helfer
    SEND_REMINDER_MAIL,
    
    // (initiale) Anfrage an Helfer, ob Positionen aus Vorgänger-Event (wenn vorhanden) übernomen werden sollen
    REQUEST_OVERTAKE_LEGACY_POSITIONS,
    
    // Abfrage an einzelnen Helfer zwecks Positionstausch
    REQUEST_SIMPLE_SWAP,
    
    // Request-Cycle wurde (nur?) durch Admin unterbrochen
    REQUEST_CYCLE_FINALIZED
}