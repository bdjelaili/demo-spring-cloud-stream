package io.confluent.developer.springccloud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(partitions = 1, ports = 9094, controlledShutdown = true, topics = {"myTopic1", "myTopic2"})
public class SpringCcloudApplicationTests {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafka;

	@Autowired
	private List<String> myList1;

	@Autowired
	private List<String> myList2;

	private DefaultKafkaProducerFactory<Long, Object> pf;
	protected KafkaTemplate<Long, Object> kafkaTemplate;

	@PostConstruct
	void init() {
		pf = new DefaultKafkaProducerFactory<>(KafkaTestUtils.producerProps(embeddedKafka));
		kafkaTemplate = new KafkaTemplate<>(pf, true);
	}

	@PreDestroy
	void destroy() {
		pf.destroy();
		embeddedKafka.destroy();
	}

	@Test
	public void myList1() {
		kafkaTemplate.send("myTopic1", "my text");
		Awaitility.await().pollInterval(Duration.ofMillis(500))
				.atMost(Duration.ofSeconds(3))
				.until(() -> !myList1.isEmpty());
	}

	@Test
	public void myList2() {
		kafkaTemplate.send("myTopic2", "my text");
		Awaitility.await().pollInterval(Duration.ofMillis(500))
				.atMost(Duration.ofSeconds(3))
				.until(() -> !myList2.isEmpty());
	}


}
