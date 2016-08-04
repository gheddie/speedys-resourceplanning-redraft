package de.trispeedys.resourceplanning.entity.enumeration;

public enum MessageQueueType
{
    // initiale oder nachfolgende Erinnerung zur Positionswahl an Helfer
    SEND_REMINDER_MAIL,
    
    // (initiale) Anfrage an Helfer, ob Positionen aus Vorg�nger-Event (wenn vorhanden) �bernomen werden sollen
    REQUEST_OVERTAKE_LEGACY_POSITIONS
}