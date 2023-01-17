package mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

class AddTest {

	@InjectMocks
    private Add add;
    @Mock
    private ValidNumber validNumber;
    @Mock
    private Print print;
    @Captor
    private ArgumentCaptor<Integer> captor;
    

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addTest(){
        when(validNumber.check(3)).thenReturn(true);
        boolean checkNumber = validNumber.check(3);
        assertEquals(true, checkNumber);

        when(validNumber.check("a")).thenReturn(false);
        checkNumber = validNumber.check("a");
        assertEquals(false, checkNumber);
    }

    @Test
    public void addMockExceptionTest(){
        when(validNumber.checkZero(0)).thenThrow(new ArithmeticException("No podemos aceptar cero"));
        Exception exception = null;
        try{
            validNumber.checkZero(0);
        }catch (ArithmeticException e){
            exception = e;
        }
        assertNotNull(exception);
    }

    @Test
    public void addRealMethodTest(){
        when(validNumber.check(3)).thenCallRealMethod();
        assertEquals(true, validNumber.check(3));

        when(validNumber.check("3")).thenCallRealMethod();
        assertEquals(false, validNumber.check("3"));
    }

    @Test
    public void addDoubleToIntThenAnswerTest(){
        Answer<Integer> answer = new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                return 7;
            }
        };

        when(validNumber.doubleToInt(7.7)).thenAnswer(answer);
        assertEquals(14, add.addInt(7.7));
    }
    
    @Test
    public void patterTest() {   					//patron de prueba 1
    	//arrange
    	when(validNumber.check(4)).thenReturn(true);
    	when(validNumber.check(5)).thenReturn(true);
    	//act
    	int result = add.add(4, 5);
    	//assert
    	assertEquals(9, result);
    	
    }
    @Test
    public void patterBDDTest() { 					//patron de prueba 2
    	//given
    	given(validNumber.check(4)).willReturn(true);
    	given(validNumber.check(5)).willReturn(true);
    	//when
    	int result = add.add(4, 5); 				//escribir otro valor en los parametros el mock no lo cumple
    	//then
    	assertEquals(9, result);
    	
    }
    
    @Test
    public void argument_Matcher_Test() { 					
    	//given
    	given(validNumber.check(anyInt())).willReturn(true);  //acepta cualquier entero sin tener que especificar
    	//when
    	int result = add.add(4, 5); 				
    	//then
    	assertEquals(9, result);
    	
    }
    
    @Test
    public void add_Print_Test() { 			
    	//given
    	given(validNumber.check(4)).willReturn(true);
    	given(validNumber.check(5)).willReturn(true);
    	//when
    	add.addPrint(4, 5); 				
    	//then
    	verify(validNumber).check(4); //metodo check ejecuta 2 veces
    	
    }
    
    @Test
    public void add_Print_Same_Number_Test() { 		//usar times			
    	//given
    	given(validNumber.check(4)).willReturn(true);
    	//when
    	add.addPrint(4, 4);				
    	//then
    	//verify(validNumber).check(4); //error metodo check ejecuta 2 veces
    	verify(validNumber, times(2)).check(4); //invocar 2 veces
    	
    }
    @Test
    public void add_Print1_Test() { 			
    	//given
    	given(validNumber.check(4)).willReturn(true);
    	given(validNumber.check(5)).willReturn(true);
    	//when
    	add.addPrint(4, 5); 				
    	//then
    	verify(validNumber, never()).check(99); //nunca invocar
    	verify(validNumber, atLeast(1)).check(4);  //num min veces al menos
    	verify(validNumber, atMost(3)).check(4);	//como mucho
    	
    	verify(print).showMessage(9);
    	verify(print, never()).showError();
    }
    
    @Test
    public void captor_Test() {
    	//given
    	given(validNumber.check(4)).willReturn(true);
    	given(validNumber.check(5)).willReturn(true);
    	//when
    	add.addPrint(4, 5); 				
    	//then
    	verify(print).showMessage(captor.capture()); //capturar el argumento cualquier valor
    	assertEquals(captor.getValue().intValue(), 9); 	//castearlo
    }
    
    
    
//    @Spy
//    List<String> spyList = new ArrayList<>();
    @Mock
    List<String> mockList = new ArrayList<>();
//    
//    @Test
//    public void spy_test() {
//    	spyList.add("1");
//    	spyList.add("2");
//    	verify(spyList).add("1");
//    	verify(spyList).add("2");
//    	assertEquals(2, spyList.size());  //no ha sido mockeado
//    }
    
    @Test
    public void mock_test() {
    	mockList.add("1");
    	mockList.add("2");
    	verify(mockList).add("1");
    	verify(mockList).add("2");
    	given(mockList.size()).willReturn(2); //return el valor, si no devuelve 0 al no mockear size
    	assertEquals(2, mockList.size());  
    }
    
    
}
