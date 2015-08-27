package prototype

/**
 * Created by spyroslalos on 8/25/15.
 */
trait Origin {
  def vendor: String
  def account: String
  def filterMap: Map[String, String] = Map.empty
  def resources: Set[String]
  def standardFields: Map[String, String] = Map( "vendor" -> vendor, "accountName" -> account )
  def jsonFields: Map[String, String]
}

  case class OpenstackOrg( endpoint: String, elempath: String, region: String, tenant: String, user: String,
                         accessKey: String, resources: Set[String] )(val secretKey: String) extends Origin{

  lazy val vendor = "openstack"
  lazy val account = s"$tenant@region"
  override lazy val filterMap = Map( "vendor" -> vendor, "region" -> region, "account" -> tenant, "accountName" -> tenant )
  val jsonFields = Map( "region" -> region, "tenant" -> tenant )

}