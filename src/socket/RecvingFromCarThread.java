package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecvingFromCarThread extends MyThread implements Runnable {
	RecvingFromCarThread(Socket socket){
		super(socket);
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			/*byte[] bytes = new byte[1024];
			int a = socket.getInputStream().read(bytes);
			String b = new String(bytes,0,a,"utf-8");*/
			//first handshake
			String s = readPackage(3,br);
			String host = getFieldValue(s,"HOST");
			String password = getFieldValue(s,"CODE");
			
			this.handShake(s, br, host, password);
			
			while(true) {
				
				s = readPackage(2,br);
				if(s.indexOf("LOCATION")!=-1||s.indexOf("PICTURE")!=-1
						) {
					this.storeUserInfo(s);
				}

				if(s.indexOf("DISCONNECT")!=-1) {
					socket.getOutputStream().write("GET DISCONNECT".getBytes("utf-8"));
					
					byte[] bytes = new byte[1024];
					int len = socket.getInputStream().read(bytes);
					if(new String(bytes,0,len,"utf-8").equals("ALREADY DISCONNECT"))
					break;
				}
			}
			synchronized(Server.userState.get(host)) {
				Server.userState.get(host).online = false;
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	private void storeUserInfo(String s) {
		String command = s.split("\r\n")[1];
		int start = s.indexOf(":")+1;
		String userName = command.split("\\s")[0].substring(start);
		synchronized(Server.userState.get(userName)) {
			if(Server.userState.get(userName).online) {
				Server.userState.get(userName).info.add(s);
				Server.userState.get(userName).notifyAll();
			}
			else {
				//TODO
			}
		}
	}
}
