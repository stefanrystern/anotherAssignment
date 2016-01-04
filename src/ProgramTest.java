
import org.junit.Test;

public class ProgramTest {

	public ProgramTest() {
		try {
			BugzillaException.init();
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Test
	public void testRegisterNullName() {
		Bugzilla bz = null;

		try {
			// Disable file handling by passing false to the constructor
			bz = new Bugzilla(false);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			bz.register(null, "abc", Bugzilla.MemberType.USER);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Test
	public void testChangeStateUnconfirmedToInProgress() {

		Bug bug = null;

		try {
			bug = new Bug(0, "crash on OK press");
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			bug.setState(Bug.State.INPROGRESS);

		} catch (BugStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());

		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testInvalidID() {

		Bug bug = null;

		try {
			bug = new Bug(-100, "negative ID");
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}
	@Test
	public void testInvalidDescription() {

		Bug bug = null;

		try {
			bug = new Bug(0, null);

		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}		
	}
	@Test
	public void testSetStateFromConfirmedToInProgress(){
		Bug bug = null;

		try {
			bug = new Bug(0, "set state to unconfirmed");
			bug.setState(Bug.State.CONFIRMED);
			bug.setState(Bug.State.INPROGRESS);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}	
		
		try {
			bug = new Bug(0, "set state to unconfirmed");
			bug.setState(Bug.State.INPROGRESS);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}	
	}
	@Test
	public void testSetStateUnconfirmed() {

		Bug bug = null;

		try {
			bug = new Bug(0, "set state to unconfirmed");
			bug.setState(Bug.State.UNCONFIRMED);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}		
	}
	@Test
	public void testSetStateResolved() {

		Bug bug = null;

		try {
			bug = new Bug(0, "set state to resolved");
			bug.setState(Bug.State.RESOLVED);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}		
	}
	@Test
	public void testSetResolvedUnresolved() {

		Bug bug = null;

		try {
			bug = new Bug(0, "setAsResolved to unresolved");
			bug.setAsResolved(Bug.Resolution.UNRESOLVED, "should fail");
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}		
	}
	@Test
	public void testSetAsResolvedInvalidSolution() {

		Bug bug = null;

		try {
			bug = new Bug(0, "set state to resolved");
			bug.setAsResolved(Bug.Resolution.INVALID, null);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}		
	}
}
