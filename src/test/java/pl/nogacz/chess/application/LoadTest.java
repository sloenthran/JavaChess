package pl.nogacz.chess.application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.io.File;
import java.io.FileNotFoundException;


@RunWith(PowerMockRunner.class)
public class LoadTest {

    @PrepareForTest(LoadGame.class)
    @Test
    public void testLoadGameThrowsFileNotFound(){
        SaveGame saveGameObj = new SaveGame();
        File dir = new File("/foo");
        Assertions.assertThrows(FileNotFoundException.class, () -> saveGameObj.saveToDirectory(dir));

    }
}
