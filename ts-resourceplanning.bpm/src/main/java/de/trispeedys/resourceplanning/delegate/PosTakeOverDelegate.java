package de.trispeedys.resourceplanning.delegate;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import de.gravitex.hibernateadapter.core.repository.RepositoryProvider;
import de.trispeedys.resourceplanning.context.BpmVariables;
import de.trispeedys.resourceplanning.entity.Position;
import de.trispeedys.resourceplanning.repository.PositionRepository;
import de.trispeedys.resourceplanning.service.AssignmentService;

public class PosTakeOverDelegate extends AbstractResourcePlanningMasterProcessDelegate
{
    public void execute(DelegateExecution execution) throws Exception
    {
        List<Long> legacyPosIdList = (List<Long>) execution.getVariable(BpmVariables.MainProcess.VAR_LEGACY_POS_ID_LIST);
        Position position = null;
        PositionRepository repository = RepositoryProvider.getRepository(PositionRepository.class);
        for (Long legacypositionId : legacyPosIdList)
        {
            position = repository.findById(legacypositionId);
            AssignmentService.bookHelper(getEvent(execution), position, getHelper(execution), null);
        }
    }
}