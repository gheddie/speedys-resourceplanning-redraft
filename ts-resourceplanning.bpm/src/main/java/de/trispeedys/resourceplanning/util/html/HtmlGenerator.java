package de.trispeedys.resourceplanning.util.html;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.entity.Event;
import de.trispeedys.resourceplanning.entity.EventDay;
import de.trispeedys.resourceplanning.entity.EventPosition;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.interaction.RequestHandler;
import de.trispeedys.resourceplanning.interaction.enumeration.HelperCallback;
import de.trispeedys.resourceplanning.interaction.enumeration.ServletRequestContext;
import de.trispeedys.resourceplanning.repository.EventDayRepository;
import de.trispeedys.resourceplanning.repository.EventPositionRepository;
import de.trispeedys.resourceplanning.util.DateHelperMethods;
import de.trispeedys.resourceplanning.util.LoadFactor;
import de.trispeedys.resourceplanning.util.ServletRequestParameters;

public class HtmlGenerator
{
    private static final String COLOR_OFF = "#ffffff";

    private static final String COLOR_ON_ASSIGNED = "#ff0000";

    private static final String COLOR_ON_UNASSIGNED = "#cccccc";

    private static final String COLOR_ON_SATURATED = "#00ff00";

    public static final String MACHINE_MESSAGE = "machineMessage";

    private StringBuffer buffer;

    private boolean renderNoReply;

    private enum RowOption
    {
        ASSIGNABLE,
        CANCELABLE,
        NONE,
        COMPLETELY_BOOKED
    }

    public HtmlGenerator(boolean aRenderNoReply, boolean useCss)
    {
        super();
        this.renderNoReply = aRenderNoReply;
        this.buffer = new StringBuffer();
        buffer.append("<html>");
        if (useCss)
        {
            buffer.append("<head>");
            buffer.append("<title></title>");
            buffer.append("<style type=\"text/css\">");
            // body
            buffer.append("body");
            buffer.append("{");
            buffer.append("color: purple;");
            buffer.append("background-color: #ffffff");
            buffer.append("}");
            // h1
            buffer.append("table");
            buffer.append("{");
            buffer.append("color: #333;");
            buffer.append("font-family: Helvetica, Arial, sans-serif;");
            buffer.append("}");
            // table
            buffer.append("table");
            buffer.append("{");
            buffer.append("color:#333;*/");
            buffer.append("font-family:Helvetica,Arial,sans-serif;");
            buffer.append("width:1080px;");
            buffer.append("border-collapse:");
            buffer.append("collapse;border-spacing:0;");
            buffer.append("}");
            // td, th
            buffer.append("td, th {border: 1px solid #CCC; height: 30px;}");
            buffer.append("th");
            buffer.append("{");
            buffer.append("background: #F3F3F3;");
            buffer.append("font-weight: bold;");
            buffer.append("}");
            buffer.append("td");
            buffer.append("{");
            buffer.append("background: #FAFAFA;");
            buffer.append("text-align: center;");
            buffer.append("}");
            buffer.append("</style>");
            buffer.append("</head>");
        }

    }

    public HtmlGenerator()
    {
        this(false, false);
    }

    /**
     * referred to images must be placed in the /src/main/webapp/img directory.
     * 
     * @param filename
     * @param suffix
     * @param width
     * @param height
     * 
     * @return
     */
    public HtmlGenerator withImage(String filename, String suffix, int width, int height)
    {
        MessageFormat mf = new MessageFormat("<img src=\"img/{0}.{1}\" width=\"{2}\" height=\"{3}\" align=\"middle\" class=\"centeredImageContainer\">");
        buffer.append(mf.format(new Object[]
        {
                filename, suffix, width, height
        }));
        newLine();
        return this;
    }

    public HtmlGenerator withHeader(String text)
    {
        buffer.append("<h1>" + text + "</h1>");
        newLine();
        return this;
    }

    public HtmlGenerator withLinebreak()
    {
        return withLinebreak(1);
    }

    public HtmlGenerator withLinebreak(int howMany)
    {
        for (int i = 0; i < howMany; i++)
        {
            buffer.append("<br>");
            newLine();
        }
        return this;
    }

    public HtmlGenerator withList(List<String> items, boolean ordered)
    {
        String tag = (ordered
                ? "ol"
                : "ul");
        buffer.append("<" + tag + ">");
        for (String item : items)
        {
            buffer.append("<li>" + item + "</li>");
        }
        buffer.append("</" + tag + ">");
        return this;
    }

    public HtmlGenerator withParagraph(String text)
    {
        buffer.append("<p>" + text + "</p>");
        newLine();
        return this;
    }

    public HtmlGenerator withDiv(String text)
    {
        buffer.append("<div>" + text + "</div>");
        newLine();
        return this;
    }

