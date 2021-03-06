package multicast_chat;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

import javax.swing.UIManager;

import multicast_chat.interfaces.console.ConsoleInterface;
import multicast_chat.interfaces.gui.GraphicalInterface;
import multicast_chat.network.MessagesManager;
import multicast_chat.network.MulticastConnection;

public class MulticastChat {
	
	public static void main(String[] args) throws IOException {
		
		try {
			// donne à l'interface graphique le thème associé au système d'exploitation
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		InetAddress host = InetAddress.getByName("224.0.0.1");
		int port = 7654;
		
		MulticastConnection connection = new MulticastConnection(host, port);
		
		MessagesManager messagesManager = new MessagesManager(connection);
		
		connection.setMessageManager(messagesManager);
		
		
		if (args.length > 0 && args[0].equalsIgnoreCase("spam")) {
			// spam le réseau pour tester les performances de l'interface graphique
			Random r = new Random();
			for (int i=0; i<10000; i++) {
				try { Thread.sleep(10); } catch (InterruptedException e) { }
				messagesManager.send(r.nextInt(100)+"");
			}
		}
		
		else if (System.console() != null) {
			// lancé via une console, ou en utilisant (pour Windows) l'exécutable java.exe au lieu de javaw.exe
			ConsoleInterface console = new ConsoleInterface(messagesManager);
			
			messagesManager.addInterface(console);
			
			console.loop();
		}
		else {
			GraphicalInterface ui = new GraphicalInterface(messagesManager);
			
			messagesManager.addInterface(ui);
			
			ui.waitForDispose();
		}
		
		
		
		connection.close();
	}

}
