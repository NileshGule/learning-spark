package com.nileshgule.movielens;

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;


public class MoviesCsvToORCConvertor {

    public static void main(String[] args) {

        // warehouseLocation points to the default location for managed databases and tables
        String warehouseLocation = new File("spark-warehouse").getAbsolutePath();

        System.out.println("Warehouse locations : " + warehouseLocation);

        SparkSession spark = SparkSession
                .builder()
                .appName("Movie CSV To ORC Convertor")
                .config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .getOrCreate();

        String inputFilePath = args[0];

        Dataset<Row> ds = CsvUtils.getDataFrame(spark, inputFilePath);

        ds.printSchema();

        ds.select("userId", "movieId", "rating", "timestamp")
                .show(10, false);

        System.out.println("Total movies in collection = " + ds.count());

        ds.write()
                .orc("/ml-lates/" + "rating-orc");

        System.out.print("Successfully written ORC file");
    }
}
