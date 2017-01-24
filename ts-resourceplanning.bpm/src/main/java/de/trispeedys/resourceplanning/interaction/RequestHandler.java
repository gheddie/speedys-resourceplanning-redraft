package de.trispeedys.resourceplanning.interaction;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.ProcessEngine;

import de.trispeedys.resourceplanning.interaction.errorhandler.DefaultRequestErrorHandler;
import de.trispeedys.resourceplanning.parser.ValueParser;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.gravitex.hibernateadapter.core.SessionHolder;
import de.gravitex.hibernateadapter.core.SessionManager;
import de.gravitex.hibernateadapter.core.SessionToken;
import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.interaction.enumeration.ServletRequestContext;
import de.trispeedys.resourceplanning.interaction.errorhandler.AbstractRequestErrorHandler;
import de.trispeedys.resourceplanning.util.ServletRequestParameters;
import de.trispeedys.resourceplanning.util.StringUtilMethods;
import de.trispeedys.resourceplanning.util.TestUtil;
import de.trispeedys.resourceplanning.util.html.HtmlGenerator;

public class RequestHandler
{
    public static final String GENERIC_REQUEST_RECEIVER = "GenericRequestReceiver.jsp";

    public static final String GENERIC_CONFIRM_RECEIVER = "GenericConfirmReceiver.jsp";
    
    public static final String HELPER_UPDATE_RECEIVER = "UpdateHelperDataReceiver.jsp";

    private static HashMap<Class<? extends Exception>, AbstractRequestErrorHandler> errorHandlers =
            new HashMap<Class<? extends Exception>, AbstractRequestErrorHandler>();
    static
    {
        // add custom handlers
    }

    public static String processRequest(HttpServletRequest request, ProcessEngine processEngine)
    {
        Long eventId = parseLong(request.getParameter(ServletRequestParameters.EVENT_ID));
        Long helperId = parseLong(request.getParameter(ServletRequestParameters.HELPER_ID));
        switch (ServletRequestContext.valueOf(request.getParameter(ServletRequestParameters.CONTEXT)))
        {
            case LEGACY_POS:
                try
                {
                    boolean takeOver = parseBoolean(request.getParameter(ServletRequestParameters.TAKEOVER_CALLBACK));
                    HelperInteraction.processLegacyPositionTakeover(helperId, eventId, takeOver, processEngine);
                    return getOkResult(takeOver
                            ? ApplicationContext.getText("request.ok.pos.takeover.accepted")
                            : ApplicationContext.getText("request.ok.pos.takeover.declined"));
                }
                catch (Exception e)
                {
                    return getRequestErrorHandler(e).renderResult(request);
                }
            case CALLBACK:
                try
                {
                    HelperCallback helperCallback = HelperCallback.valueOf(request.getParameter(ServletRequestParameters.CALLBACK));
                    Long positionId = parseLong(request.getParameter(ServletRequestParameters.POSITION_ID));
                    HelperInteraction.processHelperCallback(helperCallback, helperId, eventId, positionId, processEngine);
                    String message = null;
                    Position position = RepositoryProvider.getRepository(PositionRepository.class).findById(positionId);
                    switch (helperCallback)
                    {
                        case ADD_POSITION:
                            message = ApplicationContext.getText("request.ok.pos.added", position.getName());
                            break;
                        case REMOVE_POSITION:
                            message = ApplicationContext.getText("request.ok.pos.removed", position.getName());
                            break;
                        case EARMARK_POSITION:
                            message = ApplicationContext.getText("request.ok.pos.earmarked", position.getName());
                            break;
                        case MANUAL_ASSIGNMENT:
                            message = ApplicationContext.getText("request.ok.man.assig");
                            break;
                        case NEVER_AGAIN:
                            message = ApplicationContext.getText("request.ok.never.again");
                            break;
                        case NOT_THIS_TIME:
                            message = ApplicationContext.getText("request.ok.not.this.time");
                            break;
                        default:
                            break;
                    }
                    return getOkResult(message);
                }
                catch (Exception e)
                {
                    return getRequestErrorHandler(e).renderResult(request);
                }
            default:
                // will not occur
                return null;
        }
    }

    private static AbstractRequestErrorHandler getRequestErrorHandler(Exception e)
    {
        AbstractRequestErrorHandler handler = errorHandlers.get(e);
        if (handler == null)
        {
            handler = new DefaultRequestErrorHandler();
        }
        handler.setException(e);
        return handler;
    }

    private static String getOkResult(String message)
    {
        return new HtmlGenerator().withImage("tri", "jpg", 400, 115)
                .withHeader(ApplicationContext.getText("requesthandler.operation.suceeded"))
                .withParagraph(message)
                .withClosingLink()
                .render();
    }

