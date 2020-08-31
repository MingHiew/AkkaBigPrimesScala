import akka.actor.typed.ActorSystem

object Main extends App{

  val bigPrimes = ActorSystem(ManagerBehavior(),"BigPrimes")

  val result = bigPrimes ! ManagerBehavior.Instruction("start")

}
