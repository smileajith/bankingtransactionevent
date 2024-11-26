package banking.ajith.app;

import banking.ajith.app.entity.Transaction;
import banking.ajith.app.kafka.TransactionProducer;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Random;
import java.util.UUID;

public class BankingMainApplication {

    public static void main(String[] args) throws JsonProcessingException {

        String topic = "bank-transactions";

        TransactionProducer transactionProducer = new TransactionProducer(topic);
        Random random = new Random();
        long startTime = System.currentTimeMillis(); // Capture start time
        long duration = 5 * 60 * 1000; // Duration for 5 minutes (300,000 ms)

        int i=0;
        while(System.currentTimeMillis() - startTime < duration){
            Transaction transaction = new Transaction();

            transaction.setAccountnumber(100000 + random.nextInt(900000)); // Random 6-digit account number
            transaction.setPayment_type(random.nextBoolean() ? "Credit" : "Debit"); // Random payment type
            transaction.setDebitedamount(random.nextInt(10000) + 1); // Random debited amount between 1 and 10,000
            transaction.setBranchname("Branch" + (i + 1)); // Sequential branch name
            transaction.setTransactionid("CUST" + (1000 + i)); // Sequential customer ID
            transaction.setTransactionid(UUID.randomUUID().toString()); // Unique transaction ID

            i++;
            System.out.println("Sending transaction: " + transaction);
            transactionProducer.sendTransaction(transaction); // Assuming `producer` is defined
        }

        transactionProducer.close();

        System.out.println("Total banking transaction made " + i );
    }



}