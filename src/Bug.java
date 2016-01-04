
import java.io.Serializable;

import com.google.java.contract.Ensures;
import com.google.java.contract.Invariant;
import com.google.java.contract.Requires;

@Invariant({ "ID >= 0",
		"state == State.UNCONFIRMED || state == State.CONFIRMED || state == State.INPROGRESS || state == State.RESOLVED || state == State.VERIFIED",
		"description != null", "description.length()>0",
		"solutionType == Resolution.UNRESOLVED || solutionType == Resolution.FIXED || solutionType == Resolution.DUPLICATE || solutionType == Resolution.WONTFIX || solutionType == Resolution.WORKSFORME || solutionType == Resolution.INVALID",
		"solutionInfo != null",
})
/*
 * The class represents a bug in the bugs database. The bug cannot be modified
 * after it is VERIFIED.
 */
public class Bug implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * If the bug is RESOLVED or VERIFIED, it must have a Resolution type other
	 * than UNRESOLVED. The type is set in the "setAsResolved" method. A
	 * DEVELOPER may set the value to any other than UNRESOLVED.
	 */

	public static enum Resolution {
		UNRESOLVED, FIXED, DUPLICATE, WONTFIX, WORKSFORME, INVALID
	}

	/*
	 * The bug must be in one of the following states
	 */
	public static enum State {
		UNCONFIRMED, CONFIRMED, INPROGRESS, RESOLVED, VERIFIED
	}

	@Requires({ "ID >= 0", "description != null", "description.length()>0", })
	@Ensures({ "ID == old(id)", "bugDescription = old(description)", "state = State.UNCONFIRMED",
			"solutionType = Resolution.UNRESOLVED", "solutionInfo != null"})

	/*
	 * The constructor accepts the Bug ID and description BugID must not be less
	 * than 0. Description must not be empty The bug is initialized in the state
	 * UNCONFIRMED and the resolution type is UNRESOLVED
	 */
	public Bug(int id, String description) throws BugzillaException {
		if (ID < 0) {
			throw new BugzillaException(BugzillaException.ErrorType.INVALID_BUGID);
		}
		if (description == null || description.length()<=0) {  //weird check that length is not negative
			throw new BugzillaException(BugzillaException.ErrorType.INVALID_DESCRIPTION);
		}

		// ...

		ID = id;
		bugDescription = description;
		state = State.UNCONFIRMED;
		solutionType = Resolution.UNRESOLVED;
		solutionInfo = new String();
	}

	public int getID() {
		return ID;
	}

	public String getBugDescription() {
		return bugDescription;
	}

	public State getState() {
		return state;
	}

	@Requires({
			"st != State.RESOLVED",
			"st != State.UNCONFIRMED",
			"st == State.INPROGRESS? state == State.CONFIRMED : true" })
	@Ensures({
			"state == old(st)",
			"state != State.RESOLVED",
			"state != State.UNCONFIRMED"
	})
	/*
	 * Sets the sate of the bug to any state other than RESOLVED and
	 * UNCONFIRMED. A bug cannot be set to UNCONFIRMED because it starts in that
	 * state and does not go back to it. State RESOLVED is set by the method
	 * "setAsResolved"
	 */
	public void setState(State st) throws BugzillaException {

		if (st == State.INPROGRESS && state != State.CONFIRMED) {
			throw new BugStateException(state, st);
		}
		if(st == State.UNCONFIRMED){
			throw new BugStateException(state, st);
		}
		if(st == State.RESOLVED){
			throw new BugzillaException(BugzillaException.ErrorType.INVALID_METHOD);
		}

		// ...

		this.state = st;

		// If state changed from RESOLVED to CONFIRMED then discard the solution
		// type
		if (state == State.CONFIRMED && solutionType != Resolution.UNRESOLVED) {
			solutionType = Resolution.UNRESOLVED;
		}
	}

	public Resolution getSolutionType() {
		return solutionType;
	}

	/*
	 * Sets the state of the bug to RESOLVED. Solution type must not be
	 * UNRESOLVED and solution description must not be empty
	 */

	@Requires({
		"type != Resolution.UNRESOLVED",
		"solution != null",
	})
	@Ensures({
		"solutionType == old(type)",
		"solutionInfo == old(solution)",
	})
	public void setAsResolved(Resolution type, String solution) throws BugzillaException {
		if(type == Resolution.UNRESOLVED){
			throw new BugzillaException(BugzillaException.ErrorType.INVALID_SOLUTION_TYPE);
		}
		if(solution == null){
			throw new BugzillaException(BugzillaException.ErrorType.INVALID_SOLUTION_INFO);
		}
		
		state = State.RESOLVED;
		solutionType = type;
		solutionInfo = solution;
	}

	public String getSolutionInfo() {
		return solutionInfo;
	}

	private int ID;
	private String bugDescription;
	private State state;
	private Resolution solutionType;
	private String solutionInfo;
}
