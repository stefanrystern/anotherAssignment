

public class BugStateException extends BugzillaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BugStateException(Bug.State from, Bug.State to) {
		super(ErrorType.INVALID_STATE_TRANSITION);
		// TODO Auto-generated constructor stub
		
		fromState = from;
		toState = to;
		
		msg = String.format(msg, fromState.toString(), toState.toString());
		
	}
	
	public Bug.State getFromState() {
		return fromState;
	}

	public Bug.State getToState() {
		return toState;
	}

	private Bug.State fromState;
	private Bug.State toState;

}
