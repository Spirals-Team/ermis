package dsl


class RuntimeModel {

  val instances = NovaModel

  // =====================================
  // Functions requesting Nova information
  // =====================================
  def connect() ={
    NovaModel.connect()
  }

  def flavors () ={
    NovaModel.flavors()
  }

  def images () ={
    NovaModel.images()
  }

  def keyPairs () ={
    NovaModel.keyPairs()
  }

}
