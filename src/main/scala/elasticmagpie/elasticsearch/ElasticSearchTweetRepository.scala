package elasticmagpie.elasticsearch

import elasticmagpie.model.Tweet
import org.elasticsearch.client.Client
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

/**
  * Created by psy on 19.04.14.
  */
class ElasticSearchTweetRepository(private val elasticSearchClient: Client) {

  private val objectMapper: ObjectMapper = new ObjectMapper
  objectMapper.registerModule(DefaultScalaModule)

  val index = "twitter"
  val typee = "tweet"

   def storeTweet(tweet: Tweet) = {

     val json = objectMapper.writeValueAsString(tweet)

     elasticSearchClient.prepareIndex(index, typee)
       .setId("" + tweet.id)
       .setSource(json).execute().actionGet()

   }

 }
