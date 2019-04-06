package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendingThread extends MyThread implements Runnable{
	public SendingThread(Socket socket) {
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
			
			this.handShaking(s, br, host, password);
			
			
			synchronized(Server.userState.get(host)) {
				Server.userState.get(host).online = true;
			}
			
			
			while(Server.userState.get(host).online) {
				synchronized(Server.userState.get(host)) {
					Server.userState.get(host).wait();
					while(!Server.userState.get(host).info.isEmpty()) {
						String info = Server.userState.get(host).info.get(0);
						socket.getOutputStream().write(info.getBytes("utf-8"));
					}
				}
			}
			System.out.println(host+"log off");
			socket.close();
		}     catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
	}
}
