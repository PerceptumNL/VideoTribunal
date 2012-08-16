package tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import khan.videos.DAO;
import khan.videos.models.Video;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class VideoStorageTest {

	@Test
	public void run() {
		DAO dao = DAO.get();
		dao.addVideo(new Video("YOUTUBEID0", "127.0.0.1", null, null, "Title"));
		dao.addVideo(new Video("YOUTUBEID1", "127.0.0.1", null, null, "Title"));
		dao.addVideo(new Video("YOUTUBEID2", "127.0.0.1", null, null, "Title"));
		// Add the same videos again
		dao.addVideo(new Video("YOUTUBEID2", "127.0.0.1", null, null, "Title"));
		// Videos: Root
		{
			List<Video> videos = dao.getTopicVideos(null);
			assertEquals(videos.size(), 3);
			assertEquals(videos.get(0).getYoutubeId(), "YOUTUBEID0");
			assertEquals(videos.get(1).getYoutubeId(), "YOUTUBEID1");
			assertEquals(videos.get(2).getYoutubeId(), "YOUTUBEID2");
		}
	}

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

}
