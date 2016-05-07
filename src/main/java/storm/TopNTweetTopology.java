package storm;

import java.util.Properties;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

class TopNTweetTopology extends BaseTopology 
{
  public TopNTweetTopology(String configFileLocation) throws Exception {
		super(configFileLocation);
		// TODO Auto-generated constructor stub
	}

  private void buildAndSubmitLocal() throws Exception
  {
	  int TOP_N = 1000;
	    // create the topology
	    TopologyBuilder builder = new TopologyBuilder();

	    // now create the tweet spout with the credentials
	    // credential
	    TweetSpout tweetSpout = new TweetSpout(
	            "bxaPvISdZ8OvbKtEc069iGUnY",
	            "z71aEO7BAB4coPJclRy3MrvEm7nepVqzZNOukd29Jpc96FA4Wi",
	            "45456822-HAyi4j1opkfUzPYlt7RPJun4mk0DZ21qZnrwXAtTW",
	            "EibuYtIxzwB8WUCL6va0mirYdfdgYnkMBl3n7XfVOzsN2");

	    // attach the tweet spout to the topology - parallelism of 1
	    builder.setSpout("tweet-spout", tweetSpout, 1);

	    // attach the parse tweet bolt using shuffle grouping
	    builder.setBolt("parse-tweet-bolt", new ParseTweetBolt(), 10).shuffleGrouping("tweet-spout");
	    builder.setBolt("infoBolt", new InfoBolt(), 10).fieldsGrouping("parse-tweet-bolt", new Fields("county_id"));
	    builder.setBolt("top-words", new TopWords(), 10).fieldsGrouping("infoBolt", new Fields("county_id"));
	    builder.setBolt("report-bolt", new ReportBolt(), 1).globalGrouping("top-words");
	    builder.setBolt("data-bolt", HBaseUpdateBolt.make(topologyConfig), 1).shuffleGrouping("report-bolt");

	    // create the default config object
	    Config conf = new Config();

	    // set the config in debugging mode
	    conf.setDebug(true);

	    conf.setMaxTaskParallelism(4);

	      // create the local cluster instance
	      LocalCluster cluster = new LocalCluster();

	      // submit the topology to the local cluster
	      cluster.submitTopology("tweet-word-count", conf, builder.createTopology());

	      // let the topology run for 300 seconds. note topologies never terminate!
	      Utils.sleep(300000000);

	      // now kill the topology
	      cluster.killTopology("tweet-word-count");

	      // we are done, so shutdown the local cluster
	      cluster.shutdown();
  }
public static void main(String[] args) throws Exception
  {
	String configFileLocation = "topology-conf.properties";
	TopNTweetTopology topNTweetTopology = new TopNTweetTopology(configFileLocation);
	topNTweetTopology.buildAndSubmitLocal();
	/*
    //Variable TOP_N number of words
    int TOP_N = 1000;
    // create the topology
    TopologyBuilder builder = new TopologyBuilder();

    // now create the tweet spout with the credentials
    // credential
    TweetSpout tweetSpout = new TweetSpout(
            "bxaPvISdZ8OvbKtEc069iGUnY",
            "z71aEO7BAB4coPJclRy3MrvEm7nepVqzZNOukd29Jpc96FA4Wi",
            "45456822-HAyi4j1opkfUzPYlt7RPJun4mk0DZ21qZnrwXAtTW",
            "EibuYtIxzwB8WUCL6va0mirYdfdgYnkMBl3n7XfVOzsN2");

    // attach the tweet spout to the topology - parallelism of 1
    builder.setSpout("tweet-spout", tweetSpout, 1);

    // attach the parse tweet bolt using shuffle grouping
    builder.setBolt("parse-tweet-bolt", new ParseTweetBolt(), 10).shuffleGrouping("tweet-spout");
    builder.setBolt("infoBolt", new InfoBolt(), 10).fieldsGrouping("parse-tweet-bolt", new Fields("county_id"));
    builder.setBolt("top-words", new TopWords(), 10).fieldsGrouping("infoBolt", new Fields("county_id"));
    builder.setBolt("report-bolt", new ReportBolt(), 1).globalGrouping("top-words");
    builder.setBolt("data-bolt", HBaseUpdateBolt.make(topologyConfig), 1).shuffleGrouping("report-bolt");

    // create the default config object
    Config conf = new Config();

    // set the config in debugging mode
    conf.setDebug(true);

    if (args != null && args.length > 0) {

      // run it in a live cluster

      // set the number of workers for running all spout and bolt tasks
      conf.setNumWorkers(3);

      // create the topology and submit with config
      StormSubmitter.submitTopology(args[0], conf, builder.createTopology());

    }
    */
  }
}
