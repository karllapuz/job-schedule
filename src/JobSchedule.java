import java.util.*;

public class JobSchedule
{
	private ArrayList<Job> schedule;
	private Job end = new Job(0);

	public JobSchedule()
	{
		schedule = new ArrayList<Job>();
	}

	public Job addJob(int time)
	{
		Job j = new Job(time);
		schedule.add(j);
		end.requires(j);
		return j;
	}

	public Job getJob(int index)
	{
		return schedule.get(index);
	}

	public int minCompletionTime()
	{
		if (!simulateDAG())
		{
			return -1;
		}
		return end.d;
	}

	private boolean simulateDAG()
	{
		ArrayList<Job> ordered = topOrder();
		schedule.add(end);
		ordered.add(end);
		initialize();
		findPath(ordered); 
		if (ordered.size() != schedule.size())
		{
			schedule.remove(schedule.size() - 1);
			return false;
		}
		schedule.remove(schedule.size() - 1);
		return true;
	}

	private void initialize()
	{
		for (Job j : schedule)
		{
			j.finished = false;
			j.pi = null;
			j.d = 0;
		}
	}

	private ArrayList<Job> topOrder()
	{
		ArrayList<Job> ordered = new ArrayList<Job>();
		for (Job j : schedule)
		{
			j.inDegree = j.incoming.size();
			if (j.inDegree == 0)
			{
				ordered.add(j);
			}
		}
		ListIterator<Job> iter = ordered.listIterator();
		while (iter.hasNext())
		{
			Job j = iter.next();
			for (Job in : j.outgoing)
			{
				in.inDegree--;
				if (in.inDegree == 0)
				{
					iter.add(in);
					j = iter.previous();
				}
			}
		}
		return ordered;
	}

	private void findPath(ArrayList<Job> jobs)
	{
		for (Job j : jobs)
		{
			j.finished = true;
			for (Job out : j.outgoing)
			{
				relax(j, out);
			}
		}
	}

	private void relax(Job from, Job to)
	{
		if (from.d + from.time > to.d)
		{

			to.d = from.d + from.time;
			to.pi = from;
			from.finished = true;

		}
	}

	public class Job
	{
		public ArrayList<Job> incoming;
		public ArrayList<Job> outgoing;
		public boolean inALoop;
		public boolean finished;
		public Job pi;
		public int d;
		public int time;
		public int inDegree;

		private Job(int time)
		{
			this.time = time;
			incoming = new ArrayList<Job>();
			outgoing = new ArrayList<Job>();
		}

		public void requires(Job j)
		{
			incoming.add(j);
			j.outgoing.add(this);
			if (this.incoming.contains(j) && this.outgoing.contains(j))
			{
				inALoop = true;
				j.inALoop = true;
			}
		}

		public int getStartTime()
		{
			if (inALoop)
			{
				return -1;
			}
			return d;
		}
	}
}