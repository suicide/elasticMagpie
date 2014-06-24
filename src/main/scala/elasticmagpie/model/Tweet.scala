package elasticmagpie.model

import java.util.Date

/**
 * Created by psy on 19.04.14.
 */
class Tweet(val id: String, val text: String, val user: String, val hashtags: Set[String], val createdAt: Date) {

}
