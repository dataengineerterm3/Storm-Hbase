<<<<<<< HEAD
# RealTimeStorm

###Navigate to project home path
mvn clean package

### install redis as socket
on mac->  
brew install redis
redis-server

reference: http://jasdeep.ca/2012/05/installing-redis-on-mac-os-x/

###launch website
cd viz <br />
python app.py <br />

type http://127.0.0.1:5000/index

###trigger storm process
Storm jar target/udacity-storm-hack-0.0.1-SNAPSHOT-jar-with-dependencies.jar udacity.storm.TopNTweetTopology
=======
# Storm-Hbase
>>>>>>> 0a9c790c9e2d1c62cc98c537035dc6da825b45b1
