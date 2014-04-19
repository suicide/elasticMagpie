package elasticmagpie.elasticsearch

import elasticmagpie.model.Tweet
import org.elasticsearch.client.Client
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.elasticsearch.index.query.{QueryBuilders, QueryBuilder}
import org.elasticsearch.search.sort.{SortOrder, SortBuilders}

/**
  * Created by psy on 19.04.14.
  */
class ElasticSearchTweetRepository(private val client: Client, private val objectMapper: ObjectMapper) {

  val index = "twitter"
  val typee = "tweet"

   def storeTweet(tweet: Tweet) = {

     val json = objectMapper.writeValueAsString(tweet)

     client.prepareIndex(index, typee)
       .setId("" + tweet.id)
       .setSource(json).execute().actionGet()

   }

  def getTweets(searchText: String): Seq[Tweet] = {

    val response = client.prepareSearch(index).setTypes(typee)
      .setQuery(QueryBuilders.matchQuery("text", searchText))
      .addSort(SortBuilders.fieldSort("createdAt").order(SortOrder.DESC))
      .execute().actionGet()

    val tweets = response.getHits().getHits.map(
      hit => objectMapper.readValue(hit.getSourceAsString, classOf[Tweet])
    ).toList

    tweets

  }

 }