    public HtmlGenerator withClosingLink()
    {
        // TODO how to do it ?

        buffer.append("<a href=\"#\" onclick=\"javascript:window.close();\">" + ApplicationContext.getText("app.close") + "</a>");
        // buffer.append("<a href=\"javascript:window.close()\">" + ApplicationContext.getText("app.close") + "</a>");
        newLine();
        return this;
    }

    public HtmlGenerator withLink(String link, String displayText)
    {
        MessageFormat mf = new MessageFormat("<a href=\"{0}\">{1}</a>");
        buffer.append(mf.format(new Object[]
        {
                link, displayText
        }));
        newLine();
        return this;
    }

    public HtmlGenerator withTextAreaInputForm(String target, int rows, int columns, String buttonText, Long eventId, Long helperId)
    {
        // TODO improve css to render commit button centered, too...
        MessageFormat mf = new MessageFormat("<form action =\"{0}\"><textarea name=\"helperMessage\" rows=\"{1}\" cols=\"{2}\">" +
                "</textarea>" + "<input type=\"submit\" value=\"{3}\"><input type=\"hidden\" name=\"eventId\" value=\"{4}\">" +
                "<input type=\"hidden\" name=\"helperId\" value=\"{5}\">" + "</form>");
        buffer.append(mf.format(new Object[]
        {
                target, rows, columns, buttonText, eventId, helperId
        }));
        newLine();
        return this;
    }

    /**
     * renders out a form with hidden parameters eventId + helperId.
     * 
     * @param target
     * @param buttonText
     * @param eventId
     * @param helperId
     * @param helperCallback
     * @param positionId
     * @param takeoverCallback
     * @return
     */
    public HtmlGenerator withSimpleButtonForm(String target, String buttonText, Long eventId, Long helperId, Long positionId,
            HelperCallback helperCallback, boolean takeoverCallback, ServletRequestContext requestContext)
    {
        MessageFormat mf = new MessageFormat("<form action =\"{0}\">" +
                "<input type=\"submit\" value=\"{1}\">" + "<input type=\"hidden\" name=\"{2}\" value=\"{3}\">" +
                "<input type=\"hidden\" name=\"{4}\" value=\"{5}\">" + "<input type=\"hidden\" name=\"{6}\" value=\"{7}\">" +
                "<input type=\"hidden\" name=\"{8}\" value=\"{9}\">" + "<input type=\"hidden\" name=\"{10}\" value=\"{11}\">" +
                "<input type=\"hidden\" name=\"{12}\" value=\"{13}\">" + "</form>");
        buffer.append(mf.format(new Object[]
        {
                target,
                buttonText,
                ServletRequestParameters.EVENT_ID,
                eventId,
                ServletRequestParameters.HELPER_ID,
                helperId,
                ServletRequestParameters.POSITION_ID,
                positionId,
                ServletRequestParameters.CALLBACK,
                helperCallback,
                ServletRequestParameters.TAKEOVER_CALLBACK,
                takeoverCallback,
                ServletRequestParameters.CONTEXT,
                requestContext
        }));
        newLine();
        return this;
    }

    public void withBookingOverview(Event event, Helper helper, int tableWidth, String baseLink, List<Position> assignedPositions,
            List<Position> helperAssignablePositions)
    {
        Set<Long> assignedPositionsSet = new HashSet<Long>();
        for (Position assignedPosition : assignedPositions)
        {
            assignedPositionsSet.add(assignedPosition.getId());
        }
        Set<Long> helperAssignablePositionsSet = new HashSet<Long>();
        for (Position helperAssignablePosition : helperAssignablePositions)
        {
            helperAssignablePositionsSet.add(helperAssignablePosition.getId());
        }
        buffer.append("<table border=\"0\">");
        buffer.append("<tr>");
        buffer.append("<th>Tag</th>");
        buffer.append("<th>Position</th>");
        buffer.append("<th>von</th>");
        buffer.append("<th>bis</th>");
        for (int hour = 0; hour <= 21; hour += 3)
        {
            buffer.append("<th align=\"left\" colspan=\"3\" width=\"120\">" + hour + "</th</tr>");
        }
        buffer.append("<th align=\"left\" colspan=\"1\" width=\"50\">Auslastung</th</tr>");
        buffer.append("<th align=\"left\" colspan=\"1\" width=\"200\">Aktion</th</tr>");
        buffer.append("</tr>");
        RowOption rowOption = null;

        boolean firstRowOfEventDay = false;

        for (EventDay eventDay : RepositoryProvider.getRepository(EventDayRepository.class).findByEvent(event, null))
        {
            firstRowOfEventDay = true;

            for (EventPosition eventPosition : RepositoryProvider.getRepository(EventPositionRepository.class).findByEventDay(eventDay, null))
            {
                if (assignedPositionsSet.contains(eventPosition.getPosition().getId()))
                {
                    // assigned
                    rowOption = RowOption.CANCELABLE;
                }
                else if (helperAssignablePositionsSet.contains(eventPosition.getPosition().getId()))
                {
                    // to be assigned (if not completely booked)
                    if (eventPosition.isCompletelyBooked())
                    {
                        rowOption = RowOption.COMPLETELY_BOOKED;
                    }
                    else
                    {
                        rowOption = RowOption.ASSIGNABLE;
                    }
                }
                else
                {
                    // none
                    rowOption = RowOption.NONE;
                }
                buildPositionRow(eventDay, helper, baseLink, eventPosition, rowOption, firstRowOfEventDay);
                firstRowOfEventDay = false;
            }
        }

        buffer.append("</table>");
    }

