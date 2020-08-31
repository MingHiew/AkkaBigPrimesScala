import java.math.BigInteger
import java.util.Random

import ManagerBehavior.Result
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors


object WorkerBehavior {
  case class ManagerCommand(sender: ActorRef[ManagerBehavior.Result])

  def apply(): Behavior[ManagerCommand] = {
    case ManagerCommand(sender) => {
      val bigInteger: BigInteger = new BigInteger(2000, new Random)
      val prime = bigInteger.nextProbablePrime()
      val r = new Random
      if (r.nextInt(5) < 2) {
        sender ! Result(prime)
      }
      handleMessagesWhenWeAlreadyHaveAPrimeNumber(prime)
    }
  }

  def handleMessagesWhenWeAlreadyHaveAPrimeNumber(prime: BigInteger):Behavior[ManagerCommand] = {
    case ManagerCommand(sender) => {
      val r = new Random();
      if (r.nextInt(5) < 2) {
        sender ! Result(prime)
      }
      Behaviors.same
    }
  }

}
