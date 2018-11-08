package diary.beautiful.sync;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZipToolTest {

    @Test
    public void doZip() {
        ZipTool.getTool().doZip("E:\\log","E:\\log.zip");
    }

    @Test
    public void unZip() {
        ZipTool.getTool().unZip("E:\\log.zip","E:\\log");
    }
}