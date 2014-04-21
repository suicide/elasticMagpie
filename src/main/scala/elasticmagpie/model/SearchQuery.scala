package elasticmagpie.model

import java.util.Date

/**
 * TODO: Comment
 *
 * @author Patrick Sy (patrick.sy@get-it.us)
 */
class SearchQuery {

  var accounts: Seq[String] = _

  var hashtags: Seq[String] = _

  var createdAt: Date = _


  def canEqual(other: Any): Boolean = other.isInstanceOf[SearchQuery]

  override def equals(other: Any): Boolean = other match {
    case that: SearchQuery =>
      (that canEqual this) &&
        accounts == that.accounts &&
        hashtags == that.hashtags
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(accounts, hashtags)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }
}
