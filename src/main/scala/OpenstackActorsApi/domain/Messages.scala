package OpenstackActorsApi.domain


//=============================
//RESULT MESSAGES
sealed trait ResultMessage

case class Created(location: String) extends ResultMessage

case class Success(message: String) extends ResultMessage

//case class ResultItem(items: List[String]) extends ResultMessage

case class ResultItem(item: String) extends ResultMessage

case class Error(message: String)

//=============================
// REQUEST MESSAGES
sealed trait RequestMessage

case class Get(id: String) extends RequestMessage

//returns Success Message if we manage to connect to
// Openstack
case class Connect(id: Long) extends RequestMessage

case object All extends RequestMessage
