package elasticmagpie.twitter4j

import twitter4j._
import org.elasticsearch.client.Client
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import elasticmagpie.elasticsearch.ElasticSearchTweetRepository

/**
 * Created by psy on 19.04.14.
 */
class SimpleStatusListener(private val elasticSearchTweetRepository: ElasticSearchTweetRepository) extends StatusListener {

  private val statusMapper: StatusTweetMapper = new StatusTweetMapper

  override def onException(ex: Exception): Unit = {}

  override def onStallWarning(warning: StallWarning): Unit = {}

  override def onScrubGeo(userId: Long, upToStatusId: Long): Unit = {}

  override def onTrackLimitationNotice(numberOfLimitedStatuses: Int): Unit = {}

  override def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice): Unit = {}

  override def onStatus(status: Status): Unit = {
    System.out.println(status.getUser.getName + " " + status.getText)

    val tweet = statusMapper.map(status)

    elasticSearchTweetRepository.storeTweet(tweet)



  }
}
