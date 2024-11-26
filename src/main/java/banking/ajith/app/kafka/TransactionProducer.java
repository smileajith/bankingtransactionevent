package banking.ajith.app.kafka;

import banking.ajith.app.entity.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Properties;

public class TransactionProducer {

    private KafkaProducer<String, String> producer;
    private String topic;
    private final ObjectMapper mapper;


    public TransactionProducer(String topic) {
        this.mapper = new ObjectMapper();
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka broker
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Ensures message delivery
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put("metadata.max.age.ms", "10000"); // Retry failed sends

        this.producer = new KafkaProducer<>(props);

    }

    public void sendTransaction(Transaction transaction) throws JsonProcessingException {
        try {
            String transactionToJson = mapper.writeValueAsString(transaction);

            ProducerRecord<String, String> record = new ProducerRecord<>(topic, transaction.getTransactionid(), transactionToJson);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Message Successfully sent to Topic :" + metadata.topic());
                } else {
                    exception.printStackTrace();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
}