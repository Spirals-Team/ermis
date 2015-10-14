package openstackAuth

import java.util

import com.typesafe.config.ConfigFactory
import com.woorea.openstack.keystone.Keystone
import com.woorea.openstack.keystone.model.authentication.UsernamePassword
import com.woorea.openstack.keystone.utils.KeystoneUtils
import com.woorea.openstack.nova.Nova
import com.woorea.openstack.nova.model.{Flavor, Image, KeyPair, Server}


object NovaConnector {


  def getNovaClient(): Nova = {

    val conf = ConfigFactory.load( "nova_authentication.conf" )
      .getConfig( "nova_authentication" )

    try {

      val keystoneClient = new Keystone( conf.getString( "keystone_auth_url" ) )
      val access = keystoneClient.tokens().authenticate( new UsernamePassword( conf.getString( "tenant_name" ),
        conf.getString( "tenant_password" ) ) )
        .withTenantName( conf.getString( "tenant_name" ) )
        .execute()
      val novaEndpoint = KeystoneUtils.findEndpointURL( access.getServiceCatalog, "compute", null, "public" )
      val novaClient = new Nova( novaEndpoint )
      novaClient.token( access.getToken.getId )
      return novaClient

    } catch {

      case e: Exception => {
        e.printStackTrace()
        println( "Failed to create/initialize a Nova client." )
        throw e
        System.exit( 0 )
      }

    }
    null
  }



  /*
    Get Flavors using Nova Service
   */
  def GetFlavors(nova_client: Nova): util.List[Flavor] ={

    try {

      val flavors = nova_client.flavors().list(true).execute()
      return flavors.getList

    } catch {

      case e: Exception => {
        e.printStackTrace()
        throw e
      }

    }
  }

  /*
    Get Keypairs using Nova Service
   */
  def GetKeys(nova_client: Nova): util.List[KeyPair] ={

    try {

      val keys = nova_client.keyPairs().list().execute()
      return keys.getList

    } catch {

      case e: Exception => {
        e.printStackTrace()
        throw e
      }

    }
  }

  /*
    Get Instances/Servers using Nova Service
   */
  def GetServers(nova_client: Nova): util.List[Server] ={

    try {

      val servers = nova_client.servers().list(true).execute()
      return servers.getList

    } catch {

        case e: Exception => {
          e.printStackTrace()
          throw e
      }

    }
  }

  /*
    Get Images using Nova Service
   */
  def GetImages(nova_client: Nova): util.List[Image] ={

    try {

      val images = nova_client.images().list(true).execute()
      return images.getList

    } catch {

      case e: Exception => {
        e.printStackTrace()
        throw e
      }

    }
  }

  /*
  Pretty print of a Java List using
  new lines between object of the collection
  Useful for Nova Get<X> functions
 */
  def prettyPrint[T] ( list: util.List[T]): String={
    var result=""
    var x = 0

    for ( x <- 0 to (list.size()-1) ) {
      result += list.get(x).toString + "\n\n"
    }
    result
  }


  //===================================
  // Main Tester
  //===================================
  def main (args: Array[String]): Unit = {

    println("Starting main Nova Connector")
    val novaClient = NovaConnector.getNovaClient()

    //get keypairs
    print( "KEY_2" +novaClient.keyPairs().list().execute().getList().get(2).getName() )
    // get the first image
    print( "IMAGE_0" +novaClient.images().list(true).execute().getList.get(0).getName )
    // get flavor list
    print( "NAME_3" +novaClient.flavors().list(true).execute().getList().get(3).getName )

    print("NOW get the servers")
    print(GetServers(novaClient))

    //convert a java.util to Scala list
    val serversSeq = scala.collection.JavaConversions.asScalaBuffer(GetFlavors(novaClient))
    for (server <- serversSeq) {
      print("")
    }
    print(serversSeq)

    var FlavorsSeq = scala.collection.JavaConversions.asScalaBuffer(NovaConnector.GetFlavors(novaClient))
    for (flavor <- FlavorsSeq) {
      print(flavor+"$###$########")
    }

    var keysSeq = scala.collection.JavaConversions.asScalaBuffer(NovaConnector.GetKeys(novaClient))
    //  print(FlavorsSeq)
    for (key <- keysSeq) {
      print(key.getName+"$###$########")
    }

    print(GetImages(novaClient))

  }

}
