package elasticmagpie.web

import org.springframework.stereotype.Controller
import elasticmagpie.model.Tweet
import elasticmagpie.elasticsearch.ElasticSearchTweetRepository
import org.springframework.web.bind.annotation.{RequestParam, ResponseStatus, ResponseBody, RequestMapping}
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by psy on 19.04.14.
 */
@Controller
class TweetController {

  @Autowired
  private var tweetRepository: ElasticSearchTweetRepository = _

  @RequestMapping(Array("/tweets"))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def tweets(@RequestParam("text") text: String): Seq[Tweet] = {

    tweetRepository.getTweets(text)

  }

}
