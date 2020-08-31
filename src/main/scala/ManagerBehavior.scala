import java.math.BigInteger
import java.util

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.Behaviors.Receive
import akka.util.Timeout

import scala.concurrent.duration._
import scala.collection.SortedSet
import scala.util.{Failure, Success}

object ManagerBehavior {

  sealed trait Command
  case class Instruction(message: String) extends Command
  case class Result(result: BigInteger) extends Command
  case class NoResponseReceived(worker: ActorRef[WorkerBehavior.ManagerCommand]) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup[Command]{context =>
      implicit val timeout: Timeout = 3.seconds
      var primes = List[BigInteger]()
      val behavior = Behaviors.receiveMessage[Command]{
        case Instruction(message) => {
          if(message == "start") {
            for (i <- 1 to 20) {
              val worker = context.spawn(WorkerBehavior(),s"worker$i")
              context.ask(worker,WorkerBehavior.ManagerCommand){
                case Success(ManagerBehavior.Result(result)) => ManagerBehavior.Result(result)
                case Failure(exception) => NoResponseReceived(worker)
              }
            }
          }
          Behaviors.same
        }
        case Result(result) => {
          primes = primes :+ result
          println(s"I have received ${primes.size} prime numbers");
          if (primes.size == 20) {
            primes.foreach(println)
          }
          Behaviors.same
        }
        case NoResponseReceived(worker) => {}
          println(s"Retrying with worker ${worker.path}")
          context.ask(worker,WorkerBehavior.ManagerCommand){
            case Success(ManagerBehavior.Result(result)) => ManagerBehavior.Result(result)
            case Failure(exception) => NoResponseReceived(worker)
          }
          Behaviors.same
      }


      def askWorkerForAPrime(worker: ActorRef[WorkerBehavior.ManagerCommand]) = {
          context.ask(worker,WorkerBehavior.ManagerCommand){
            case Success(ManagerBehavior.Result(result)) => ManagerBehavior.Result(result)
            case Failure(exception) => NoResponseReceived(worker)
          }
      }
      behavior
    }

}
