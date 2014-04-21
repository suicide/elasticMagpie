package elasticmagpie.elasticsearch

import org.elasticsearch.client.Client
import com.fasterxml.jackson.databind.ObjectMapper
import elasticmagpie.model.SearchQuery
import org.elasticsearch.index.query.QueryBuilders

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
class SearchQueryRepository(private val client: Client, private val objectMapper: ObjectMapper) {

  val index = "twitter"
  val typee = "searchquery"
  val maxResults = 1000

  def findAll(): Seq[SearchQuery] = {
    val response = client.prepareSearch(index).setTypes(typee)
    .setQuery(QueryBuilders.matchAllQuery())
    .setSize(maxResults)
    .execute().actionGet()


    response.getHits().getHits.map(
      hit => objectMapper.readValue(hit.getSourceAsString, classOf[SearchQuery])
    ).toList
  }

  def store(searchQuery: SearchQuery): Unit = {
    val json = objectMapper.writeValueAsString(searchQuery)

    client.prepareIndex(index, typee)
      .setSource(json).execute().actionGet()
  }

}
