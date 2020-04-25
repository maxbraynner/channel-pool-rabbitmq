package br.com.pooling.sender;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {

	public static void main(String[] argv) throws Exception {
		final int messageCount = 10;
		final int executorSize = 100;
		final int threadSize = 1000;

		ExecutorService executor = Executors.newFixedThreadPool(executorSize);
		for (int i = 0; i < threadSize; ++i) {
			executor.execute(() -> {
				try {
					Instant start = Instant.now();

					for (int j = 0; j < messageCount; j++) {
						QueueUtil.pushMessage(Queue.HELLO,
								MessageFormat.format("[{0}] - Message: {1}", Thread.currentThread().getName(), j));
					}

					Duration duration = Duration.between(start, Instant.now());
					System.out.println(MessageFormat.format("Thread time: {0}ms", duration.toMillis()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

}
