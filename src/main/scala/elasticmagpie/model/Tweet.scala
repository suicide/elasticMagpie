package elasticmagpie.model

import java.util.Date

/**
 * Created by psy on 19.04.14.
 */
class Tweet {

  var id: String = _

  var text: String = _

  var user: String = _

  var hashtags: Seq[String] = _

  var createdAt: Date = _

}
