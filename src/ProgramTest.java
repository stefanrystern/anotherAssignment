import org.junit.Before;
import org.junit.Test;

import com.google.java.contract.InvariantError;
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

	
	//bug
	@Test(expected = InvariantError.class)
	public void testInvalidID() throws BugzillaException {
		bug = new Bug(-100, "negative ID");
	}

	@Test(expected = PreconditionError.class)
	public void testNullDescription() throws BugzillaException {
		bug = new Bug(0, null);
	}
	
	@Test(expected = PreconditionError.class)
	public void testEmptyDescription() throws BugzillaException {
		bug = new Bug(0, "");
	}
	//bug setState
	@Test(expected = PreconditionError.class)
	public void testSetStateToInProgressFromFalseState() throws BugzillaException {
		bug = new Bug(0, "set state to unconfirmed");
		bug.setState(Bug.State.INPROGRESS);
	}
	@Test(expected = PreconditionError.class)
	public void testSetStateToConfirmedFromFalseState() throws BugzillaException {
		bug = new Bug(0, "set state to unconfirmed");
		bug.setState(Bug.State.CONFIRMED);
		bug.setState(Bug.State.INPROGRESS);
		bug.setAsResolved(Bug.Resolution.FIXED, "fixed it");
		bug.setState(Bug.State.VERIFIED);
		bug.setState(Bug.State.CONFIRMED);
	}
	@Test(expected = PreconditionError.class)
	public void testSetStateToVerifiedProgressFromFalseState() throws BugzillaException {
		bug = new Bug(0, "set state to unconfirmed");
		bug.setState(Bug.State.VERIFIED);
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
	// bug setResolved
	@Test(expected = PreconditionError.class)
	public void testSetResolvedUnresolved() throws BugzillaException {
		bug = new Bug(0, "setAsResolved to unresolved");
		bug.setState(Bug.State.CONFIRMED);
		bug.setState(Bug.State.INPROGRESS);
		bug.setAsResolved(Bug.Resolution.UNRESOLVED, "should fail");
	}

	@Test(expected = PreconditionError.class)
	public void testSetAsResolvedInvalidSolution() throws BugzillaException {
		bug = new Bug(0, "set state to resolved");
		bug.setState(Bug.State.CONFIRMED);
		bug.setState(Bug.State.INPROGRESS);
		bug.setAsResolved(Bug.Resolution.FIXED, null);
	}
	@Test(expected = PreconditionError.class)
	public void testSetAsResolvedInvalidStartState() throws BugzillaException {
		bug = new Bug(0, "set state to resolved");
		bug.setAsResolved(Bug.Resolution.FIXED, "fixed it");
	}
	// Bugzilla register
	@Test(expected = PreconditionError.class)
	public void testAlreadyRegistered() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
	}

	@Test(expected = PreconditionError.class)
	public void testUsernameTooShort() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("", "mina", Bugzilla.MemberType.USER);
	}
	
	@Test(expected = PreconditionError.class)
	public void testUsernameTooLong() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("minaminaminaminaminaminamina", "mina", Bugzilla.MemberType.USER);
	}

	@Test(expected = PreconditionError.class)
	public void testRegisterNullPassword() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", null, Bugzilla.MemberType.USER);
	}

	@Test(expected = PreconditionError.class)
	public void testRegisterPasswordTooShort() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "", Bugzilla.MemberType.USER);
	}
	
	@Test(expected = PreconditionError.class)
	public void testRegisterPasswordTooLong() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "minaminaminaminaminaminamina", Bugzilla.MemberType.USER);
	}

	@Test(expected = PreconditionError.class)
	public void testRegisterMemberType() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", null);
	}

	// Bugzilla Login
	@Test(expected = PreconditionError.class)
	public void testLoginNullUsername() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.login(null, "mina");
	}

	@Test(expected = PreconditionError.class)
	public void testLoginNullPassword() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.login("mina", null);
	}

	@Test(expected = PreconditionError.class)
	public void testLoginInvalidPassword() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("minas", "minas");
	}

	@Test(expected = PreconditionError.class)
	public void testLoginIsLoggedIn() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("mina", "mina");
		bz.login("mina", "mina");
	}

	// bugzilla Logout
	@Test(expected = PreconditionError.class)
	public void testLogoutNullUsername() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.logout(null);
	}

	@Test(expected = PreconditionError.class)
	public void testLogoutIsNotLoggedIn() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.logout("mina");
	}

	// submitbug
	@Test(expected = PreconditionError.class)
	public void testSubmitBugNullUsername() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.submitBug(null, "test bug");
	}

	@Test(expected = PreconditionError.class)
	public void testSubmitBugMemberTypeUser() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.SYSTEMANALYST);
		bz.login("mina", "mina");
		bz.submitBug("mina", "test bug");
	}

	@Test(expected = PreconditionError.class)
	public void testSubmitBugNotLoggedIn() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.submitBug("mina", "test bug");
	}

	@Test(expected = PreconditionError.class)
	public void testSubmitBugDescriptionLength() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.submitBug("mina", "");
	}
	
	@Test(expected = PreconditionError.class)
	public void testSubmitBugDescriptionNull() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.submitBug("mina", null);
	}

	// bugzilla confirmbug
	@Test(expected = PreconditionError.class)
	public void testConfirmBugNullUsername() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.confirmBug(null, 0);
	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugIDoutOfBounds() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.SYSTEMANALYST);
		bz.login("mina", "mina");
		bz.confirmBug("mina", 3);
	}
	
	@Test(expected = PreconditionError.class)
	public void testConfirmBugIDNegative() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.SYSTEMANALYST);
		bz.login("mina", "mina");
		bz.confirmBug("mina", -1);
	}
	
	@Test(expected = PreconditionError.class)
	public void testConfirmBugDoesntExist() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.SYSTEMANALYST);
		bz.login("mina", "mina");
		bz.confirmBug("mina", 3);
	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugTypeNotSystemAnalyst() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("mina", "mina");
		bz.submitBug("mina", "test bug");
		bz.confirmBug("mina", 0);
	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugNotLoggedIn() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("mina", "mina");
		bz.submitBug("mina", "test bug");
		bz.logout("mina");
		bz.confirmBug("mina", 0);
	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugStateNotUnconfirmed() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("mina", "mina");
		bz.submitBug("mina", "test bug");
		bz.confirmBug("mina", 0);
	}

	@Test(expected = PreconditionError.class)
	public void testConfirmBugStateVerified() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.submitBug("mina", "test bug");
		bz.invalidateBug("analyst", 0, "solution");
		bz.approveFix("assurance", 0);
		bz.confirmBug("analyst", 0);
	}
	
	//bugzilla invalidateBug
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugUsernameNull() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.invalidateBug(null, 0, "solution");
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugBugIdOutOfBounds() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.invalidateBug("mina", 3, "solution");
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugBugIdNegative() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.invalidateBug("mina", -3, "solution");
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugSolutionNull() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("mina", "mina");
		bz.submitBug("mina", "test bug");
		bz.invalidateBug("mina", 0, null);
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugUserOffline() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("mina", "mina");
		bz.submitBug("mina", "test bug");
		bz.logout("mina");
		bz.invalidateBug("mina", 0, "solution");
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugUserNotAnalyst() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.login("mina", "mina");
		bz.submitBug("mina", "test bug");
		bz.invalidateBug("mina", 0, "solution");
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugBugDoesntExist() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.login("analyst", "analyst");
		bz.invalidateBug("analyst", 0, "solution");
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugBugStateNotUncofnirmed() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.submitBug("mina", "test bug");
		bz.invalidateBug("analyst", 0, "solution");
		bz.invalidateBug("analyst", 0,"solution");
	}
	@Test(expected = PreconditionError.class)
	public void testInvalidateBugBugStateVerified() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.submitBug("mina", "test bug");
		bz.invalidateBug("analyst", 0, "solution");
		bz.approveFix("assurance", 0);
		bz.invalidateBug("analyst", 0,"solution");
	}
	//bugzilla startDevelopment
	@Test(expected = PreconditionError.class)
	public void testStartDevelopmentNullUsername() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("dev", "dev");
		bz.startDevelopment(null, 10);
	}
	public void testStartDevelopmentBugsNegative() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("dev", "dev");
		bz.startDevelopment("dev", -10);
	}
	public void testStartDevelopmentBugsOutOfBounds() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("dev", "dev");
		bz.startDevelopment("dev", 10);
	}
	public void testStartDevelopmentDeveloperType() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.startDevelopment("mina", 0);
		bz.startDevelopment("analyst", 0);
		bz.startDevelopment("assurance", 0);
	}
	public void testStartDevelopmentNotLoggedIn() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("dev", "dev");
		bz.logout("dev");
		bz.startDevelopment("dev", 0);
	}
	public void testStartDevelopmentNotConfirmed() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.startDevelopment("dev", 0);
	}
	public void testStartDevelopmentStateVerified() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.invalidateBug("analyst", 0, "it's not that bad");
		bz.approveFix("assurance", 0);
		bz.startDevelopment("dev", 0);
	}
	//bugzilla stopDevelopment
	@Test(expected = PreconditionError.class)
	public void testStopDevelopmentNullUsername() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.confirmBug("analyst", 0);
		bz.startDevelopment("dev", 0);
		bz.stopDevelopment(null, 10);
	}
	public void testStopDevelopmentBugsNegative() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.confirmBug("analyst", 0);
		bz.startDevelopment("dev", 0);
		bz.stopDevelopment("dev", -10);
	}
	public void testStopDevelopmentBugsOutOfBounds() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.confirmBug("analyst", 0);
		bz.startDevelopment("dev", 0);
		bz.stopDevelopment("dev", 10);
	}
	public void testStopDevelopmentLoggedIn() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.confirmBug("analyst", 0);
		bz.startDevelopment("dev", 0);
		bz.logout("dev");
		bz.stopDevelopment("dev", 0);
	}
	public void testStopDevelopmentTypeDeveloper() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.confirmBug("analyst", 0);
		bz.startDevelopment("dev", 0);
		bz.stopDevelopment("mina", 0);
	}
	public void testStopDevelopmentStateInProgress() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.confirmBug("analyst", 0);
		bz.stopDevelopment("dev", 0);
	}
	public void testStopDevelopmentDevInProgress() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.register("dev2", "dev2", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.login("dev2", "dev2");
		bz.submitBug("user", "it's a bug");
		bz.confirmBug("analyst", 0);
		bz.startDevelopment("dev", 0);
		bz.stopDevelopment("dev2", 0);
	}
	public void testStopDevelopmentStateVerified() throws BugzillaException {
		bz = new Bugzilla(false);
		bz.register("mina", "mina", Bugzilla.MemberType.USER);
		bz.register("analyst", "analyst", Bugzilla.MemberType.SYSTEMANALYST);
		bz.register("assurance", "assurance", Bugzilla.MemberType.QUALITYASSURANCE);
		bz.register("dev", "dev", Bugzilla.MemberType.DEVELOPER);
		bz.login("mina", "mina");
		bz.login("analyst", "analyst");
		bz.login("assurance", "assurance");
		bz.login("dev", "dev");
		bz.submitBug("user", "it's a bug");
		bz.invalidateBug("analyst", 0, "it's not that bad");
		bz.approveFix("assurance", 0);
		bz.stopDevelopment("dev", 0);
	}
	//bugzilla fixedBug
	@Test(expected = PreconditionError.class)
	public void testFixedBugInvalidSolutionType() throws BugzillaException {
		
	}
	public void testFixedBugInvalidSolutionInfo() throws BugzillaException {
			
	}
	public void testFixedBugNullUsername() throws BugzillaException {
		
	}
	public void testFixedBugInvalidBugID() throws BugzillaException {
		
	}
	public void testFixedBugMemberTypeDev() throws BugzillaException {
		
	}
	public void testFixedBugLoggedOut() throws BugzillaException {
		
	}
	public void testFixedBugInProgress() throws BugzillaException {
		
	}
	public void testFixedBugVerified() throws BugzillaException {
		
	}
	public void testFixedBugDevInProgress() throws BugzillaException {
		
	}

	//bugzilla approvedFix
	@Test(expected = PreconditionError.class)
	public void testApprovedFix() throws BugzillaException {
			
	}
	//bugzilla rejectFix
	@Test(expected = PreconditionError.class)
	public void testRejectFix() throws BugzillaException {
	
	}
}
