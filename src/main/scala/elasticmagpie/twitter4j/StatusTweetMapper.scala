package elasticmagpie.twitter4j

import twitter4j.Status
import elasticmagpie.model.Tweet

/**
 * Created by psy on 19.04.14.
 */
class StatusTweetMapper {

  def map(status: Status): Tweet = {
    val tweet = new Tweet

    tweet.id = "" + status.getId
    tweet.text = status.getText
    tweet.user = status.getUser.getScreenName
    tweet.hashtags = status.getHashtagEntities.map(_.getText).toList

    tweet
  }

}
