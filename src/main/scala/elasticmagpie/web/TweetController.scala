package elasticmagpie.web

import org.springframework.stereotype.Controller
import elasticmagpie.model.Tweet
import elasticmagpie.elasticsearch.TweetRepository
import org.springframework.web.bind.annotation.{RequestParam, ResponseStatus, ResponseBody, RequestMapping}
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by psy on 19.04.14.
 */
@Controller
class TweetController {

  @Autowired
  var tweetRepository: TweetRepository = _

  @RequestMapping(Array("/tweets"))
  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  def tweets(@RequestParam(value = "accounts", required = false) accounts: String,
             @RequestParam(value = "hashtags", required = false) hashtags: String): Seq[Tweet] = {

    var hashtagList: Seq[String] = null

    if (hashtags != null) {
      hashtagList = hashtags.split(",").map(s => s).toList
    }

    var accountList: Seq[String] = null

    if (accounts != null) {
      accountList = accounts.split(",").map(s => s).toList
    }

    tweetRepository.getTweets(accountList, hashtagList)

  }

}
