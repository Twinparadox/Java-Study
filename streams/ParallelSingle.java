package streams;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ParallelSingle {

	public static final int TEST_SIZE = 100000;

	public static void main(String[] args) {

		List<Person> people = new ArrayList<>();
		Random rand = new Random();
		for (int i = 0; i < TEST_SIZE; i++) {
			people.add(new Person(UUID.randomUUID().toString(), rand.nextInt(100), rand.nextInt(100) + 100));
		}

		// Warming up
		for (int i = 0; i < 10; i++) {
			people.stream().filter(p -> p.getAge() % 2 == 0);
			people.parallelStream().filter(p -> p.getAge() % 2 == 0);
		}

		// Simple Task
		System.out.println("Even Age");
		filterTest(people);

		// Grouping Task
		// Warming up
		for (int i = 0; i < 10; i++) {
			Map<Integer, List<Person>> map1 = people.stream().collect(groupingBy(Person::getAge));
			Map<Integer, List<Person>> map2 = people.parallelStream().collect(groupingBy(Person::getAge));
		}

		System.out.println("groupingBy");
		groupingTest(people);
	}

	private static void filterTest(List<Person> people) {
		long start = 0, end = 0, runningTime = 0;

		// Single Stream
		start = System.nanoTime();
		people.stream().filter(p -> p.getAge() % 2 == 0);
		end = System.nanoTime();
		runningTime = end - start;
		System.out.println("Single Stream : " + runningTime);

		// Parallel Stream
		start = System.nanoTime();
		people.parallelStream().filter(p -> p.getAge() % 2 == 0);
		end = System.nanoTime();
		runningTime = end - start;
		System.out.println("Parallel Stream : " + runningTime);
	}

	private static void groupingTest(List<Person> people) {
		long start = 0, end = 0, runningTime = 0;

		// Single Stream
		start = System.nanoTime();
		Map<Integer, List<Person>> map1 = people.stream().collect(groupingBy(Person::getAge));
		end = System.nanoTime();
		runningTime = end - start;
		System.out.println("Single Stream : " + runningTime);

		// Parallel Stream
		start = System.nanoTime();
		Map<Integer, List<Person>> map2 = people.parallelStream().collect(groupingBy(Person::getAge));
		end = System.nanoTime();
		runningTime = end - start;
		System.out.println("Parallel Stream : " + runningTime);
	}
}
