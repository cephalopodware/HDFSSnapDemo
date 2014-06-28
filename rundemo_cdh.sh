#!/bin/bash
#
# code to create a log file in HDFS and make snapshot copies while it is being created.

#clean up from previous run
hadoop fs -deleteSnapshot /user/cloudera snapshot1
hadoop fs -deleteSnapshot /user/cloudera snapshot2
hadoop fs -deleteSnapshot /user/cloudera snapshot3
hadoop fs -rm /user/cloudera/Hadoop_File.txt

#run the java program to create log file in HDFS with timestamps 
java -cp `hadoop classpath`:. CopyFileToHDFS /usr/local/snapshot_demo/log.entries /user/cloudera/Hadoop_File.txt &

#take 3 mapr snapshots as the file is being created
sleep 45
echo "taking first snapshot @ " `date`
hadoop fs -createSnapshot /user/cloudera snapshot1
sleep 75
echo "taking second snapshot @ " `date`
hadoop fs -createSnapshot /user/cloudera snapshot2
sleep 75
echo "taking third snapshot @ " `date`
hadoop fs -createSnapshot /user/cloudera snapshot3