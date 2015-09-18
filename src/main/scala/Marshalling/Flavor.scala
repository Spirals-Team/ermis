package Marshalling

import org.codehaus.jackson.annotate.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
case class Flavor(id: String,
                  name: String,
                  vcpus: String,
                  ram: String,
                  ephemeral: String,
                  swap: String,
                  rxtxFactor: String,
                  public: String
                   )

case class FlavorList(list: List[Flavor]){
  def get_list(): List[Flavor] ={
    return list
  }
}