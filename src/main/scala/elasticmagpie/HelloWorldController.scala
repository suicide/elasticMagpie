package elasticmagpie

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}

/**
 * Created by psy on 19.04.14.
 */
@Controller
class HelloWorldController {

  @RequestMapping(Array("/hello"))
  @ResponseBody
  def helloWorld(): String = {
    "Hello World"
  }

}
