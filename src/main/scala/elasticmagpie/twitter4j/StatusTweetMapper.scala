package elasticmagpie.twitter4j

import twitter4j.Status
import elasticmagpie.model.Tweet

/**
 * Created by psy on 19.04.14.
 */
class StatusTweetMapper {

  def map(status: Status): Tweet = {
    new Tweet("" + status.getId,
      status.getText,
      status.getUser.getScreenName,
      status.getHashtagEntities.map(_.getText).toSet,
      status.getCreatedAt)

  }

}