    private void buildPositionRow(EventDay eventDay, Helper helper, String baseLink, EventPosition eventPosition, RowOption rowOption,
            boolean firstRowOfEventDay)
    {
        Position position = eventPosition.getPosition();

        buffer.append("<tr>");
        buffer.append(firstRowOfEventDay
                ? "<td nowrap=\"true\">" + DateHelperMethods.formatLocalDate(eventDay.getPlannedDate()) + "</td>"
                : "<td nowrap=\"true\">&nbsp;</td>");
        buffer.append("<td nowrap=\"true\">" + position.getName() + "</td>");
        buffer.append("<td>" + eventPosition.getHourOfStart() + "</td>");
        buffer.append("<td>" + eventPosition.getHourOfEnd() + "</td>");
        String colorStr = "123456";
        for (int hour = 0; hour <= 23; hour++)
        {
            switch (rowOption)
            {
                case ASSIGNABLE:
                    colorStr = eventPosition.includesHourOfDay(hour)
                            ? COLOR_ON_UNASSIGNED
                            : COLOR_OFF;
                    break;
                case CANCELABLE:
                    colorStr = eventPosition.includesHourOfDay(hour)
                            ? COLOR_ON_ASSIGNED
                            : COLOR_OFF;
                    break;
                case COMPLETELY_BOOKED:
                    colorStr = eventPosition.includesHourOfDay(hour)
                            ? COLOR_ON_SATURATED
                            : COLOR_OFF;
                    break;
                case NONE:
                    colorStr = COLOR_OFF;
                    break;
            }
            buffer.append("<td colspan=\"1\" style=\"background-color:" + colorStr + ";\">&nbsp;</td>");
        }
        LoadFactor loadFactor = eventPosition.getLoadFactor();
        buffer.append("<td>" + loadFactor.getNumerator() + "/" + loadFactor.getDenominator() + "</td>");
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ServletRequestParameters.EVENT_ID, eventDay.getEvent().getId());
        parameters.put(ServletRequestParameters.HELPER_ID, helper.getId());
        parameters.put(ServletRequestParameters.POSITION_ID, position.getId());
        parameters.put(ServletRequestParameters.CONTEXT, ServletRequestContext.CALLBACK);
        String link = null;
        String linkText = "";
        String domainName = eventPosition.getPosition().getName();
        String positionName = eventPosition.getPosition().getDomain().getName();
        switch (rowOption)
        {
            case ASSIGNABLE:
                parameters.put(ServletRequestParameters.CALLBACK, HelperCallback.ADD_POSITION);
                link = new LinkGenerator(baseLink, RequestHandler.GENERIC_REQUEST_RECEIVER, parameters).generate();
                linkText = ApplicationContext.getText("helpercallback.linkname.book");
                break;
            case CANCELABLE:
                parameters.put(ServletRequestParameters.CALLBACK, HelperCallback.REMOVE_POSITION);
                link = new LinkGenerator(baseLink, RequestHandler.GENERIC_REQUEST_RECEIVER, parameters).generate();
                linkText = ApplicationContext.getText("helpercallback.linkname.cancel");
                break;
            case COMPLETELY_BOOKED:
                parameters.put(ServletRequestParameters.CALLBACK, HelperCallback.EARMARK_POSITION);
                link = new LinkGenerator(baseLink, RequestHandler.GENERIC_REQUEST_RECEIVER, parameters).generate();
                linkText = ApplicationContext.getText("helpercallback.linkname.earmark");
                break;
            case NONE:
                link = null;
                break;
        }
        if (link != null)
        {
            buffer.append("<td><a href=\"" + link + "\">" + linkText + "</a></td>");
        }
        else
        {
            buffer.append("<td>&nbsp;</td>");
        }
        buffer.append("</tr>");
    }

    private void withMachineMessage()
    {
        /*
         * withParagraph(AppConfiguration.getInstance().getText(HtmlGenerator.MACHINE_MESSAGE));
         */
    }

    private void newLine()
    {
        buffer.append("\n");
    }

    public String render()
    {
        if (renderNoReply)
        {
            withMachineMessage();
        }
        buffer.append("</html>");
        return buffer.toString();
    }
}