package tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import khan.videos.DAO;
import khan.videos.models.Topic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class TopicStorageTest {

	@Test
	public void run() {
		DAO dao = DAO.get();
		Topic root = new Topic("Root", null, (Topic) null);
		dao.addTopic(root);
		dao.addTopic(new Topic("Child1", null, root));
		dao.addTopic(new Topic("Child2", null, root));
		// Topic: Root
		{
			List<Topic> topics = dao.getTopicChildren(null);
			assertEquals(topics.get(0).getName(), "Root");
		}
		// Topics: Children
		{
			List<Topic> topics = dao.getTopicChildren("Root");
			assertEquals(topics.get(0).getName(), "Child1");
			assertEquals(topics.get(1).getName(), "Child2");
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
