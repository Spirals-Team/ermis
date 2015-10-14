package dsl

import com.woorea.openstack.nova.model.Server


class NovaInstance( private var instanceName: String, private var instanceFlavor: String,
                    private var instanceImage: String, private var instanceKeyPair: String)
                    extends Server{

  var vmname = instanceName
  var vmflavor = instanceFlavor
  var vmimage = instanceImage
  var vmkeyPair = instanceKeyPair

  // Getters
  def _name = vmname
  def _flavor = vmflavor
  def _image = vmimage
  def _keyPair = vmkeyPair

  // Setters

  def name_= ( value: String ) { vmname = value}
  def flavor_= (value: String):Unit = vmflavor = value
  def image_= (value: String):Unit = vmimage = value
  def keyPair_= (value: String):Unit = vmkeyPair = value

}
