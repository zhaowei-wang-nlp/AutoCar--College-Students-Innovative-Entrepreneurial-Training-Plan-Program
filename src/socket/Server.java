package socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
	static Map<String,String> userMap = new HashMap<String,String>();
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
				userMap.put(user[0], user[1]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("loading users file failed");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void mainFuction() {
		int port = 55533;
		ServerSocket server;
		try {
			server = new ServerSocket(port, 20);
			while (true) {
				Socket socket = server.accept();
				System.out.println("Connection is built");
				new Thread(new subThread(socket)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class subThread implements Runnable{
	private Socket socket;
	subThread(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String s = readPackage(3,br);
			String host = getField(s,"HOST");
			String password = getField(s,"CODE");
			String rightPassword = Server.userMap.get(host);
			if(rightPassword==null) {
				throw new Exception("Invalid user name");
			}
			if(!Server.userMap.get(host).equals(password)) {
				throw new Exception("Wrong password");
			}
			System.out.println("user confirtmed");
			socket.getOutputStream().write(("HOST:"+host+"\r\n"+"FUCTION:PERMISSION").getBytes("utf-8"));
			System.out.println("PERSION sent");
			s = readPackage(2, br);
			String confirmHost = getField(s,"HOST");
			if(!(confirmHost.equals(host)&&s.indexOf("BUILD")!=-1)) {
				throw new Exception("Wrong confirming");
			}
			socket.getInputStream().close();
			socket.getOutputStream().close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private String getField(String s,String field) {
		int start = s.indexOf(field+":")+field.length()+1;
		int end = s.indexOf("\r\n");
		return s.substring(start, end);
	}
	private String readPackage(int n,BufferedReader br) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<n;i++) {
			try {
				sb.append(br.readLine()+"\r\n");
			} catch (IOException e) {
				System.out.println("Read Packet Wrongly");
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
}