import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import models.raven.AuthenticatedUser;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.Application;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 *
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 *
 */
public class ApplicationTest {

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertEquals(2, a);
    }
    
    
	@Test
	public void testPasswordHashingConsistency() {
		String password = "abcd123!";
		AuthenticatedUser u = new AuthenticatedUser();
		u.setPassword(password);
		boolean authenticated = u.authenticate(password);
		assertTrue(authenticated);
	}
	
	/*private Application fakeApp = Helpers.fakeApplication();
	
    @Test  
    public void testEmailVerification2 () {  
        Helpers.running(fakeApp, () -> {
    		AuthenticatedUser u = new AuthenticatedUser();
    		u.setEmail("i@jamiel.net");
    		u.setFname("Jamiel");
    		u.setLname("Sheikh");
    		Call action = controllers.raven.routes.RavenController.verifyEmailAddress(); 
            Result res = route(Helpers.fakeRequest(action)); 
            //Optional<String> url = res.header(Http.HeaderNames.LOCATION);
            //assertEquals("text/html", res.contentType());
            
        });
    } */


}
