package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RecvingFromUserThread extends MyThread implements Runnable{
	RecvingFromUserThread(Socket socket){
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
				if(s.indexOf("MOVE")!=-1||s.indexOf("GETPICTURE")!=-1
						||s.indexOf("GETPICTURE")!=-1||
						s.indexOf("STOPPICTURE")!=-1) {
					this.storeCarInfo(s);
				}

				//HOST:xxx\r\n
				//DISCONNECT
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
	private void storeCarInfo(String s) {
		String command = s.split("\r\n")[1];
		String carName = command.split("\\s")[0];
		synchronized(Server.userState.get(carName)) {
			if(Server.userState.get(carName).online) {
				Server.userState.get(carName).info.add(s);
				Server.userState.get(carName).notifyAll();
			}
			else {
				//TODO 在UI上反馈信息
			}
		}
	}

	
}