import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;

public class Bugzilla implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum MemberType {
		SYSTEMANALYST, QUALITYASSURANCE, DEVELOPER, USER
	}

	@Requires({ 
			"username != null", "username.length()>0", "username.length()<20", 
			"passwd != null", "passwd.length()>0", "passwd.length()<20",
			"type == MemberType.SYSTEMANALYST || type == MemberType.QUALITYASSURANCE || type == MemberType.DEVELOPER || type == MemberType.USER",
			"isRegistered(username) == false" })
	@Ensures({ "isRegistered(old(username)) == true", })
	public void register(String username, String passwd, MemberType type) throws BugzillaException {

		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}

		if (isRegistered(username)) {
			throwBex(BugzillaException.ErrorType.USER_ALREADY_REGISTRED);
		}
		
		if(username.length()<=0 || username.length()>=20){
			throwBex(BugzillaException.ErrorType.INVALID_USERNAME_LENGTH);
		}
		
		if (passwd == null) {
			throwBex(BugzillaException.ErrorType.PASSWD_NULL);
		}
		
		if (passwd.length()<=0 || passwd.length()>=20) {
			throwBex(BugzillaException.ErrorType.INVALID_PASSWD_LENGTH);
		}
		
		if(type != MemberType.SYSTEMANALYST || type != MemberType.QUALITYASSURANCE || type != MemberType.DEVELOPER || type != MemberType.USER){
			throwBex(BugzillaException.ErrorType.INVALID_MEMBER_TYPE);
		}
		
		

		members.put(username, getMember(passwd, type));
	}

	@Requires({ 
		"username != null", 
		"passwd != null",
		"passwd == getPasswd(username)", 
		"isLoggedIn(username) == false",
		})
	@Ensures({ "isLoggedIn(old(username)) == true", })
	public void login(String username, String passwd) throws BugzillaException {
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (passwd == null) {
			throwBex(BugzillaException.ErrorType.PASSWD_NULL);
		}
		if (passwd != getPasswd(username)) {
			throwBex(BugzillaException.ErrorType.INVALID_LOGIN_INFO);
		}
		if (isLoggedIn(username)) {
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_IN);
		}
		
		loggedIn.add(username);
	}

	@Requires({ 
		"username != null", 
		"isLoggedIn(username) == true", 
		})
	@Ensures({ "isLoggedIn(old(username)) == false", })

	public void logout(String username) throws BugzillaException {
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		loggedIn.remove(username);
	}

	@Requires({ 
			"username != null", 
			"isLoggedIn(username) == true", 
			"getType(username) == MemberType.USER",
			"description != null", 
			"description.length()>0",

	})
	@Ensures({ 
			"bugCount() == old(bugCount()) + 1", 
			"getBug(lastBugID()).getBugDescription().equals(old(description))",
			"getBug(lastBugID()).getID() == bugs.size()-1", 
			})
	/*
	 * The method allows a USER to submit a new bug
	 */
	public void submitBug(String username, String description) throws BugzillaException {

		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}

		if (getType(username) != MemberType.USER) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		
		if(description == null || description.length()<=0){
			throwBex(BugzillaException.ErrorType.INVALID_DESCRIPTION);
		}

		int bugID = bugs.size();
		bugs.put(bugID, new Bug(bugID, description));
	}

	/*
	 * The method allows a SYSTEMANALYST to confirm a bug
	 */
	@Requires({ 
			"username != null", 
			"isLoggedIn(username) == true",
			"getType(username) == MemberType.SYSTEMANALYST",
			"bugID < bugs.size()", 
			"bugID >= 0",
			"bugExists(bugID) == true",
			"getBug(bugID).getState() == Bug.State.UNCONFIRMED",
			"getBug(bugID).getState() != Bug.State.VERIFIED",
	})
	@Ensures({ 
			"getBug(old(bugID)).getState() == Bug.State.CONFIRMED",
	})
	public void confirmBug(String username, int bugID) throws BugzillaException {

		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (bugID >= bugs.size() || bugID < 0 || !bugExists(bugID)) {
			throwBex(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (getType(username) != MemberType.SYSTEMANALYST) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		if (getBug(bugID).getState() != Bug.State.UNCONFIRMED) {
			throw new BugStateException(Bug.State.CONFIRMED, getBug(bugID).getState());
		}
		if (getBug(bugID).getState() == Bug.State.VERIFIED) {
			throw new BugStateException(Bug.State.CONFIRMED, Bug.State.VERIFIED);
		}
		getBug(bugID).setState(Bug.State.CONFIRMED);
	}

	/*
	 * The method allows a SYSTEMANALYST to invalidate a bug
	 */
	@Requires({ 
			"username != null",
			"bugID < bugs.size()", 
			"bugID >= 0",
			"solution != null",
			"isLoggedIn(username) == true",
			"getType(username) == MemberType.SYSTEMANALYST",
			"bugExists(bugID) == true",
			"getBug(bugID).getState() == Bug.State.UNCONFIRMED",
			"getBug(bugID).getState() != Bug.State.VERIFIED",
	})
	@Ensures({ 
			"getBug(old(bugID)).getState() == Bug.State.RESOLVED",
			"getBug(old(bugID)).getSolutionType() == Bug.Resolution.INVALID",
			"getBug(old(bugID)).getSolutionInfo() == old(solution)"
	})
	public void invalidateBug(String username, int bugID, String solution) throws BugzillaException {
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (bugID >= bugs.size() || bugID < 0 || !bugExists(bugID)) {
			throwBex(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (getType(username) != MemberType.SYSTEMANALYST) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		if (getBug(bugID).getState() != Bug.State.UNCONFIRMED) {
			throw new BugStateException(Bug.State.RESOLVED, getBug(bugID).getState());
		}
		if (getBug(bugID).getState() == Bug.State.VERIFIED) {
			throw new BugStateException(Bug.State.RESOLVED, Bug.State.VERIFIED);
		}
		getBug(bugID).setAsResolved(Bug.Resolution.INVALID, solution);
	}

	/*
	 * The method allows a DEVELOPER to start working on the bug
	 */
	@Requires({ 
			"username != null",
			"bugID < bugs.size()", 
			"bugID >= 0",
			"isLoggedIn(username) == true",
			"getType(username) == MemberType.DEVELOPER",
			"bugExists(bugID) == true",
			"getBug(bugID).getState() == Bug.State.CONFIRMED",
			"getBug(bugID).getState() != Bug.State.VERIFIED",
	})
	@Ensures({ 
			"getBug(old(bugID)).getState() == Bug.State.INPROGRESS",
			"devInProgress(old(username), old(bugID)) == true",
	})
	public void startDevelopment(String username, int bugID) throws BugzillaException {
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (bugID >= bugs.size() || bugID < 0 || !bugExists(bugID)) {
			throwBex(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (getType(username) != MemberType.DEVELOPER) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		if (getBug(bugID).getState() != Bug.State.CONFIRMED) {
			throw new BugStateException(Bug.State.INPROGRESS, getBug(bugID).getState());
		}
		if (getBug(bugID).getState() == Bug.State.VERIFIED) {
			throw new BugStateException(Bug.State.INPROGRESS, Bug.State.VERIFIED);
		}
		
		getBug(bugID).setState(Bug.State.INPROGRESS);
		inProgress.put(username, bugID);
	}

	/*
	 * The method allows a DEVELOPER to stop working on the bug
	 */
	@Requires({ 
			"username != null",
			"bugID < bugs.size()", 
			"isLoggedIn(username) == true",
			"getType(username) == MemberType.DEVELOPER",
			"bugExists(bugID) == true",
			"getBug(bugID).getState() == Bug.State.INPROGRESS",
			"devInProgress(username, bugID) == true",
			"getBug(bugID).getState() != Bug.State.VERIFIED",
	})
	@Ensures({ 
			"getBug(old(bugID)).getState() == Bug.State.CONFIRMED",
			"devInProgress(old(username), old(bugID)) == false",
	})
	public void stopDevelopment(String username, int bugID) throws BugzillaException {
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (bugID >= bugs.size() || bugID < 0 || !bugExists(bugID)) {
			throwBex(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (getType(username) != MemberType.DEVELOPER) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		if (getBug(bugID).getState() != Bug.State.INPROGRESS) {
			throw new BugStateException(Bug.State.CONFIRMED, getBug(bugID).getState());
		}
		if (getBug(bugID).getState() == Bug.State.VERIFIED) {
			throw new BugStateException(Bug.State.CONFIRMED, Bug.State.VERIFIED);
		}
		if (!devInProgress(username, bugID)) {
			throwBex(BugzillaException.ErrorType.ANOTHER_DEVELOPER_ALREADY_WORKING);
		}
		
		getBug(bugID).setState(Bug.State.CONFIRMED);
		inProgress.remove(username);
	}

	/*
	 * The method allows DEVELOPER to mark the bug as fixed
	 */
	@Requires({ 
			"username != null",
			"resType != null",
			"solution !=null",
			"bugID < bugs.size()", 
			"bugID >= 0",
			"isLoggedIn(username) == true",
			"getType(username) == MemberType.DEVELOPER",
			"bugExists(bugID) == true",
			"getBug(bugID).getState() == Bug.State.INPROGRESS",
			"devInProgress(username, bugID) == true",
			"getBug(bugID).getState() != Bug.State.VERIFIED",
	})
	@Ensures({ 
			"getBug(old(bugID)).getState() == Bug.State.RESOLVED",
			"devInProgress(old(username), old(bugID)) == false",
			"getBug(old(bugID)).getSolutionInfo() == old(solution)",
			"getBug(old(bugID)).getSolutionType() == old(resType)",
	})
	public void fixedBug(String username, int bugID, Bug.Resolution resType, String solution) throws BugzillaException {
		if (resType == null) {
			throwBex(BugzillaException.ErrorType.INVALID_SOLUTION_TYPE);
		}
		if (solution == null) {
			throwBex(BugzillaException.ErrorType.INVALID_SOLUTION_INFO);
		}
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (bugID >= bugs.size() || bugID < 0 || !bugExists(bugID)) {
			throwBex(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (getType(username) != MemberType.DEVELOPER) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		if (getBug(bugID).getState() != Bug.State.INPROGRESS) {
			throw new BugStateException(Bug.State.RESOLVED, getBug(bugID).getState());
		}
		if (getBug(bugID).getState() == Bug.State.VERIFIED) {
			throw new BugStateException(Bug.State.RESOLVED, Bug.State.VERIFIED);
		}
		if (!devInProgress(username, bugID)) {
			throwBex(BugzillaException.ErrorType.ANOTHER_DEVELOPER_ALREADY_WORKING);
		}
		
		getBug(bugID).setAsResolved(resType, solution);
		inProgress.remove(username);
	}

	/*
	 * The method allows QUALITYASSURANCE to approve the fix (VERIFY)
	 */
	@Requires({ 
			"username != null",
			"bugID < bugs.size()", 
			"isLoggedIn(username) == true",
			"getType(username) == MemberType.QUALITYASSURANCE",
			"bugExists(bugID) == true",
			"getBug(bugID).getState() == Bug.State.RESOLVED",
			"getBug(bugID).getState() != Bug.State.VERIFIED",
	})
	@Ensures({ 
			"getBug(old(bugID)).getState() == Bug.State.VERIFIED",
			
	})
	public void approveFix(String username, int bugID) throws BugzillaException {
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (bugID >= bugs.size() || bugID < 0 || !bugExists(bugID)) {
			throwBex(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (getType(username) != MemberType.QUALITYASSURANCE) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		if (getBug(bugID).getState() != Bug.State.RESOLVED) {
			throw new BugStateException(Bug.State.VERIFIED, getBug(bugID).getState());
		}
		if (getBug(bugID).getState() == Bug.State.VERIFIED) {
			throw new BugStateException(Bug.State.VERIFIED, Bug.State.VERIFIED);
		}
		if (!devInProgress(username, bugID)) {
			throwBex(BugzillaException.ErrorType.ANOTHER_DEVELOPER_ALREADY_WORKING);
		}
		
		getBug(bugID).setState(Bug.State.VERIFIED);
	}

	/*
	 * The method allows QUALITYASSURANCE to reject the bug (back to CONFIRMED)
	 */
	@Requires({ 
			"username != null",
			"bugID < bugs.size()", 
			"bugID >= 0",
			"isLoggedIn(username) == true",
			"getType(username) == MemberType.QUALITYASSURANCE",
			"bugExists(bugID) == true",
			"getBug(bugID).getState() == Bug.State.RESOLVED",
			"getBug(bugID).getState() != Bug.State.VERIFIED",
	})
	@Ensures({ 
			"getBug(old(bugID)).getState() == Bug.State.CONFIRMED",
			
	})
	public void rejectFix(String username, int bugID) throws BugzillaException {
		if (username == null) {
			throwBex(BugzillaException.ErrorType.USERNAME_NULL);
		}
		if (bugID >= bugs.size() || bugID < 0 || !bugExists(bugID)) {
			throwBex(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (getType(username) != MemberType.QUALITYASSURANCE) {
			throwBex(BugzillaException.ErrorType.USER_ACTION_NOT_PERMITTED);
		}
		if(!isLoggedIn(username)){
			throwBex(BugzillaException.ErrorType.USER_ALREADY_LOGGED_OFF);
		}
		if (getBug(bugID).getState() != Bug.State.RESOLVED) {
			throw new BugStateException(Bug.State.CONFIRMED, getBug(bugID).getState());
		}
		if (getBug(bugID).getState() == Bug.State.VERIFIED) {
			throw new BugStateException(Bug.State.CONFIRMED, Bug.State.VERIFIED);
		}
		if (!devInProgress(username, bugID)) {
			throwBex(BugzillaException.ErrorType.ANOTHER_DEVELOPER_ALREADY_WORKING);
		}
		
		getBug(bugID).setState(Bug.State.CONFIRMED);
	}

	/*
	 * Method for throwing exception
	 */
	public static void throwBex(BugzillaException.ErrorType type) throws BugzillaException {
		throw new BugzillaException(type);
	}

	//////////////////////////////////////////////////////////////////////////////////////
	/*
	 * The following private methods can be used for the task
	 */

	private MemberType getType(String username) {
		return members.get(username).getRight();

	}

	private String getPasswd(String username) {
		return members.get(username).getLeft();
	}

	private boolean isRegistered(String username) {
		return members.containsKey(username);

	}

	private boolean isLoggedIn(String username) {
		return loggedIn.contains(username);

	}

	private boolean bugExists(int bugID) {
		return bugs.containsKey(bugID);

	}

	private int bugCount() {
		return bugs.size();
	}

	/*
	 * The method returns the Id of the bug that was created latest
	 */
	private int lastBugID() {
		return (bugs.size() - 1);

	}

	/*
	 * The method returns the Bug object with a given bug ID
	 */
	private Bug getBug(int bugID) {
		return bugs.get(bugID);

	}

	/*
	 * The method checks if a developer is already assigned to a bug. When a
	 * developer changes the state of an object to INPROGRESS, then s/he is
	 * consider assigned to the bug. When the state of the bug is changed by the
	 * same developer to CONFIRMED (stop working) or RESOLVED (fixed bug) then
	 * s/he is not considered to be assigned.
	 */

	private boolean isDeveloperAssigned(String username) {
		return inProgress.containsKey(username);

	}

	/*
	 * The method checks if a developer is is assigned to a specific bug ID
	 */

	private boolean devInProgress(String username, int bugID) {
		return (inProgress.get(username) == bugID);
	}

	///////////////////////////////////////////////////////////////////////////////////////
	/*
	 * The following methods are not relevant for the assignment task
	 */

	@Ensures({ "exceptionsInitialized() == true", "dataInitialised() == true", "fileEnabled == old(saveToFile)",
			"fileEnabled? fileExists() == true: true" })
	/*
	 * The constructor initializes the loads and initializes the data The file
	 * operations are enabled only if saveToFile is true.
	 */
	public Bugzilla(boolean saveToFile) throws BugzillaException {

		fileEnabled = saveToFile;
		BugzillaException.init();

		loggedIn = new ArrayList<String>();

		if (!fileEnabled) {
			bugs = new HashMap<Integer, Bug>();
			members = new HashMap<String, Pair<String, MemberType>>();
			inProgress = new HashMap<String, Integer>();
		} else {
			try {
				loadDB();
			} catch (Exception e1) {
				if (fileExists()) {
					File f = new File(filePath);
					if (!f.delete()) {
						throwBex(BugzillaException.ErrorType.DB_LOAD_ERROR);
					}
				}

				bugs = new HashMap<Integer, Bug>();
				members = new HashMap<String, Pair<String, MemberType>>();
				inProgress = new HashMap<String, Integer>();

				try {
					saveDB();
				} catch (Exception e2) {
					if (fileExists()) {
						File f = new File(filePath);
						f.delete();
					}
					throwBex(BugzillaException.ErrorType.DB_LOAD_ERROR);
				}
			}
		}

	}

	@Ensures({ "isCopyOf(result) == true" })
	public Map<Integer, Bug> getBugList() {
		return Collections.unmodifiableMap(bugs);
	}

	private Pair<String, MemberType> getMember(String passwd, MemberType type) {
		return new ImmutablePair<String, MemberType>(passwd, type);
	}

	private boolean isCopyOf(Map<Integer, Bug> map) {
		return map.equals(bugs);
	}

	public void saveData() throws BugzillaException {
		if (fileEnabled) {
			try {
				saveDB();
			} catch (Exception ex) {
				ex.printStackTrace();
				throwBex(BugzillaException.ErrorType.DB_SAVE_ERROR);

			}
		}
	}

	private void saveDB() throws Exception {
		try {
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);

			out.writeObject(members);
			out.writeObject(bugs);
			out.writeObject(inProgress);

			out.close();
			fileOut.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;

		}

	}

	@SuppressWarnings("unchecked")
	private void loadDB() throws Exception {

		try {
			FileInputStream fileIn = new FileInputStream(filePath);
			ObjectInputStream in = new ObjectInputStream(fileIn);

			members = (Map<String, Pair<String, MemberType>>) in.readObject();
			bugs = (Map<Integer, Bug>) in.readObject();
			inProgress = (Map<String, Integer>) in.readObject();

			in.close();
			fileIn.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private boolean fileExists() {
		File f = new File(filePath);
		return (f.exists() && !f.isDirectory());
	}

	private boolean dataInitialised() {
		return (members != null && loggedIn != null && bugs != null && inProgress != null);
	}

	private boolean exceptionsInitialized() {
		return BugzillaException.exInitialized();
	}

	private Map<String, Pair<String, MemberType>> members;
	private ArrayList<String> loggedIn;
	private Map<String, Integer> inProgress;
	private Map<Integer, Bug> bugs;

	private boolean fileEnabled;

	private static final String filePath = "bl.bin";
}
