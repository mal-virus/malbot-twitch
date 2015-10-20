package pirc

import org.jibble.pircbot._
import Array._
import com.typesafe.config.{Config,ConfigFactory}
import scala.collection.mutable.{Map => MutableMap }

class MalBot(conf: Config = ConfigFactory.load) extends PircBot {

	lazy private val admin = conf.getString("twitch.Malbot.admin")
	lazy private val myself = conf.getString("twitch.Malbot.name")
	lazy private val bottingFor: Array[String] = conf.getString("twitch.users").split(",").map(user => "#" + user.toLowerCase) 	
	
	private var silent = true
	private val ourBot: MutableMap[String,Commands] = MutableMap()
	ourBot += ("!scream" -> new Scream())
	ourBot += ("!halt"-> new Shutdown())

	setName(myself)
	setLogin(myself.toLowerCase)
	
	def setSilent(b: Boolean = true ) = {
		silent = b
	}

	override def onConnect() = {
		println("Connected to Twitch!")
		for(channel <- bottingFor)
			joinChannel(channel)
	}

	override def onDisconnect() = {
		dispose
	}

	override def onJoin(channel: String, sender: String, login: String, hostname: String) = {
		println(login + " joined channel " + channel)
	}

	override def onKick(channel: String, kickerNick: String, kickerLogin: String, kickerHostname: String, recipientNick: String, reason: String) = {
		if (recipientNick equalsIgnoreCase getNick ) {
    			joinChannel(channel)
		}
		else println("[" + channel + "]: Later " + recipientNick + "!")
	}

	override def onUserList(channel: String, users: Array[User]) = {
		println("Channel: " + channel + "\nUsers:")
		for ( user <- users) {
			println(user)
		}
		println
	}
	
	override def onPrivateMessage(sender: String, login: String, hostname: String, message: String) = {
		sendMessage(admin, "MalBot PM:\n"+message)
	}

	override def onMessage(channel: String, sender: String, login: String, hostname: String, message: String) = {
		if(!silent) {
			if(message startsWith "!")  {
				var command = message.split(" ")(0)
				if(ourBot contains command)
					ourBot(command).handle(channel, sender, login, hostname, message)
				else
					println("[" + channel + "](" + sender + ")???: " + message)
			}
			else {
				// What do we do with the rest of the messages?
			}
		}
	}

	class Scream extends Commands with Mod {
		def handle(channel: String, sender: String, login: String, hostname: String, message: String) = {
			if(isAdmin(sender))
				sendMessage(channel, "AAAAAAAAAH")
		}
	}

	class Shutdown extends Commands with Mod {
		def handle(channel: String, sender: String, login: String, hostname: String, message: String) = {
			if(isAdmin(sender)) disconnect
		}
	}

	trait Commands {
	def handle(channel: String, sender: String, login: String, hostname: String, message: String)
	}

	trait Mod {
		
		// Need to figure this out...
		def isMod(user: String, channel: String): Boolean = {
			
			true
		}

		def isAdmin(user: String): Boolean = {		
			user equalsIgnoreCase admin
		}
	}
}