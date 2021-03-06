# MovieLens
-
### Prerequisite
Download [MovieLens dataset](https://grouplens.org/datasets/movielens/) locally on to the machine where the Spark job would be running
The details of the data contained within the dataset can be found [here](http://files.grouplens.org/datasets/movielens/ml-latest-small-README.html)

Assumption is that the extracted contents of the zip file are available in folder named `ml-latest` in the root directory of the project

To fix the error with zsh while running parallel option for spark master local[2] run the following command before submitting the spark-submit
`setopt nonomatch` 

### Run MoviesCsvReader program

```bash
spark-submit --packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.MoviesCsvReader \
--master local \
--deploy-mode client \
--executor-memory 2g \
--name MoviesCsvReader \
--conf "spark.app.id=MoviesCsvReader" \
target/learning-spark-1.0.jar
```

***Note:***
Make sure to pass the spark-csv package during spark-submit using `--packages com.databricks:spark-csv_2.10:1.5.0`, otherwise runtime exception of classNotFound is thrown

### Run RatingsCsvReader program

```bash
time spark-submit --packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.RatingsCsvReader \
--master local \
--deploy-mode client \
--executor-memory 2g \
--name RatingsCsvReader \
--conf "spark.app.id=RatingsCsvReader" \
target/learning-spark-1.0.jar

time spark-submit --packages com.databricks:spark-csv_2.10:1.5.0,com.groupon.sparklint:sparklint-spark163_2.10:1.0.8 \
--conf spark.logConf=true \
--conf spark.extraListeners=com.groupon.sparklint.SparklintListener \
--class com.nileshgule.movielens.RatingsCsvReader \
--master local[2] \
--deploy-mode client \
--executor-memory 2g \
--name RatingsCsvReader \
--conf "spark.app.id=RatingsCsvReader" \
target/learning-spark-1.0.jar

time spark-submit --packages com.databricks:spark-csv_2.10:1.5.0,com.groupon.sparklint:sparklint-spark163_2.10:1.0.8 \
--conf spark.logConf=true \
--conf spark.extraListeners=com.groupon.sparklint.SparklintListener \
--class com.nileshgule.movielens.RatingsCsvReader \
--master local[4] \
--deploy-mode client \
--executor-memory 2g \
--name RatingsCsvReader \
--conf "spark.app.id=RatingsCsvReader" \
target/learning-spark-1.0.jar

time spark-submit --packages com.databricks:spark-csv_2.10:1.5.0,com.groupon.sparklint:sparklint-spark163_2.10:1.0.8 \
--conf spark.logConf=true \
--conf spark.extraListeners=com.groupon.sparklint.SparklintListener \
--class com.nileshgule.movielens.RatingsCsvReader \
--master local[4] \
--deploy-mode client \
--executor-memory 4g \
--name RatingsCsvReader \
--conf "spark.app.id=RatingsCsvReader" \
target/learning-spark-1.0.jar

time spark-submit --packages com.databricks:spark-csv_2.10:1.5.0,com.groupon.sparklint:sparklint-spark163_2.10:1.0.8 \
--conf spark.logConf=true \
--conf spark.extraListeners=com.groupon.sparklint.SparklintListener \
--class com.nileshgule.movielens.RatingsCsvReader \
--master local \
--deploy-mode client \
--executor-memory 4g \
--name RatingsCsvReader \
--conf "spark.app.id=RatingsCsvReader" \
target/learning-spark-1.0.jar ml-latest/ratings.csv


time spark-submit --packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.RatingsCsvReader \
--master local[4] \
--deploy-mode client \
--executor-memory 2g \
--name RatingsCsvReader \
--conf "spark.app.id=RatingsCsvReader" \
target/learning-spark-1.0.jar

spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.RatingsCsvReader \
--master local \
--deploy-mode client \
--name RatingsCsvReader \
--conf "spark.app.id=RatingsCsvReader" \
target/learning-spark-1.0.jar \
ml-latest/ratings.csv
```

### Run TagsCsvReader program

```bash
spark-submit --packages com.databricks:spark-csv_2.10:1.5.0,com.groupon.sparklint:sparklint-spark163_2.10:1.0.8 \
--conf spark.extraListeners=com.groupon.sparklint.SparklintListener \
--class com.nileshgule.movielens.TagsCsvReader \
--master local \
--deploy-mode client \
--executor-memory 2g \
--name TagsCsvReader \
--conf "spark.app.id=TagsCsvReader" \
target/learning-spark-1.0.jar

spark-submit --conf spark.extraListeners=com.groupon.sparklint.SparklintListener \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.TagsCsvReader \
--master local \
--deploy-mode client \
--executor-memory 2g \
--name TagsCsvReader \
--conf "spark.app.id=TagsCsvReader" \
target/learning-spark-1.0.jar

spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.TagsCsvReader \
--master local \
--deploy-mode client \
--executor-memory 2g \
--name TagsCsvReader \
--conf "spark.app.id=TagsCsvReader" \
target/learning-spark-1.0.jar
```

```bash
time \
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.MovieRatingAnalysis \
--master local \
--deploy-mode client \
--name MovieRatingAnalysis \
--conf "spark.app.id=MovieRatingAnalysis" \
target/learning-spark-1.0.jar \
ml-latest/ratings.csv \
ml-latest/movies.csv
```

Time taken with cache enabled
231.71s user 8.07s system 114% cpu 3:29.34 total

Time taken with cache disabled
271.15s user 8.78s system 115% cpu 4:02.76 total

```bash
time \
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.UserAnalysis \
--master local \
--deploy-mode client \
--name UserAnalysis \
--conf "spark.app.id=UserAnalysis" \
target/learning-spark-1.0.jar \
ml-latest/ratings.csv \
ml-latest/movies.csv
```

Fine tuning performance using executors and memory
```bash
time \
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.UserAnalysis \
--num-executors 3 \
--executor-memory 2G \
--executor-cores 3 \
--master local \
--deploy-mode client \
--name UserAnalysis \
--conf "spark.app.id=UserAnalysis" \
target/learning-spark-1.0.jar \
ml-latest/ratings.csv \
ml-latest/movies.csv
```