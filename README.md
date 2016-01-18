# RealTimeStorm

###Navigate to project home path
mvn clean package

###trigger storm process
Storm jar target/udacity-storm-hack-0.0.1-SNAPSHOT-jar-with-dependencies.jar udacity.storm.TopNTweetTopology

###launch website
cd viz <br />
python app.py <br />

type http://127.0.0.1:5000/index
