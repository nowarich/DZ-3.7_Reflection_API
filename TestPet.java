import testing.AfterSuite;
import testing.BeforeSuite;
import testing.Test;

public class TestPet {

    @BeforeSuite
    void setUp() {
        System.out.println("Setting up...");
    }

//    @BeforeSuite
//    void setUp2() {
//        System.out.println("Setting up...");
//    }

    @AfterSuite
    void tearDown() {
        System.out.println("Finalizing test class...");
    }

//    @AfterSuite
//    void tearDown2() {
//        System.out.println("Finalizing test class...");
//    }

    @Test(order = 2)
    void shouldTestSmthTwo() {
        System.out.println("Test 2");
    }

    @Test
    void shouldTestSmthOne() {
        System.out.println("Test 1");
    }

    @Test(order = 3)
    void shouldTestSmthThree() {
        System.out.println("Test 3");
    }

}
