package mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class LoginTest {

	@InjectMocks
	private Login login;
	
	@Mock
	private WebService webservice;
	
	@Captor
	private ArgumentCaptor<Callback> callbackArgumentCaptor;
	
	@BeforeEach
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void do_login_Test() {
		doAnswer(new Answer() {
			@Override
			public Object answer (InvocationOnMock invocationOnMock)throws Throwable{
				String user = (String)invocationOnMock.getArguments()[0];  //recuperar el arg real
				assertEquals("Juan", user);
				String password = (String)invocationOnMock.getArguments()[1];
				assertEquals("1234", password);
				Callback callback = (Callback)invocationOnMock.getArguments()[2];
				callback.onSuccess("OK");
				return null;
			}
		}).when(webservice).login(anyString(), anyString(), any(Callback.class));
		
		login.doLogin();
		verify(webservice, times(1)).login(anyString(), anyString(), any(Callback.class));
		assertEquals(login.isLogin, true);
		
	};
	
	@Test
	public void do_login_Error_Test() {
		doAnswer(new Answer() {
			@Override
			public Object answer (InvocationOnMock invocationOnMock)throws Throwable{
				String user = (String)invocationOnMock.getArguments()[0];  //recuperar el arg real
				assertEquals("Juan", user);
				String password = (String)invocationOnMock.getArguments()[1];
				assertEquals("1234", password);
				Callback callback = (Callback)invocationOnMock.getArguments()[2];
				callback.onFail("Error");
				return null;
			}
		}).when(webservice).login(anyString(), anyString(), any(Callback.class));
		
		login.doLogin();
		verify(webservice, times(1)).login(anyString(), anyString(), any(Callback.class));
		assertEquals(login.isLogin, false);
		
	};
	
	@Test
	public void do_login_in_captor_Test() {
		login.doLogin();
		verify(webservice, times(1)).login(anyString(), anyString(), callbackArgumentCaptor.capture()); 
		assertEquals(login.isLogin, false);
		
		Callback callback = callbackArgumentCaptor.getValue();
		
		callback.onSuccess("Ok");					//
		assertEquals(login.isLogin, true);
		
		callback.onFail("Error");					//
		assertEquals(login.isLogin, false);
		
	}

}
