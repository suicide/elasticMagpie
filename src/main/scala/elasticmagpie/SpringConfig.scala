package elasticmagpie

import _root_.twitter4j.conf.{ConfigurationBuilder, ConfigurationContext, ConfigurationFactory}
import _root_.twitter4j.{TwitterStreamFactory, StatusListener}
import org.springframework.context.annotation._
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.beans.factory.InitializingBean
import elasticmagpie.twitter4j.SimpleStatusListener
import org.springframework.core.env.Environment
import org.springframework.beans.factory.annotation.Autowired
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import elasticmagpie.elasticsearch.ElasticSearchTweetRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter

/**
 * Created by psy on 19.04.14.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
//@PropertySource(value = Array("classpath:/default-application.properties", "classpath:/application.properties"))
class SpringConfig extends WebMvcAutoConfigurationAdapter with InitializingBean {

  @Autowired
  var springEnv: Environment = null

  @Bean
  @Primary
  def jacksonObjectMapper(): ObjectMapper = {
    new ObjectMapper() {
      registerModule(DefaultScalaModule)
    }
  }

  @Bean
  def elasticSearchClient(): Client = {
    new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300))
  }

  @Bean
  def elasticSearchTweetRepository(): ElasticSearchTweetRepository = {
    new ElasticSearchTweetRepository(elasticSearchClient(), jacksonObjectMapper())
  }

  @Bean
  def statusListener(): StatusListener = {
    new SimpleStatusListener(elasticSearchTweetRepository())
  }



  override def afterPropertiesSet(): Unit = {

    val configurationBuilder = new ConfigurationBuilder
    configurationBuilder.setJSONStoreEnabled(true)
    configurationBuilder.setOAuthConsumerKey(springEnv.getRequiredProperty("twitter.consumerKey"))
    configurationBuilder.setOAuthConsumerSecret(springEnv.getRequiredProperty("twitter.consumerSecret"))
    configurationBuilder.setOAuthAccessToken(springEnv.getRequiredProperty("twitter.accessToken"))
    configurationBuilder.setOAuthAccessTokenSecret(springEnv.getRequiredProperty("twitter.accessTokenSecret"))

    val stream = new TwitterStreamFactory(configurationBuilder.build()).getInstance()

    stream.addListener(statusListener())

    stream.sample()
  }
}
