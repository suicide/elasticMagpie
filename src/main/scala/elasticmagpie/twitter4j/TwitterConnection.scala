package elasticmagpie.twitter4j

import twitter4j.conf.Configuration
import twitter4j.{FilterQuery, TwitterStream, TwitterStreamFactory, StatusListener}
import elasticmagpie.elasticsearch.SearchQueryRepository

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
class TwitterConnection(private val config: Configuration, private val statusListener: StatusListener,
                         private val searchQueryRepository: SearchQueryRepository) {

  private var stream: TwitterStream = _

  def connect(): Unit = {

    this.synchronized {
      stream = new TwitterStreamFactory(config).getInstance()

      stream.addListener(statusListener)

      stream.filter(getFilterQuery())

    }
  }

  private def getFilterQuery(): FilterQuery = {
    val searchQueries = searchQueryRepository.findAll()

    val track = searchQueries.flatMap(query => {
      if (query.hashtags == null)  List() else  query.hashtags
    }).toSet

    val filterQuery = new FilterQuery()
    filterQuery.track(track.toArray)


    return filterQuery
  }

  def disconnect(): Unit = {

    this.synchronized {
      stream.shutdown()
    }

  }

}
