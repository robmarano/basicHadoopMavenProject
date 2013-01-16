basicHadoopMavenProject
=======================

A simple project template to begin hacking Hadoop with MavenhadoopWordCountComplex
======================

To build:
$> mvn compile

To package:
$> mvn package

To run, assuming you have Cloudera Hadoop CDH4 installed, and that you have text files in /user/${USER}/input
$> hadoop fs -rm -r /user/${USER}/output/
$> hadoop jar ./target/primaven-1.0-SNAPSHOT.jar com.thehackerati.hadoop.WordCount /user/${USER}/input /user/${USER}/output

