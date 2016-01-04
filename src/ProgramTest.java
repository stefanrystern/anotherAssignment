
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
	public void testRegisterNullName() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register(null, "abc", Bugzilla.MemberType.USER);
	}

	@Test(expected = PreconditionError.class)
	public void testChangeStateUnconfirmedToInProgress() throws BugzillaException {
		bug = new Bug(0, "crash on OK press");
		bug.setState(Bug.State.INPROGRESS);
	}

	@Test(expected = PreconditionError.class)
	public void testInvalidID() throws BugzillaException {
		bug = new Bug(-100, "negative ID");
	}

	@Test(expected = PreconditionError.class)
	public void testInvalidDescription() throws BugzillaException {
		bug = new Bug(0, null);
	}

	@Test(expected = PreconditionError.class)
	public void testSetStateFromConfirmedToInProgress() throws BugzillaException {
		bug = new Bug(0, "set state to unconfirmed");
		bug.setState(Bug.State.CONFIRMED);
		bug.setState(Bug.State.INPROGRESS);

		bug = new Bug(0, "set state to unconfirmed");
		bug.setState(Bug.State.INPROGRESS);
	}

	@Test(expected = PreconditionError.class)
	public void testSetStateUnconfirmed() throws BugzillaException {
		bug = new Bug(0, "set state to unconfirmed");
		bug.setState(Bug.State.UNCONFIRMED);
	}

	@Test(expected = PreconditionError.class)
	public void testSetStateResolved() throws BugzillaException {
		bug = new Bug(0, "set state to resolved");
		bug.setState(Bug.State.RESOLVED);
	}

	@Test(expected = PreconditionError.class)
	public void testSetResolvedUnresolved() throws BugzillaException {
		bug = new Bug(0, "setAsResolved to unresolved");
		bug.setAsResolved(Bug.Resolution.UNRESOLVED, "should fail");
	}

	@Test(expected = PreconditionError.class)
	public void testSetAsResolvedInvalidSolution() throws BugzillaException {
		bug = new Bug(0, "set state to resolved");
	}
	// Bugzilla part

	@Test(expected = PreconditionError.class)
	public void testRegisterNullUsername() {

	}

	@Test(expected = PreconditionError.class)
	public void testAlreadyRegistered() {

	}

	@Test(expected = PreconditionError.class)
	public void testUsernameLength() {

	}

	@Test(expected = PreconditionError.class)
	public void testRegisterNullPassword() {

	}

	@Test(expected = PreconditionError.class)
	public void testRegisterPasswordLength() {

	}

	@Test(expected = PreconditionError.class)
	public void testRegisterMemberType() {

	}

	// Login
	@Test(expected = PreconditionError.class)
	public void testLoginNullUsername() {

	}

	@Test(expected = PreconditionError.class)
	public void testLoginNullPassword() {

	}

	@Test(expected = PreconditionError.class)
	public void testLoginInvalidPassword() {

	}

	@Test(expected = PreconditionError.class)
	public void testLoginIsLoggedIn() {

	}

	// Logout
	@Test(expected = PreconditionError.class)
	public void testLogoutNullUsername() {

	}

	@Test(expected = PreconditionError.class)
	public void testLogoutIsNotLoggedIn() {

	}

	// submitbug
	@Test(expected = PreconditionError.class)
	public void testSubmitBugNullUsername() {

	}

	@Test(expected = PreconditionError.class)
	public void testSubmitBugMemberTypeUser() {

	}

	@Test(expected = PreconditionError.class)
	public void testSubmitBugNotLoggedIn() {

	}

	@Test(expected = PreconditionError.class)
	public void testSubmitBugDescriptionLength() {

	}

	// confirmbug
	@Test(expected = PreconditionError.class)
	public void testConfirmBugNullUsername() {

	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugIDoutOfBounds() {

	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugTypeNotSystemAnalyst() {

	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugNotLoggedIn() {

	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugStateNotUnconfirmed() {

	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugStateVerified() {

	}
}
