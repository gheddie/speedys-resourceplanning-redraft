package de.trispeedys.resourceplanning.interaction.errorhandler;

import javax.servlet.http.HttpServletRequest;

import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.util.html.HtmlGenerator;

public abstract class AbstractRequestErrorHandler
{
    private Throwable error;

    public void setException(Throwable t)
    {
        this.error = t;
    }
    
    protected abstract String generateErrorMessage();
    
    public String renderResult(HttpServletRequest request)
    {
        return new HtmlGenerator().withImage("tri", "jpg", 400, 115)
                .withHeader("ERROR :")
                .withParagraph(ApplicationContext.getText(generateErrorMessage()))
                .withClosingLink()
                .render();
    }
    
    protected Throwable getError()
    {
        return error;
    }
}