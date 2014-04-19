package elasticmagpie.model

import java.util.Date

/**
 * Created by psy on 19.04.14.
 */
class Tweet {

  var id: String = null

  var text: String = null

  var user: String = null

  var hashtags: Seq[String] = null

  var createdAt: Date = null

}
