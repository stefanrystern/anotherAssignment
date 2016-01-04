
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
			//Disable file handling by passing false to the constructor
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
			bug = new Bug(0,"crash on OK press");
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

}
