import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JobScheduleTest {

	private JobSchedule schedule;
	private JobSchedule invalidSchedule;

	@Before
	public void setUp() throws Exception {

		schedule = new JobSchedule();
		schedule.addJob(5);
		schedule.addJob(8);
		schedule.addJob(10);
		
		invalidSchedule = new JobSchedule();
		invalidSchedule.addJob(4);
		invalidSchedule.addJob(8);
		invalidSchedule.addJob(9);
		invalidSchedule.addJob(6);
	}

	@Test
	public void testPreliminary() 
	{
		JobSchedule.Job j = schedule.addJob(4);
		assertEquals(10, schedule.minCompletionTime(), .000001);
		assertEquals(0, j.getStartTime(), .000001);
	}

	@Test
	public void testRequired()
	{
		schedule.addJob(3);
		schedule.addJob(13);
		schedule.addJob(6);
		schedule.getJob(2).requires(schedule.getJob(0));
		schedule.getJob(4).requires(schedule.getJob(1));
		schedule.getJob(3).requires(schedule.getJob(1));
		schedule.getJob(3).requires(schedule.getJob(4));
		schedule.getJob(2).requires(schedule.getJob(5));
		schedule.getJob(1).requires(schedule.getJob(2));
		assertEquals(40, schedule.minCompletionTime(), .000001);
		assertEquals(16, schedule.getJob(1).getStartTime(), .000001);
		assertEquals(24, schedule.getJob(4).getStartTime(), .000001);
	}

	@Test
	public void testCycles()
	{
		invalidSchedule.getJob(1).requires(invalidSchedule.getJob(2));
		invalidSchedule.getJob(2).requires(invalidSchedule.getJob(1));
		invalidSchedule.getJob(3).requires(invalidSchedule.getJob(0));
		invalidSchedule.getJob(2).requires(invalidSchedule.getJob(3));
		assertEquals(-1, invalidSchedule.minCompletionTime(), .000001);
		assertEquals(-1, invalidSchedule.getJob(1).getStartTime(), .000001);
		assertEquals(-1, invalidSchedule.getJob(2).getStartTime(), .000001);
		assertEquals(0, invalidSchedule.getJob(0).getStartTime(), .000001);
		assertEquals(4, invalidSchedule.getJob(3).getStartTime(), .000001);
	}

}
