package dsl


class NovaModel {

  def getInstances(): Unit={

    Requester.Request( inputRequest="instances", null )

  }

}

object NovaModel {

  def apply() = new NovaModel().getInstances()

  //Request for connection to Nova Service
  def connect(): Unit ={

    Requester.Request( inputRequest="connect", null)

  }

  def flavors() = {

    Requester.Request( inputRequest="flavors", null )

  }

  def images() = {

    Requester.Request( inputRequest="images", null )

  }

  def keyPairs() = {

    Requester.Request( inputRequest="keyPairs", null )

  }

  def +=(instance: NovaInstance): Unit ={

    Requester.Request( inputRequest="create_instance", instance )

  }


}
