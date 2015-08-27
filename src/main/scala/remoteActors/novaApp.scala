package remoteActors

import java.io.File

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object novaApp extends App {


  val configFileINS = getClass.getClassLoader.
    getResource("remote_application.conf").getFile
  val configINS = ConfigFactory.parseFile(new File(configFileINS))
  val remote_INS = ActorSystem("RemoteINSSystem" , configINS)


  val configFileHPV = getClass.getClassLoader.
    getResource("local_application.conf").getFile
  val configHPV = ConfigFactory.parseFile(new File(configFileHPV))
  val remote_HPV = ActorSystem("RemoteHPVSystem" , configHPV)


  val insref = remote_INS.actorOf(Props[Instance], "novains")
  val hpvref = remote_HPV.actorOf(Props[Hypervisor], "novahpv")

  //check
  println (insref.path)
  println (hpvref.path)
  //send message to hpvref


  hpvref ! "ping"

  //shut down the actor system
  remote_HPV.shutdown()
  remote_HPV.shutdown()

}