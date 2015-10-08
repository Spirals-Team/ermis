package openstackApi.domain


//=============================
//RESULT MESSAGES

sealed trait ResultMessage

case class Success2(message: String) extends ResultMessage

case class Error(message: String)

//=============================
// REQUEST MESSAGES

sealed trait RequestMessage

case class Get(id: String) extends RequestMessage

case class CreateVMrq(name: String, flavor: String, image: String, keyname: String)