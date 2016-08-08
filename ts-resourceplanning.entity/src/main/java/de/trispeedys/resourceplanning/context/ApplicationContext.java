package de.trispeedys.resourceplanning.context;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.AppConfigurationEntry;
import de.trispeedys.resourceplanning.messaging.DefaultMessageSender;
import de.trispeedys.resourceplanning.messaging.IMessageSender;
import de.trispeedys.resourceplanning.repository.AppConfigurationEntryRepository;
import de.trispeedys.resourceplanning.util.StringUtil;

public class ApplicationContext
{
    private static final Logger logger = Logger.getLogger(ApplicationContext.class);
    
    private static final String PROPERTIES_NAME = "messages.properties";

    private static ApplicationContext instance;

    private HashMap<String, String> configurationValues;

    private IMessageSender messageSender;
   
    private Properties textResources;

    public static ApplicationContext getInstance()
    {
        if (ApplicationContext.instance == null)
        {
            ApplicationContext.instance = new ApplicationContext();
        }
        return ApplicationContext.instance;
    }

    private ApplicationContext()
    {
        readConfigurationValues();
        init();
    }

    private void init()
    {
        // message sender
        try
        {
            messageSender = (IMessageSender) Class.forName(getConfigurationValue(AppConfigurationValues.MESSAGE_SENDER)).newInstance();
        }
        catch (Exception e)
        { // switch to default configurator on any exception
            messageSender = new DefaultMessageSender();
        }
        finally
        {
            messageSender.prepare();
        }
        
        // test resources
        logger.info("initializing text resources...");

        textResources = new Properties();
        try
        {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_NAME);
            if (stream != null)
            {
                textResources.load(stream);   
            }            
        }
        catch (IOException e)
        {
            logger.error(e);
        }
    }

    private void readConfigurationValues()
    {
        configurationValues = new HashMap<String, String>();
        for (AppConfigurationEntry entry : RepositoryProvider.getRepository(AppConfigurationEntryRepository.class).findAll())
        {
            configurationValues.put(entry.getKey(), entry.getValue());
        }
    }

    public String getConfigurationValue(String key)
    {
        return configurationValues.get(key);
    }
    
    public static String getText(String key)
    {
        return getText(key, null);
    }
    
    public static String getText(String key, Object... parameters)
    {
        return getInstance().getTextResource(key, parameters);
    }

    private String getTextResource(String key, Object... parameters)
    {
        String resource = textResources.getProperty(key);
        if (StringUtil.isBlank(resource))
        {
            return " ### " + key + " ### ";
        }
        logger.debug("got text resource for key [" + key + "] : " + resource);
        MessageFormat mf = new MessageFormat(resource);
        String result = mf.format(parameters);
        return result;
    }

    public IMessageSender getMessageSender()
    {
        return messageSender;
    }
}