import junit.framework.TestCase;


public class Test extends TestCase {

    public void testMaskRemaker(){

        DirectoryScanner test = new DirectoryScanner();

        String mask = "*Test*";
        mask = DirectoryScanner.maskRemaker(mask);
        assertEquals(".*Test.*",mask);
    }
}
