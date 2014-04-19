package elasticmagpie.web

import org.springframework.stereotype.Controller
import elasticmagpie.model.Tweet
import elasticmagpie.elasticsearch.ElasticSearchTweetRepository
import org.springframework.web.bind.annotation.{ResponseStatus, ResponseBody, RequestMapping}
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import java.util
import scala.collection.JavaConversions

/**
 * Created by psy on 19.04.14.
 */
@Controller
class TweetController {

  @Autowired
  private val tweetRepository: ElasticSearchTweetRepository = null

  @RequestMapping(Array("/tweets"))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def tweets(): Seq[Tweet] = {

    tweetRepository.getTweets()

  }

}
