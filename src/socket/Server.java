package socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
	static Map<String,String> clientMap = Collections.synchronizedMap(new HashMap<String,String>());
	static Map<String,State> clientState = Collections.synchronizedMap(new HashMap<String,State>());
	static Map<String,String> carMap = Collections.synchronizedMap(new HashMap<String,String>());
	static Map<String,State> carState = Collections.synchronizedMap(new HashMap<String,State>());
	public static void main(String[] args) {
		Server s = new Server();
		s.mainFuction();
	}
	public Server() {
		File file = new File("src\\doc\\users.txt");
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String line;
			while((line = buffer.readLine())!=null) {
				String[] user = line.split("\\s");
				clientMap.put(user[0], user[1]);
				clientState.put(user[0], new State());
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("loading users file failed");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file1 = new File("src\\doc\\cars.txt");
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(file1));
			String line;
			while((line = buffer.readLine())!=null) {
				String[] car = line.split("\\s");
				carMap.put(car[0], car[1]);
				carState.put(car[0], new State());
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void mainFuction() {
		new Thread(new ServerThread()).start();
		int port = 55533;
		ServerSocket server;
		try {
			server = new ServerSocket(port, 20);
			System.out.println("Sending server Built");
			while (true) {
				Socket socket = server.accept();
				
				System.out.println("Recving connection is built");
				new Thread(new RecvingThread(socket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
class ServerThread implements Runnable{
	@Override
	public void run() {
		int port = 55532;
		ServerSocket server;
		try {
			server = new ServerSocket(port, 20);
			System.out.println("Sending Server Built");
			while (true) {
				Socket socket = server.accept();
				System.out.println("Sending connection is built");
				new Thread(new SendingThread(socket)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
class State{
	List<String> info=Collections.synchronizedList(new ArrayList<>());
	Boolean online=false;
}
