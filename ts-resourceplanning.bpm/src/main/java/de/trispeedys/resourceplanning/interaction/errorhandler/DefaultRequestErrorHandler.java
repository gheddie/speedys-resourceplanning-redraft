package de.trispeedys.resourceplanning.interaction.errorhandler;

public class DefaultRequestErrorHandler extends AbstractRequestErrorHandler
{
    protected String generateErrorMessage()
    {
        return getError().getMessage();
    }
}