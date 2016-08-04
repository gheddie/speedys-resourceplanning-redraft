package de.trispeedys.resourceplanning;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.entity.Helper;
import de.trispeedys.resourceplanning.factory.EntityCreator;
import de.trispeedys.resourceplanning.repository.HelperRepository;
import de.trispeedys.resourceplanning.util.DbResultExcluder;
import de.trispeedys.resourceplanning.util.TestUtil;

public class ResultExcluderTest
{
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    @Test
    public void testResultExcluderPersistedEntity()
    {
        TestUtil.clearAll();
        
        EntityCreator.createHelper("Schulz", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
        EntityCreator.createHelper("Meier", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
        EntityCreator.createHelper("Petersen", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
        
        List<Helper> helpers = RepositoryProvider.getRepository(HelperRepository.class).findAll();
        
        List<Helper> result = new DbResultExcluder(helpers, helpers.get(0)).getResult();
        assertEquals(2, result.size());
    }
    
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Test
    public void testResultExcluderFreshEntity()
    {
        TestUtil.clearAll();
        
        EntityCreator.createHelper("Schulz", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
        EntityCreator.createHelper("Meier", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
        EntityCreator.createHelper("Petersen", "Stefan", 13,  2,  1976, "", null).saveOrUpdate();
        
        List<Helper> helpers = RepositoryProvider.getRepository(HelperRepository.class).findAll();
        
        List<Helper> result = new DbResultExcluder(helpers, EntityCreator.createHelper("Klausen", "Stefan", 13,  2,  1976, "", null)).getResult();
        assertEquals(3, result.size());
    }
}