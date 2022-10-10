package com.draexlmaier;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class FlowAPI {

	public static class MySubscriber<T> implements Flow.Subscriber<T> {

		private Flow.Subscription subscription;

		@Override
		public void onSubscribe(Flow.Subscription subscription) {
			this.subscription = subscription;
			this.subscription.request(1); // Ask for initial one data object.
		}

		@Override
		public void onNext(T item) {
			System.out.println(item); // Print it.
			subscription.request(1); // Ask for one more.
		}

		@Override
		public void onError(Throwable throwable) {
			throwable.printStackTrace();
		}

		@Override
		public void onComplete() {
			System.out.println("DONE"); // Done with the stream of data.
		}
	}

	public static void main(String[] args) {
		var items = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9");
		var publisher = new SubmissionPublisher<>();
		publisher.subscribe(new MySubscriber<>());

		items.forEach(s -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			publisher.submit(s);
		});

		publisher.close();
	}

}
