package tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import khan.videos.DAO;
import khan.videos.models.Topic;
import khan.videos.models.Video;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class DAOTest {

	@Test
	public void testTopics() {
		DAO dao = DAO.get();
		Topic root = new Topic("Root", null, (Topic) null);
		dao.addTopic(root);
		dao.addTopic(new Topic("Child1", null, root));
		dao.addTopic(new Topic("Child2", null, root));
		// Add the same topic again
		dao.addTopic(new Topic("Child2", null, root));
		// Topic: Root
		{
			List<Topic> topics = dao.getTopicChildren(null);
			assertEquals(topics.get(0).getName(), "Root");
		}
		// Topics: Children
		{
			List<Topic> topics = dao.getTopicChildren("Root");
			assertEquals(topics.size(), 2);
			assertEquals(topics.get(0).getName(), "Child1");
			assertEquals(topics.get(1).getName(), "Child2");
		}
	}

	@Test
	public void testVideos() {
		DAO dao = DAO.get();
		dao.addVideo(new Video("YOUTUBEID0", "127.0.0.1", null, null, "Title"));
		dao.addVideo(new Video("YOUTUBEID1", "127.0.0.1", null, null, "Title"));
		dao.addVideo(new Video("YOUTUBEID2", "127.0.0.1", null, null, "Title"));
		// Add the same video again
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
