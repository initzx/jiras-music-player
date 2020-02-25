package test;

import com.jiras.music.Playlist;
import com.jiras.music.Queue;
import com.jiras.music.Track;
import com.jiras.user.UserData;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestProgram extends Application {
    protected UserData userData;
    protected Queue<Track> queue;
    protected Playlist playlist;

    @BeforeClass
    public static void setUp(){
        launch();
    }

    @Override
    public void start(Stage stage) {
        Platform.exit();
    }

    @Before
    public void before() {
        userData = UserData.createUserDataFromConf();
        queue = new Queue<>();
        playlist = userData.getPlaylist("Circles");
        queue.reset();
        queue.addAll(playlist.getTracks());
    }

    @Test
    public void testPrev() {
        assertNull(queue.prev());
    }

    @Test
    public void testNext() {
        assertEquals(queue.next(), playlist.getTracks()[0]);
    }

    @Test
    public void testPrevTwice() {
        assertNull(queue.prev());
        assertNull(queue.prev());
    }

    @Test
    public void testAllTracks() {
        for (Track track: playlist.getTracks()) {
            assertEquals(queue.next(), track);
        }
    }
}
