import pirc.MalBot
import org.jibble.pircbot.{NickAlreadyInUseException,IrcException}
import com.typesafe.config.ConfigFactory

object Main extends App {
	
	/** Loads all key/value pairs from the application configuration file. */
	val conf = ConfigFactory.load
	
	val bot = new MalBot(conf)
	bot setSilent false
	
	lazy val oauth2 = conf.getString("twitch.Malbot.pass")
	
	try {
		bot.connect("irc.twitch.tv", 6667, oauth2)
	} catch {
		case e: NickAlreadyInUseException => println("Nickname is currently in use")
		case e: IrcException => println("Server did not accept connection")
		case _ : Throwable =>  println("WTF BRUH")
	}
}