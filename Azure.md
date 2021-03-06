# Azure

Use HDInsight Spark cluster to run Spark jobs. Refer to [introduction to Spark on HD insight](https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-apache-spark-overview)

Intellij IDE can submmit jobs to HDInsight cluster directly using the [Azure Toolkit for Intellij](https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-apache-spark-intellij-tool-plugin)

**Most Important:** Don't forget to [delete the cluster](https://docs.microsoft.com/en-us/azure/hdinsight/hdinsight-delete-cluster)

### To display hidden files & folder in Safari
Use the Key combination of `command + shift + .` to toggle the hidden files visibility
To show the files in finder use the command
```bash
defaults write com.apple.finder AppleShowAllFiles YES
```

## Azure CLI
To work with Azure resources using command line tools [Azure CLI](https://docs.microsoft.com/en-sg/cli/azure/install-azure-cli) can be installed.

Troubleshooting [ssh connectivity](https://github.com/twright-msft/azure-content/blob/master/articles/hdinsight/hdinsight-hadoop-linux-use-ssh-unix.md) problems to the head node

Reference to the blobstorage [commandline](https://docs.microsoft.com/en-us/cli/azure/storage/blob#uploads)

### To fix the ECDSA key fingerprint command run
```bash
ssh-keygen -R ng-spark-ssh.azurehdinsight.net
```

### List keys & containers from blob storage

```bash

az storage account keys list --resource-group ngresourcegroup --account-name ngstorageaccount

az storage container list --account-name ngstorageaccount --account-key btQLcwrSfGXolrdtnXt0115rizP24U+JFH7M9uWQxcyQ2gASp3+lxIAe1+44U4JFMvBH8ZDZT30TJh5q4p0lIg==

```

### List keys & containers from blob storage for ngsparkstorageaccount

```bash

az storage account keys list \
--resource-group ngresourcegroup \
--account-name ngsparkstorageaccount

az storage container list \
--account-name ngsparkstorageaccount \
--account-key TCeus8dSphBrvL0IQ3N7D2+G/a4F8kWLqg7klBeMRLsEW+0e2fjPYoRm2jsZEo5iwZF+uZ+8Fjg2G0TXcDOjXA==

az storage container list \
--account-name ngsparkstorageaccount \
--account-key PUmQtT/j4dxqaYTkPs3YQP0jkNDOg3oAtw8jUdYEWe3tOb97jChgNJCI2IH9iXWzTi18zJlm+Xgv4hBtXh9iiw==

az storage container create --name movielens-dataset \
--account-name ngsparkstorageaccount \
--account-key PUmQtT/j4dxqaYTkPs3YQP0jkNDOg3oAtw8jUdYEWe3tOb97jChgNJCI2IH9iXWzTi18zJlm+Xgv4hBtXh9iiw==

az storage blob upload-batch \
--destination ng-spark-2017/movielense_dataset \
--source ml-latest \
--account-name ngsparkstorageaccount \
--account-key PUmQtT/j4dxqaYTkPs3YQP0jkNDOg3oAtw8jUdYEWe3tOb97jChgNJCI2IH9iXWzTi18zJlm+Xgv4hBtXh9iiw==

az storage blob upload \
--account-name ngsparkstorageaccount \
--account-key PUmQtT/j4dxqaYTkPs3YQP0jkNDOg3oAtw8jUdYEWe3tOb97jChgNJCI2IH9iXWzTi18zJlm+Xgv4hBtXh9iiw== \
--file target/learning-spark-1.0.jar \
--name learning-spark-1.0.jar \
--container-name ng-spark-2017

az storage blob upload \
--account-name ngsparkstorageaccount \
--account-key TCeus8dSphBrvL0IQ3N7D2+G/a4F8kWLqg7klBeMRLsEW+0e2fjPYoRm2jsZEo5iwZF+uZ+8Fjg2G0TXcDOjXA== \
--file learning-spark-1.0.jar \
--name learning-spark-1.0.jar \
--container-name ng-spark-2017


time \
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.UserAnalysis \
--master yarn \
--deploy-mode cluster \
--num-executors 2 \
--executor-memory 2G \
--executor-cores 6 \
--name UserAnalysis \
--conf "spark.app.id=UserAnalysis" \
wasb://ng-spark-2017@ngsparkstorageaccount.blob.core.windows.net/learning-spark-1.0.jar \
wasb://ng-spark-2017@ngsparkstorageaccount.blob.core.windows.net/movielense_dataset/ratings.csv \
wasb://ng-spark-2017@ngsparkstorageaccount.blob.core.windows.net/movielense_dataset/movies.csv
```

### Create container to store Movielens dataset
```bash

    az storage container create --name movielens-dataset \
    --account-name ngstorageaccount \
    --account-key btQLcwrSfGXolrdtnXt0115rizP24U+JFH7M9uWQxcyQ2gASp3+lxIAe1+44U4JFMvBH8ZDZT30TJh5q4p0lIg==
    
    

```

### Copy / move data from existing blob to the new one

```bash

az storage blob copy start-batch \
                                 --account-name ngstorageaccount \
                                 --account-key btQLcwrSfGXolrdtnXt0115rizP24U+JFH7M9uWQxcyQ2gASp3+lxIAe1+44U4JFMvBH8ZDZT30TJh5q4p0lIg== \
                                 --destination-container movielens-dataset \
                                 --pattern "movielense_dataset/*" \
                                 --source-container ng-spark-2017-08-18t14-24-10-259z
                                 
```

### Upload application jar to blobstorage
```bash

az storage blob upload \
--account-name ngstorageaccount \
--account-key btQLcwrSfGXolrdtnXt0115rizP24U+JFH7M9uWQxcyQ2gASp3+lxIAe1+44U4JFMvBH8ZDZT30TJh5q4p0lIg== \
--file learning-spark-1.0.jar \
--name learning-spark-1.0.jar \
--container-name movielens-dataset

```

### upload movielense dataset files to blobstore

```bash

az storage blob upload-batch \
--destination ng-spark-2017-08-18t14-24-10-259z/movielense_dataset \
--source ml-latest \
--account-name ngstorageaccount \
--account-key btQLcwrSfGXolrdtnXt0115rizP24U+JFH7M9uWQxcyQ2gASp3+lxIAe1+44U4JFMvBH8ZDZT30TJh5q4p0lIg==



``` 

### Run Spark Submit
```bash

spark-submit \
--class com.nileshgule.PairRDDExample \
--master yarn \
--deploy-mode cluster \
--executor-memory 2g \
--name PairRDDExample \
--conf "spark.app.id=PairRDDExample" \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/learning-spark-1.0.jar

spark-submit \
--class com.nileshgule.PairRDDExample \
--master yarn \
--deploy-mode cluster \
--executor-memory 2g \
--name PairRDDExample \
--conf "spark.app.id=PairRDDExample" \
wasb://ng-spark-2017@ngsparkstorageaccount.blob.core.windows.net/learning-spark-1.0.jar
```

```bash
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.RatingsCsvReader \
--master yarn \
--deploy-mode cluster \
--executor-memory 1g \
--name RatingCSVReader \
--conf "spark.app.id=RatingCsvReader" \
wasb://movielens-dataset@ngstorageaccount.blob.core.windows.net/learning-spark-1.0.jar \
wasb://movielens-dataset@ngstorageaccount.blob.core.windows.net/movielens-dataset/movielense_dataset/ratings.csv

```

### Run User And Rating analysis

Default run without performance tuning parameters
```bash
time \
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.UserAnalysis \
--master yarn \
--deploy-mode cluster \
--name UserAnalysis \
--conf "spark.app.id=UserAnalysis" \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/learning-spark-1.0.jar \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/movielense_dataset/ratings.csv \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/movielense_dataset/movies.csv
```

```bash
time \
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.UserAnalysis \
--master yarn \
--deploy-mode cluster \
--num-executors 2 \
--executor-memory 2G \
--executor-cores 6 \
--name UserAnalysis \
--conf "spark.app.id=UserAnalysis" \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/learning-spark-1.0.jar \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/movielense_dataset/ratings.csv \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/movielense_dataset/movies.csv
```

### Upload application jar to blob storage

```bash 

az storage blob upload \
--account-name ngstorageaccount \
--account-key btQLcwrSfGXolrdtnXt0115rizP24U+JFH7M9uWQxcyQ2gASp3+lxIAe1+44U4JFMvBH8ZDZT30TJh5q4p0lIg== \
--file learning-spark-1.0.jar \
--name learning-spark-1.0.jar \
--container-name ng-spark-2017-08-18t14-24-10-259z

```

Upload jar with default Java serialize

```bash
az storage blob upload \
--account-name ngstorageaccount \
--account-key btQLcwrSfGXolrdtnXt0115rizP24U+JFH7M9uWQxcyQ2gASp3+lxIAe1+44U4JFMvBH8ZDZT30TJh5q4p0lIg== \
--file learning-spark-1.0.jar \
--name learning-spark-1.0.jar \
--container-name ng-spark-2017-08-18t14-24-10-259z
```


time \
spark-submit \
--packages com.databricks:spark-csv_2.10:1.5.0 \
--class com.nileshgule.movielens.UserAnalysis \
--master yarn \
--deploy-mode cluster \
--num-executors 2 \
--executor-memory 3G \
--executor-cores 4 \
--name UserAnalysis \
--conf "spark.app.id=UserAnalysis" \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/learning-spark-1.0.jar \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/movielense_dataset/ratings.csv \
wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/movielense_dataset/movies.csv

### Run HDFS commands using wasb

```bash
hdfs dfs -ls wasb://ng-spark-2017-08-18t14-24-10-259z@ngstorageaccount.blob.core.windows.net/movielense_dataset/

hdfs dfs -ls /movielense_dataset/
```

