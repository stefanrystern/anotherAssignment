import java.util.HashMap;

import com.google.java.contract.Invariant;

@Invariant({
	"true"
})
public class BugzillaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum ErrorType {
		UNKNOWN_ERROR,
		DB_LOAD_ERROR,
		DB_SAVE_ERROR,
		ERROR_MISSING_MESSAGE,
		INVALID_STATE_TRANSITION,
		
		USERNAME_NULL,
		USER_ALREADY_REGISTRED,
		INVALID_BUGID,
		USER_ACTION_NOT_PERMITTED
		//...
	}
	
	private static void addMessage() {

		msgList.put(ErrorType.UNKNOWN_ERROR, "Unknown error");
		msgList.put(ErrorType.DB_LOAD_ERROR, "Error: Failed to load database");
		msgList.put(ErrorType.DB_SAVE_ERROR, "Error: Failed to save database");
		msgList.put(ErrorType.ERROR_MISSING_MESSAGE, "Error: Missing error message in exception");
		msgList.put(ErrorType.INVALID_STATE_TRANSITION, "Error: Bug state cannot be changed from %s to %s");
		
		msgList.put(ErrorType.USERNAME_NULL, "Error: Object username is null");
		msgList.put(ErrorType.USER_ALREADY_REGISTRED, "User exists with this username");
		msgList.put(ErrorType.INVALID_BUGID, "Error: Invalid bug ID");
		msgList.put(ErrorType.USER_ACTION_NOT_PERMITTED, "User does not have permission for this action");
		//...
	}
	
	public static void init() throws BugzillaException {
		
		addMessage();
		
		if(!exInitialized()) {
			throw new BugzillaException(ErrorType.ERROR_MISSING_MESSAGE);
		}
	}
	
	public static boolean exInitialized() {
		return (msgList.size() == ErrorType.values().length && !msgList.containsValue(null));
	}
	
	
	public BugzillaException(ErrorType et) {
		error = et;
		msg = msgList.get(error);
	}
	
	public ErrorType getError() {
		return error;
	}
	
	public String getErrorMsg() {
		return msg;
	}
	
	protected ErrorType error;
	protected String msg;
	protected static HashMap<ErrorType, String> msgList = new HashMap<ErrorType,String>();
}
