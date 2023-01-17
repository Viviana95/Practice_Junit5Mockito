package junit5;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CalculatorTest {

	private Calculator calculator;
    private Calculator calculatorNull;
    private static Calculator calculatorStatic;
    
    @BeforeAll
    public static void beforeAllTests(){
        calculatorStatic = new Calculator();
        System.out.println("@BeforeAll -> beforeAllTests()");
    }

    @BeforeEach
    public void setUp(){
        calculator = new Calculator();
        System.out.println("@BeforeEach -> setUp()");
    }

    @AfterEach
    public void tearDown(){
        calculator = null;
        System.out.println("@AfterEach -> tearDown()");
    }

    @AfterAll
    public static void afterAllTests(){
        calculatorStatic = null;
        System.out.println("@AfterAll -> afterAllTests()");
    }

    @Test
    public void calculatorNotNullTest(){
        assertNotNull(calculatorStatic, "Calculator debe ser not null");
        assertNotNull(calculator, "Calculator debe ser not null");
        System.out.println("@Test -> calculatorNotNullTest()");
    }

    @Test
    public void calculatorNullTest(){
        assertNull(calculatorNull);
        System.out.println("@Test -> calculatorNullTest()");
    }


    @Test
    public void addAssertTest(){
        //1.- SetUp
        int resultadoEsperado = 30;
        int resultadoActual;
        //2.- Action
        resultadoActual = calculator.add(10,20);
        //3.- Assert
        assertEquals(resultadoEsperado, resultadoActual);
        System.out.println("@Test -> addAssertTest()");
    }

    @Test
    public void addTest(){
        assertEquals(30, calculator.add(10,20));
    }

    @Test
    public void assertTypesTest(){
        assertTrue(1 == 1);

        assertNull(calculatorNull);
        assertNotNull(calculator);

        Calculator calculator1 = new Calculator();
        Calculator calculator2 = new Calculator();
        Calculator calculator3 = calculator1;

        assertSame(calculator1, calculator3);
       // assertSame(calculator1, calculator2);

        assertNotSame(calculator1, calculator2);
        //assertNotSame(calculator1, calculator3);

        assertEquals("pepe", "pepe");
       // assertEquals("pepe", "pep", "Ha fallado nuestro metodo String");

        assertEquals(1, 1.4, 0.5);  //tercer parametro delta
       // assertEquals(1, 1.6, 0.5);
    }

    @Test
    public void add_ValidInput_ValidExpected_Test(){
        assertEquals(11, calculator.add(7,4));
    }

    @Test
    public void subtract_ValidInput_ValidExpected_Test(){
        assertEquals(11, calculator.subtract(15,4));
    }

    @Test
    public void subtract_ValidInput_ValidNegativeResultExpected_Test(){
        assertEquals(-10, calculator.subtract(10,20));
    }

    @Test
    public void divide_ValidInput_ValidResultExpected_Test(){
        assertEquals(2, calculator.divide(10,5));
    }

//    @Test
//    public void divide_InValidInput_Test(){
//        fail("Fallo detectado manualmente - No se puede dividir por cero");
//        calculator.divide(10,0);
//    }

    @Test
    public void divide_InValidInput_ExpectedException_Test(){
        assertThrows(ArithmeticException.class, ()->calculator.divideByZero(2,0), "No se puede dividir por cero");
    }

    @Disabled("Disabled until bug 23 be resolved") //no ejecutar test
    @Test
    public void divide_InvalidInput_Test(){
        assertEquals(2, calculator.divide(5,0));
    }

    @Test
    @DisplayName("Metodo Dividir -> Funciona")
    public void divide_ValidInput_ValidResultExpected_Name_Test(){
        assertEquals(2, calculator.divide(10,5));
    }

    @Test
    public void add_Assert_All_Test(){
        assertAll(
                ()-> assertEquals(31, calculator.add(11,20)),
                ()-> assertEquals(30, calculator.add(10,20)),
                ()-> assertEquals(2, calculator.add(1,1))
        );
    }

    @Nested 						//(agrupar test de un metodo)
    class AddTest{
        @Test
        public void add_Positive_Test(){
            assertEquals(30, calculator.add(15,15));
        }

        @Test
        public void add_Negative_Test(){
            assertEquals(-30, calculator.add(-15,-15));
        }

        @Test
        public void add_Zero_Test(){
            assertEquals(0, calculator.add(0,0));
        }
    }

    @ParameterizedTest(name = "{index} => a={0}, b={1}, sum={2}") //ejecutar prueba con diferentes valores
    @MethodSource("addProviderData")
    public void addParameterizedTest(int a, int b, int sum){
        assertEquals(sum, calculator.add(a, b));
    }

    private static Stream<Arguments> addProviderData(){
        return Stream.of(
                Arguments.of(6,2,8),
                Arguments.of(-6,-2,-8),
                Arguments.of(6,-2,4),
                Arguments.of(-6,2,-4),
                Arguments.of(6,0,6)
        );
    }

    @Test
    public void timeOut_Test(){
        assertTimeout(Duration.ofMillis(2000), () ->{
            calculator.longTaskOperation();
        });
    }

}
