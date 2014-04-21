package elasticmagpie.elasticsearch

import elasticmagpie.model.Tweet
import org.elasticsearch.client.Client
import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.index.query.{QueryBuilders, QueryBuilder}
import org.elasticsearch.search.sort.{SortOrder, SortBuilders}
import scala.collection.JavaConversions

/**
  * Created by psy on 19.04.14.
  */
class TweetRepository(private val client: Client, private val objectMapper: ObjectMapper) {

  val index = "twitter"
  val typee = "tweet"

   def storeTweet(tweet: Tweet) = {

     val json = objectMapper.writeValueAsString(tweet)

     client.prepareIndex(index, typee)
       .setId("" + tweet.id)
       .setSource(json).execute().actionGet()

   }

  def getTweets(accounts: Seq[String], hashtags: Seq[String]): Seq[Tweet] = {

    val query = QueryBuilders.boolQuery()

    if (accounts != null && !accounts.isEmpty) {
      query.must(QueryBuilders.inQuery("user", JavaConversions.seqAsJavaList(accounts)))
    }

    if (hashtags != null && !hashtags.isEmpty) {
      query.must(QueryBuilders.inQuery("hashtags", JavaConversions.seqAsJavaList(hashtags)))
    }

    val response = client.prepareSearch(index).setTypes(typee)
      .setQuery(query)
      .addSort(SortBuilders.fieldSort("createdAt").order(SortOrder.DESC))
      .execute().actionGet()

    val tweets = response.getHits().getHits.map(
      hit => objectMapper.readValue(hit.getSourceAsString, classOf[Tweet])
    ).toList

    tweets

  }

 }
