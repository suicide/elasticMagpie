package elasticmagpie

import java.util.Date

import _root_.twitter4j.conf.{ConfigurationBuilder, ConfigurationContext, ConfigurationFactory}
import _root_.twitter4j.{TwitterStreamFactory, StatusListener}
import elasticmagpie.model.SearchQuery
import org.elasticsearch.action.admin.indices.create.{CreateIndexRequest, CreateIndexAction}
import org.elasticsearch.indices.IndexAlreadyExistsException
import org.springframework.context.annotation._
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.beans.factory.InitializingBean
import elasticmagpie.twitter4j.{TwitterConnection, SimpleStatusListener}
import org.springframework.core.env.Environment
import org.springframework.beans.factory.annotation.Autowired
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.transport.InetSocketTransportAddress
import elasticmagpie.elasticsearch.{SearchQueryRepository, TweetRepository}
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
  var springEnv: Environment = _

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
  def elasticSearchTweetRepository(): TweetRepository = {
    new TweetRepository(elasticSearchClient(), jacksonObjectMapper())
  }

  @Bean
  def searchQueryRepository(): SearchQueryRepository = {
    new SearchQueryRepository(elasticSearchClient(), jacksonObjectMapper())
  }

  @Bean
  def statusListener(): StatusListener = {
    new SimpleStatusListener(elasticSearchTweetRepository())
  }

  @Bean(initMethod = "connect", destroyMethod = "disconnect")
  def twitterConnection(): TwitterConnection = {

    val configurationBuilder = new ConfigurationBuilder
    configurationBuilder.setJSONStoreEnabled(true)
    configurationBuilder.setOAuthConsumerKey(springEnv.getRequiredProperty("twitter.consumerKey"))
    configurationBuilder.setOAuthConsumerSecret(springEnv.getRequiredProperty("twitter.consumerSecret"))
    configurationBuilder.setOAuthAccessToken(springEnv.getRequiredProperty("twitter.accessToken"))
    configurationBuilder.setOAuthAccessTokenSecret(springEnv.getRequiredProperty("twitter.accessTokenSecret"))

    new TwitterConnection(configurationBuilder.build(), statusListener(), searchQueryRepository())
  }



  override def afterPropertiesSet(): Unit = {

    try {
      elasticSearchClient().admin().indices().create(new CreateIndexRequest("twitter")).actionGet()

      searchQueryRepository().store(new SearchQuery(Set(), Set("sexy"), new Date()))
    } catch {
      case e: IndexAlreadyExistsException => println("Index does already exist")
    }

  }
}
