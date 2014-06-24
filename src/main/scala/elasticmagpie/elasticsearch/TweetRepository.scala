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

   def storeTweet(tweet: Tweet) = {

     val json = objectMapper.writeValueAsString(tweet)

     client.prepareIndex(TweetRepository.index, TweetRepository.index)
       .setId("" + tweet.id)
       .setSource(json).execute().actionGet()

   }

  def getTweets(accounts: Set[String], hashtags: Set[String]): Seq[Tweet] = {

    val query = QueryBuilders.boolQuery()

    def inQuery = (set: Set[String], field: String) => {
      if (set != null && !set.isEmpty) {
        // ES needs this lower case hack, because index is lower case
        query.must(QueryBuilders.inQuery(field, JavaConversions.seqAsJavaList(set.map(_.toLowerCase).toList)))
      }
    }

    inQuery(accounts, "user")
    inQuery(hashtags, "hashtags")

    val response = client.prepareSearch(TweetRepository.index).setTypes(TweetRepository.index)
      .setQuery(query)
      .addSort(SortBuilders.fieldSort("createdAt").order(SortOrder.DESC))
      .execute().actionGet()

    response.getHits().getHits.map(
      hit => objectMapper.readValue(hit.getSourceAsString, classOf[Tweet])
    ).toList

  }

 }

object TweetRepository {
  val index = "twitter"
  val typee = "tweet"
}
