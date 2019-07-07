# paper57
Instruction: 

The folder "src" contains all the java source code of the experiment. We performed our experiment under jdk-12, with external jar file "lucene-analyzers-common-7.5.0.jar" and "mysql-connector-java-8.0.15.jar". 

https://drive.google.com/open?id=1NFTERBHyDHKt0DlR-xiJmXCRac5jhkc8 is the link to the data used in our experiment, the file "snippet_dataset_all.sql" contains all the data about the datasets and snippets used in our experiment. You need to download it first, and import it to your local MySQL database. 


To successfully run the experiment, you need to follow these steps: 

1, Import all the data from "snippet_dataset_all.sql" to a MySQL database. There should be 4 tables: "dataset_info", "triple", "uri_label_id" and "snippet_generation_result". 

2, Add "lucene-analyzers-common-7.5.0.jar" and "mysql-connector-java-8.0.15.jar" to your current workspace as external libraries. 

3, Adjust the JDBC configuration (username, password, etc.) of the project in util/DBconnecter.java to your local settings. 

4, Run main() in process/getResult, the output is the average scores of evaluation metrics on all the query-dataset pairs (Table 3 in our paper). There will be 4 rows of output results. The first row shows the average scores "coKyw", "coCnx", "coSkm" and "coDat" of IlluSnip, the second to fourth row are the average scores of TA+C, PrunedDP++ and CES. 
