package controllers

import prototype._
import org.jclouds.cim.ResourceAllocationSettingData.ResourceType
import org.jclouds.openstack.nova.v2_0.extensions.ServerWithSecurityGroupsApi
import org.jclouds.openstack.nova.v2_0.features.ServerApi
import org.jclouds.ContextBuilder
import org.jclouds.openstack.nova.v2_0.NovaApi



abstract class Nova( origin: OpenstackOrg, resource: ResourceType ) {

  val novaApi = ContextBuilder.newBuilder( "openstack-nova" )
    .endpoint( origin.endpoint )
    .credentials( s"${origin.tenant}:${origin.user}", origin.secretKey )
    .buildApi( classOf[NovaApi] )

  val serverApi: ServerApi = novaApi.getServerApiForZone( origin.region )
  // appropriate security group mandatory
  val secgroupServerApi: ServerWithSecurityGroupsApi = novaApi.getServerWithSecurityGroupsExtensionForZone( origin.region )
    .get()

  /*
    Connection to model
   */
  def connect[Nova]( in: String ): Unit =
    println( "You are connected to the runtime model Nova" )

  def getInstances: Unit = {
    val instances = serverApi.list.concat.toList
  }

}
