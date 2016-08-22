package de.trispeedys.resourceplanning.context;

public class BpmVariables
{   
    public class MainProcess
    {
        public static final String VAR_HELPER_CALLBACK = "helperCallback";

        public static final String VAR_HELPER_ID = "helperId";

        public static final String VAR_EVENT_ID = "eventId";

        // the id of the position the helper wants to be booked to
        public static final String VAR_BOOK_POS_ID = "bookPosId";

        // the id of the position the helper wants to be removed from
        public static final String VAR_REMOVE_POS_ID = "removePosId";
        
        public static final String VAR_EARMARK_POS_ID = "earmarkPosId";

        // from which context was delegate 'SendHelperMailDelegate' accessed?
        public static final String VAR_REMINDER_TYPE = "reminderType";
        
        public static final String VAR_LEGACY_POS_AVAILABLE = "legacyPosAvailable";
        
        // the list of legacy position ids the helper potentially overtakes in a follow up event 
        public static final String VAR_LEGACY_POS_ID_LIST = "legacyPosIdList";
        
        // did the helper decide to take over his legacy positions? 
        public static final String VAR_DO_TAKEOVER_LEGACY_POS = "doTakeoverLegacyPos";   
    }
    
    public class EarmarkProcess
    {
        public static final String VAR_EARMARK_FOUND = "earmarkFound";
    }
}