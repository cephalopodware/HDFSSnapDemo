#!/bin/bash
#
#code to create a mapr volume, run the java file creation, and take three snapshots

#Remove volume
maprcli volume unmount -name snaptest
maprcli volume remove -name snaptest -force true

#create a volume to test snapshots using the MCS CLI:
maprcli volume create -name snaptest -path /snaptest

#run the java program to create log file in HDFS with timestamps (takes about three minutes to run)
java -cp `hadoop classpath`:. CopyFileToHDFS /usr/local/snapshot_demo/log.entries /mapr/my.cluster.com/snaptest/Hadoop_File.txt &

#take 3 mapr snapshots as the file is being created
sleep 45
echo "taking first snapshot @ " `date`
maprcli volume snapshot create -snapshotname snapshot1 -volume snaptest
sleep 75
echo "taking second snapshot @ " `date`
maprcli volume snapshot create -snapshotname snapshot2 -volume snaptest
sleep 75
echo "taking third snapshot @ " `date`
maprcli volume snapshot create -snapshotname snapshot3 -volume snaptest
