import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FirstJUnitTest {

    @Test
    public void first() {
        // check that the string equals "Hello Daniil"
        String str = "Hello Daniil";
        assertEquals("Hello Daniil", str);
    }
}
