package de.trispeedys.resourceplanning.interaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.gravitex.hibernateadapter.entity.AbstractDbObject;
import de.trispeedys.resourceplanning.StringHelper;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.parser.ValueParser;

public class RequestHelper
{
    public static String buildParameterName(AbstractDbObject entity, String paramName)
    {
        return entity.getClass().getSimpleName() + "@" +  entity.getId() + "@" + paramName;
    }

    public static void applyValueChange(Helper helper, String attributeName, String newValue)
    {
        try
        {
            Class<?> type = getParameterType(helper, attributeName);
            Method setter = helper.getClass().getDeclaredMethod("set" + StringHelper.firstToUpper(attributeName), new Class[] {type});
            setter.invoke(helper, new Object[] {ValueParser.parseValue(newValue, type)});
        }
        catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e)
        {
            throw new ResourcePlanningException("unable to apply value '"+newValue+"'!!");
        }
    }

    private static Class<?> getParameterType(Object object, String attributeName) throws NoSuchFieldException, SecurityException
    {
        return object.getClass().getDeclaredField(attributeName).getType();
    }
}