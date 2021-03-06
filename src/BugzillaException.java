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
		//login
		INVALID_LOGIN_INFO,
		USER_ALREADY_LOGGED_IN,
		USER_ALREADY_LOGGED_OFF,
		//method
		INVALID_METHOD,
		//developer
		ANOTHER_DEVELOPER_ALREADY_WORKING,
		//username
		USERNAME_NULL,
		INVALID_USERNAME_LENGTH,
		USER_ALREADY_REGISTRED,
		//password
		PASSWD_NULL,
		INVALID_PASSWD_LENGTH,
		//membertype
		USER_ACTION_NOT_PERMITTED,
		INVALID_MEMBER_TYPE,
		//bug id
		INVALID_BUGID,
		//bug description
		INVALID_DESCRIPTION,
		//bug state
		INVALID_STATE_TRANSITION,
		//bug solutionType
		INVALID_SOLUTION_TYPE,
		//bug solutionInfo
		INVALID_SOLUTION_INFO
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
		msgList.put(ErrorType.INVALID_DESCRIPTION, "Error: Invalid description");
		msgList.put(ErrorType.INVALID_METHOD, "Error: Invalid method for operation");
		msgList.put(ErrorType.INVALID_SOLUTION_INFO, "Error: Invalid solution info");
		msgList.put(ErrorType.INVALID_SOLUTION_TYPE, "Error: Invalid solution type");
		msgList.put(ErrorType.INVALID_USERNAME_LENGTH, "Error: Invalid username length");
		msgList.put(ErrorType.PASSWD_NULL, "Error: Object passwd is null");
		msgList.put(ErrorType.INVALID_PASSWD_LENGTH, "Error: Invalid passwd length");
		msgList.put(ErrorType.INVALID_MEMBER_TYPE, "Error: Invalid member type");
		msgList.put(ErrorType.INVALID_LOGIN_INFO, "Error: Invalid login info");
		msgList.put(ErrorType.USER_ALREADY_LOGGED_IN, "Error: User already logged in");
		msgList.put(ErrorType.USER_ALREADY_LOGGED_OFF, "Error: User already logged off");
		msgList.put(ErrorType.ANOTHER_DEVELOPER_ALREADY_WORKING, "Error: Another developer is working on the bug");
		
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
