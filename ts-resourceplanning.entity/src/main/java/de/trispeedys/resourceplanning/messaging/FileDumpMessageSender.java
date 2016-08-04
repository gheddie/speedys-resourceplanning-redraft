package de.trispeedys.resourceplanning.messaging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.AppConfigurationValues;
import de.trispeedys.resourceplanning.context.ApplicationContext;
import de.trispeedys.resourceplanning.entity.MessageQueueItem;
import de.trispeedys.resourceplanning.repository.AppConfigurationEntryRepository;

public class FileDumpMessageSender implements IMessageSender
{
    public void prepare()
    {
        // delete all files in temp directory (do not use ApplicationContext.getInstance() here as we are still in singleton creation!!)
        String path = RepositoryProvider.getRepository(AppConfigurationEntryRepository.class).findByKey(AppConfigurationValues.DUMP_MESSAGES_PATH, null).getValue();
        for (File actualFile : new File(path).listFiles())
        {
            deleteFile(actualFile);
        }
    }
    
    private void deleteFile(File actualFile)
    {
        if (actualFile.isDirectory())
        {
            for (File file : actualFile.listFiles())
            {
                deleteFile(file);
            }
        }
        actualFile.delete();
    }
    
    public void sendMessage(MessageQueueItem message, boolean sendInitially)
    {
        String fileName = message.getSubject() + "_[TO_" + message.getToAddress() + "]_@_" + System.currentTimeMillis();
        writeTempFile(fileName, message.getBody(), message.getHelper().getCode());
    }

    private static void writeTempFile(String fileName, String content, String subDirectory)
    {
        // TODO use file name utils

        String tempDirectory = ApplicationContext.getInstance().getConfigurationValue(AppConfigurationValues.DUMP_MESSAGES_PATH);
        try
        {
            // directory
            String directoryPath = tempDirectory + "\\" + subDirectory;
            File directory = new File(directoryPath);
            if (!(directory.exists()))
            {
                directory.mkdir();
            }
            String path = directoryPath + "\\" + fileName + ".html";
            File file = new File(path);
            System.out.println(" --- dumping to file --- : " + path);
            if (!(file.exists()))
            {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}