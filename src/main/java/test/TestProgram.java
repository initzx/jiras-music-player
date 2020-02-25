package test;

import com.jiras.music.Queue;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestProgram {
    protected Queue<Integer> queue;
    protected ArrayList<Integer> playlist;

//    @BeforeClass
//    public static void setUp(){
//        launch();
//    }
//
//    @Override
//    public void start(Stage stage) {
//        Platform.exit();
//    }

    @Before
    public void before() {
        queue = new Queue<>();
        playlist = new ArrayList<>();
        queue.reset();
        for (int i=0; i<10; i++) {
            queue.add(i);
            playlist.add(i);
        }
    }

    @Test
    public void testPrev() {
        assertNull(queue.prev());
    }

    @Test
    public void testNext() {
        assertEquals(queue.next(), playlist.get(0));
    }

    @Test
    public void testPrevTwice() {
        assertNull(queue.prev());
        assertNull(queue.prev());
    }

    @Test
    public void testAllTracks() {
        for (Integer track: playlist) {
            assertEquals(queue.next(), track);
        }
        assertNull(queue.next());
        assertEquals(queue.prev(), Integer.valueOf(playlist.size()-1));
    }
}
