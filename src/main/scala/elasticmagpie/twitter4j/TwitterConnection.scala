package elasticmagpie.twitter4j

import twitter4j.conf.Configuration
import twitter4j.{TwitterStream, TwitterStreamFactory, StatusListener}

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
class TwitterConnection(private val config: Configuration, private val statusListener: StatusListener) {

  private var stream: TwitterStream = _

  def connect(): Unit = {

    this.synchronized {
      stream = new TwitterStreamFactory(config).getInstance()

      stream.addListener(statusListener)

      stream.sample()
    }
  }

  def disconnect(): Unit = {

    this.synchronized {
      stream.shutdown()
    }

  }

}