    public static String renderConfirmationForm(HttpServletRequest request, ProcessEngine processEngine)
    {
        Long eventId = parseLong(request.getParameter(ServletRequestParameters.EVENT_ID));
        Long helperId = parseLong(request.getParameter(ServletRequestParameters.HELPER_ID));
        Long positionId = parseLong(request.getParameter(ServletRequestParameters.POSITION_ID));
        boolean takeoverCallback = parseBoolean(request.getParameter(ServletRequestParameters.TAKEOVER_CALLBACK));

        ServletRequestContext requestContext = ServletRequestContext.valueOf(request.getParameter(ServletRequestParameters.CONTEXT));
        switch (requestContext)
        {
            case LEGACY_POS:
                String legacyText = takeoverCallback
                        ? ApplicationContext.getText("request.confirmation.legacy.yes")
                        : ApplicationContext.getText("request.confirmation.legacy.no");
                return new HtmlGenerator().withImage("tri", "jpg", 400, 115)
                        .withHeader("Bestätigung")
                        .withParagraph(legacyText)
                        .withSimpleButtonForm(GENERIC_CONFIRM_RECEIVER, "OK", eventId, helperId, positionId, null, takeoverCallback, requestContext)
                        .render();
            case CALLBACK:
                HelperCallback helperCallback = HelperCallback.valueOf(request.getParameter(ServletRequestParameters.CALLBACK));
                String confirmationText = null;
                Position position = RepositoryProvider.getRepository(PositionRepository.class).findById(positionId);
                switch (helperCallback)
                {
                    case ADD_POSITION:
                        confirmationText = ApplicationContext.getText("request.confirmation.add", position.getName(), position.getDomain().getName());
                        break;
                    case REMOVE_POSITION:
                        confirmationText = ApplicationContext.getText("request.confirmation.remove", position.getName(), position.getDomain().getName());
                        break;
                    case EARMARK_POSITION:
                        confirmationText = ApplicationContext.getText("request.confirmation.earmark", position.getName(), position.getDomain().getName());
                        break;
                    case MANUAL_ASSIGNMENT:
                        confirmationText = ApplicationContext.getText("request.confirmation.man_assig");
                        break;
                    case NEVER_AGAIN:
                        confirmationText = ApplicationContext.getText("request.confirmation.never_again");
                        break;
                    case NOT_THIS_TIME:
                        confirmationText = ApplicationContext.getText("request.confirmation.pause");
                        break;
                    default:
                        break;
                }
                return new HtmlGenerator().withImage("tri", "jpg", 400, 115)
                        .withHeader("Bestätigung")
                        .withParagraph(confirmationText)
                        .withSimpleButtonForm(GENERIC_CONFIRM_RECEIVER, "OK", eventId, helperId, positionId, helperCallback, takeoverCallback,
                                requestContext)
                        .render();
            default:
                // will not occur
                return null;
        }
    }
    
    /**
     * http://localhost:8080/ts-resourceplanning.bpm-0.0.1-SNAPSHOT/UpdateHelperData.jsp?helperId=9737
     * 
     * @param request
     * @param processEngine
     * @return
     */
    public static String renderHelperUpdateForm(HttpServletRequest request, ProcessEngine processEngine)
    {
        Long helperId = parseLong(request.getParameter(ServletRequestParameters.HELPER_ID));
        StringBuffer buffer = new StringBuffer();
        buffer.append("<form action=\"UpdateHelperDataReceiver.jsp\">");
        buffer.append("<table border=\"1\">");
        int i = 0;
        String helperBirthAttribute = null;
        String helperMailAttribute = null;
        for (Helper subordinated : RepositoryProvider.getRepository(HelperRepository.class).findSubordinatedHelpers(helperId, null))
        {
            // build parameter names
            helperBirthAttribute = RequestHelper.buildParameterName(subordinated, HelperRepository.PARAM_DATE_OF_BIRTH);
            helperMailAttribute = RequestHelper.buildParameterName(subordinated, HelperRepository.PARAM_EMAIL);
            
            // add an input for email and birthday
            buffer.append("<tr>");
            buffer.append("<td>"+subordinated.formatName()+"</td><td><input type=\"text\" name=\""+helperBirthAttribute+"\" value=\""+ValueParser.formatValue(subordinated.getDateOfBirth())+"\"></td><td><input type=\"text\" name=\""+helperMailAttribute+"\" value=\""+subordinated.getEmail()+"\"></td>");
            buffer.append("</tr>");
            i++;
        }  
        buffer.append("</table>");
        buffer.append("<input type=\"submit\" value=\"OK\">");
        buffer.append("</form>");
        return buffer.toString();
    }
    
    public static String processUpdateResult(HttpServletRequest request, ProcessEngine processEngine)
    {
        SessionHolder sessionHolder = SessionManager.getInstance().registerSession(RequestHandler.class, null);
        SessionToken sessionToken = sessionHolder.getToken();
        try
        {
            sessionHolder.beginTransaction();
            HashMap<Long, Helper> helpers = new HashMap<>();
            Long helperId = null;
            HelperRepository helperRepository = RepositoryProvider.getRepository(HelperRepository.class);
            for (String key : request.getParameterMap().keySet())
            {
                // get values
                String[] spl = key.split("@");
                helperId = ValueParser.parseValue(spl[1], Long.class);
                // cache helper
                Helper helper = helpers.get(helperId);
                if (helper == null)
                {
                    helpers.put(helperId, helperRepository.findById(helperId));
                }
                helper = helpers.get(helperId);
                RequestHelper.applyValueChange(helper, spl[2], request.getParameterMap().get(key)[0]);
                helper.saveOrUpdate(sessionToken);
            }
            sessionHolder.commitTransaction();
            return "OK";
        }
        catch (Exception e)
        {
            sessionHolder.rollbackTransaction();
            e.printStackTrace();
            return "FAULT : " + e.getMessage();
        }
        finally
        {
            SessionManager.getInstance().unregisterSession(sessionHolder);
        }
    }

    private static boolean parseBoolean(String value)
    {
        // TODO switch to value parser?
        if (StringUtilMethods.isBlank(value))
        {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    private static Long parseLong(String value)
    {
        // TODO switch to value parser?
        if (StringUtilMethods.isBlank(value))
        {
            return null;
        }
        value = value.replaceAll("\\.", "");
        value = value.replaceAll("\\,", "");
        return Long.parseLong(value);
    }
}