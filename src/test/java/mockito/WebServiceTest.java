package mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WebServiceTest {

	private WebService webService;
	
	@Mock
	private Callback callback;
	
	
	@BeforeEach
	public void setUp(){
		webService = new WebService();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void check_login_Test() {
		assertTrue(webService.checkLogin("Juan", "1234"));
	}
	
	@Test
	public void check_login_Error_Test() {
		assertFalse(webService.checkLogin("Juann", "12345"));
	}
	@Test
	public void loginTest() {
		webService.login("Juan", "1234", callback);
		verify(callback).onSuccess("Usuario Correcto");
		
	}
	@Test
	public void login_Error_Test() {
		webService.login("Juann", "123", callback);
		verify(callback).onFail("Error");;
		
	}

}
