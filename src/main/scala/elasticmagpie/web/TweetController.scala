package elasticmagpie.web

import org.springframework.stereotype.Controller
import elasticmagpie.model.{SearchQuery, Tweet}
import elasticmagpie.elasticsearch.{SearchQueryRepository, TweetRepository}
import org.springframework.web.bind.annotation.{RequestParam, ResponseStatus, ResponseBody, RequestMapping}
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import java.util.Date

/**
 * Created by psy on 19.04.14.
 */
@Controller
class TweetController {

  @Autowired
  var tweetRepository: TweetRepository = _

  @Autowired
  var searchQueryRepository: SearchQueryRepository = _

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

    val searchQuery = new SearchQuery
    searchQuery.accounts = accountList
    searchQuery.hashtags = hashtagList
    searchQuery.createdAt = new Date

    val queries = searchQueryRepository.findAll()

    if (!queries.contains(searchQuery)) {
      searchQueryRepository.store(searchQuery)
    }



    tweetRepository.getTweets(accountList, hashtagList)

  }

}
