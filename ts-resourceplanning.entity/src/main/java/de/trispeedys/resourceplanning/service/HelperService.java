package de.trispeedys.resourceplanning.service;

import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.exception.ResourcePlanningException;
import de.trispeedys.resourceplanning.util.StringUtilMethods;

public class HelperService
{
    public static String createHelperCode(Helper helper)
    {
        if (helper == null)
        {
            throw new ResourcePlanningException("helper must not be NULL!!");
        }
        if ((StringUtilMethods.isBlank(helper.getLastName())) || (helper.getLastName().length() < 2))
        {
            throw new ResourcePlanningException("helpers last name must be at least 2 digits long!!");
        }
        if ((StringUtilMethods.isBlank(helper.getFirstName())) || (helper.getFirstName().length() < 2))
        {
            throw new ResourcePlanningException("helpers first name must be at least 2 digits long!!");
        }
        if (helper.getDateOfBirth() == null)
        {
            throw new ResourcePlanningException("helpers date of birth must be set!!");
        }
        StringBuffer result = new StringBuffer();
        result.append(helper.getLastName()
                .substring(0, 2)
                .toUpperCase()
                .replaceAll("Ä", "A")
                .replaceAll("Ö", "O")
                .replaceAll("Ü", "U"));
        result.append(helper.getFirstName()
                .substring(0, 2)
                .toUpperCase()
                .replaceAll("Ä", "A")
                .replaceAll("Ö", "O")
                .replaceAll("Ü", "U"));
        String dayStr = String.valueOf(helper.getDateOfBirth().getDayOfMonth());
        result.append(dayStr.length() == 2
                ? dayStr
                : "0" + dayStr);
        String monthStr = String.valueOf(helper.getDateOfBirth().getMonthOfYear());
        result.append(monthStr.length() == 2
                ? monthStr
                : "0" + monthStr);
        result.append(helper.getDateOfBirth().getYear());
        return result.toString();
    }
}