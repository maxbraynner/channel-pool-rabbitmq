package br.com.pooling.sender;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {

	public static void main(String[] argv) throws Exception {
		final int messageCount = 10;
		final int poolSize = 100;

		ExecutorService executor = Executors.newFixedThreadPool(poolSize);

		for (int i = 0; i < poolSize; ++i) {
			executor.execute(() -> {
				Instant start = Instant.now();
				try {
					QueueManager instance = QueueManager.getInstance();

					for (int j = 0; j < messageCount; j++) {
						instance.pushMessage(
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
