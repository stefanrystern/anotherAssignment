
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.java.contract.InvariantError;
import com.google.java.contract.PostconditionError;
import com.google.java.contract.PreconditionError;

public class ProgramTest {

	Bug bug;
	Bugzilla bz;

	public ProgramTest() {
		try {
			BugzillaException.init();
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Before
	public void setUp() {
		Bug bug = null;
		Bugzilla bz = null;
	}

	@Test(expected = PreconditionError.class)
	public void testRegisterNullName() {
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

	@Test(expected = PreconditionError.class)
	public void testChangeStateUnconfirmedToInProgress() {
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

	@Test(expected = PreconditionError.class)
	public void testInvalidID() {
		try {
			bug = new Bug(-100, "negative ID");
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Test(expected = PreconditionError.class)
	public void testInvalidDescription() {
		try {
			bug = new Bug(0, null);

		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Test(expected = PreconditionError.class)
	public void testSetStateFromConfirmedToInProgress() {
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

	@Test(expected = PreconditionError.class)
	public void testSetStateUnconfirmed() {
		try {
			bug = new Bug(0, "set state to unconfirmed");
			bug.setState(Bug.State.UNCONFIRMED);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Test(expected = PreconditionError.class)
	public void testSetStateResolved() {
		try {
			bug = new Bug(0, "set state to resolved");
			bug.setState(Bug.State.RESOLVED);
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Test(expected = PreconditionError.class)
	public void testSetResolvedUnresolved() {
		try {
			bug = new Bug(0, "setAsResolved to unresolved");
			bug.setAsResolved(Bug.Resolution.UNRESOLVED, "should fail");
		} catch (BugzillaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getErrorMsg());
		}
	}

	@Test(expected = PreconditionError.class)
	public void testSetAsResolvedInvalidSolution() {
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
