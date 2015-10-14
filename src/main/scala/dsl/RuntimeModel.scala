package dsl


class RuntimeModel {

  val instances = NovaModel

  def connect(): Unit={
    NovaModel.connect()
  }

  def flavors () ={
    NovaModel.flavors()
  }

}
