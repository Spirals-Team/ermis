package OpenstackAuth

import com.typesafe.config.ConfigFactory
import com.woorea.openstack.keystone.Keystone
import com.woorea.openstack.keystone.model.authentication.UsernamePassword
import com.woorea.openstack.keystone.utils.KeystoneUtils
import com.woorea.openstack.nova.Nova



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

  /*
    get available flavors
   */
  def flavor(arg: String): String ={


      val novaClient = getNovaClient()
      val flavors = novaClient.flavors().list( true ).execute()
      return Utils.returnJson( flavors )


  }

  def main(args: Array[String]) {

    val novaClient = getNovaClient()
    flavor( "flavor-list" )
  }

}
