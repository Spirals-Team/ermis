package Marshalling

import org.codehaus.jackson.annotate.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
case class Image(id: String,
                  name: String,
                  vcpus: String,
                  ram: String,
                  ephemeral: String,
                  swap: String,
                  rxtxFactor: String,
                  public: String
                   )

case class ImageList(list: List[Flavor]){
}