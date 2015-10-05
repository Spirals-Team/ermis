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

    val conf = ConfigFactory.load( "nova_authentication.conf" ).getConfig( "nova_authentication" )

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
        System.exit( 0 )
      }
    }
    null
  }


  //================================================================================
  // Information Getters
  /*
    Get Flavors using Nova Service
   */
  def GetFlavors(nova_client: Nova): util.List[Flavor] ={

    val flavors = nova_client.flavors().list( true ).execute()
    return flavors.getList

  }

  /*
    Get Keypairs using Nova Service
   */
  def GetKeys(nova_client: Nova): util.List[KeyPair] ={

    val keys = nova_client.keyPairs().list().execute()
    //    print(flavors.getList.get(1))
    return keys.getList

  }

  /*
    Get Images using Nova Service
   */
  def GetServers(nova_client: Nova): util.List[Server] ={
    val servers = nova_client.servers().list( true ).execute()
    return servers.getList

  }

  /*
    Get Images using Nova Service
   */
  def GetImages(nova_client: Nova): util.List[Image] ={

    val images = nova_client.images().list(true).execute()
    //    print(flavors.getList.get(1))
    return images.getList
  }



  //================================================================================
  // Main Tester
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
    //get the Hypervisor of an Instance-VMs
    //GetServers(novaClient).get(0).getHostId
    //i.e. nova list --host e68acdd4a993707a4e11fe9a86e47552c58aaf190f95c9b7b089e76c
   // print(GetServers(novaClient).get(0))

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
    for (key <- keysSeq) {
      print(key.getName+"$###$########")
    }

    print(GetImages(novaClient))
    /*
    //==========================================

    val serverForCreate = new ServerForCreate()
    serverForCreate.setName("lalos_testing_vm")
    print("XAXAXA"+novaClient.flavors().list(true).execute().getList().get(3).getId)
    serverForCreate.setFlavorRef( "d2e0cd62-fe1c-41d3-890c-27cad5e45bcd" )
    serverForCreate.setImageRef( novaClient.images().list(true).execute().getList.get(0).getId() )
    serverForCreate.setKeyName(novaClient.keyPairs().list().execute().getList().get(2).getName())

    val server = novaClient.servers().boot(serverForCreate).execute()
    println(server.getUserId)

    */
    //==========================================

  }

}
