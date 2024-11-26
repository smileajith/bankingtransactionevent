import org.apache.spark.sql.{Encoders, SparkSession}
import org.apache.spark.sql.functions._
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra.DataFrameWriterWrapper
import org.apache.spark.sql.streaming.Trigger


case class Transaction(accountnumber: Integer, payment_type: String, debitedamount: Integer, branchname: String,transactionid :String)

object TransactionProcessor {

  def main(args: Array[String]): Unit = {
    // Step 1: Initialize Spark Session
    val spark = SparkSession.builder
      .appName("TransactionProcessor")
      .master("local[*]")
      .config("spark.cassandra.connection.host", "127.0.0.1")  // Cassandra server address
      .getOrCreate()

    import spark.implicits._

    // Step 2: Read from Kafka
    val kafkaSource = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "bank-transactions")
      .option("startingOffsets", "earliest")
      .load()

    // Step 3: Deserialize JSON Messages
    val transactions = kafkaSource.selectExpr("CAST(value AS STRING) as json")
      .select(from_json($"json", schema = Encoders.product[Transaction].schema).as("transaction"))
      .select("transaction.*")

    // Step 4: Write all transactions to Cassandra without filtering
    val query = transactions.writeStream
      .foreachBatch { (batchDF: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row], batchId: Long) =>
        batchDF.write
          .cassandraFormat("transaction", "banking") // Cassandra table and keyspace
          .mode("append")
          .save()
      }
      .outputMode("update")
      .trigger(Trigger.ProcessingTime("10 seconds"))
      .start()

    query.awaitTermination()
  }
}
