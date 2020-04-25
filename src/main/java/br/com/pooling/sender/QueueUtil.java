package br.com.pooling.sender;

import java.io.IOException;

public class QueueUtil {

	public static boolean pushMessage(Queue queue, final String message) {
		return ChannelPool.getInstance().execute(channel -> {
			try {
				channel.basicPublish(queue.getExchange(), queue.getName(), queue.getProps(), message.getBytes("UTF-8"));
				return true;
			} catch (IOException up) {
				up.printStackTrace();
				return false;
			}
		});
	}

}
