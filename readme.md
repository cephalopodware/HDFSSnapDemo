# Background:

Demo to compare how MapR and HDFS snapshots work.   This demo will stream log entries to HDFS/MapRFS, allowing us to take several snapshots and examine their characteristics.

## Files:
1.   log.entries - a file containing 1M simulated log entries, each with a timestamp, hash, ip and message; 1 record per line.
2.   CopyFileToHDFS.java - java source to create a log file in HDFS, using log.entries (or any other log file) as a source.    This program will pause and create a searchable token (SNAPTEST_TIMESTAMP) plus the timestamp in the output log every 5,000 lines.
3.  buildit.sh - script containing the command to compile CopyFileToHDFS.java
4.  rundemo_mapr.sh - script to run the demo on a MapR system (also cleans up previous tests)
5.  rundemo_cdh.sh - script to run the demo on a CDH system. (also cleans up previous tests)

#MapR Instructions:
1.   Run as a user that has rights to create and manage volumes
2.   Copy demo files to an accessible location on MapR system.   Default for demo is /usr/local/snapshot_demo.
3.   Examine log.entries contents.
4.   Examine CopyFileToHDFS.java contents.
5.   Compile CopyFileToHDFS.java on system(can use buildit.sh):  
	    `` javac -cp `hadoop classpath` CopyFileToHDFS.java ``
3.   Examine rundemo_mapr.sh contents.    Edit locations of input and output files if desired.
4.    Execute rundemo_mapr.sh.    This will start streaming the log, and take three snapshots over the course of three minutes.     The streaming will still run after the snapshots are done, use jps to determine when it finishes.
5.    Explore the data stored in the output and the snapshots. For example:
 - Note the time that each snapshot was created from the rundemo_mapr.sh output
 - Examine the size of each file:  
      `ls -l /mapr/my.cluster.com/snaptest/.snapshot/snapshot1/Hadoop_File.txt`  
      `ls -l /mapr/my.cluster.com/snaptest/.snapshot/snapshot2/Hadoop_File.txt`  
      `ls -l /mapr/my.cluster.com/snaptest/.snapshot/snapshot3/Hadoop_File.txt`  
      `ls -l /mapr/my.cluster.conf/snaptest/Hadoop_File.txt`
 - Check the latest timestamps in each snapshot file, and compare to the time the snapshots were taken  
	    `grep SNAPTEST_TIMESTAMP /mapr/my.cluster.com/snaptest/.snapshot/snapshot1/Hadoop_File.txt | tail -3  `  
	    `grep SNAPTEST_TIMESTAMP /mapr/my.cluster.com/snaptest/.snapshot/snapshot2/Hadoop_File.txt | tail -3  `  
	    `grep SNAPTEST_TIMESTAMP /mapr/my.cluster.com/snaptest/.snapshot/snapshot3/Hadoop_File.txt | tail -3 `   
	    `grep SNAPTEST_TIMESTAMP /mapr/my.cluster.conf/snaptest/Hadoop_File.txt | tail -3 `  

## Results ##
The files on the MapR snapshots are accessible (not corrupted), and represent the point in time as to when the snapshots were taken.



#CDH (or any other distro) Instructions:
1.   Run as a user that has ownership of the directory to be used for snapshots.     Demo default is the "cloudera" user.
2.   Copy demo files to an accessible location on the CDH system.   Default for demo is /usr/local/snapshot_demo.
3.   Examine log.entries contents.
4.   Examine CopyFileToHDFS.java contents.
5.   Compile CopyFileToHDFS.java on system(can use buildit.sh):  
       `` javac -cp `hadoop classpath` CopyFileToHDFS.java ``
3.   Examine rundemo_cdh.sh contents.    Edit locations of input and output files if desired.
4.   Enable snapshots on the target directory.    SU to HDFS user and run:  
	     `hdfs dfsadmin -allowSnapshots /user/cloudera`
5.    Execute rundemo_cdh.sh.    This will start streaming the log, and take three snapshots over the course of three minutes.     The streaming will still run after the snapshots are done, use jps to determine when it finishes.
6.    Explore the data stored in the output and the snapshots. For example:
 - Note the time that each snapshot was created from the rundemo_cdh.sh output
 - Examine the size of each file  
	      `hadoop fs -ls  /cloudera/.snapshot/snapshot1/Hadoop_File.txt`  
	      `hadoop fs -ls  /cloudera/.snapshot/snapshot2/Hadoop_File.txt`  
	      `hadoop fs -ls  /cloudera/.snapshot/snapshot3/Hadoop_File.txt`  
	      `hadoop fs -ls  /cloudera/Hadoop_File.txt`  
 - Check the latest timestamps in each snapshot file, and compare to the time the snapshots were taken  
	      `hadoop fs -text /user/cloudera/.snapshot/snapshot1/Hadoop_File.txt | grep SNAPTEST_TIMESTAMP | tail -3`  
	      `hadoop fs -text /user/cloudera/.snapshot/snapshot2/Hadoop_File.txt | grep SNAPTEST_TIMESTAMP | tail -3`  
	      `hadoop fs -text /user/cloudera/.snapshot/snapshot3/Hadoop_File.txt | grep SNAPTEST_TIMESTAMP | tail -3`  
	      `hadoop fs -text /user/cloudera/Hadoop_File.txt | grep SNAPTEST_TIMESTAMP | tail -3`  

## Results
All four files are identical, showing that the snapshots continue to be written to, after they should be frozen.    Each output file will contain timestamps that are later than the time that the snapshot was taken.  

# Conclusions:
Comparing the results from both tests makes it clear that HDFS snapshots do not provide any point in time consistency, whereas MapR snapshots do.  This makes MapR snapshots useful for backups, auditing, applications based on multiple files, or "data versioning" of a real time, streaming system.    HDFS snapshots are not usable for any of these use cases.
