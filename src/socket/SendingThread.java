package socket;

import java.io.IOException;
import java.net.Socket;

public class SendingThread extends MyThread implements Runnable{
	public SendingThread(Socket socket) {
		super(socket);
	}
	@Override
	public void run() {
		try {
			/*byte[] bytes = new byte[1024];
			int a = socket.getInputStream().read(bytes);
			String b = new String(bytes,0,a,"utf-8");*/
			
			String host = this.handShaking(socket);
			if(isUser) {
			synchronized(Server.clientState.get(host)) {
				Server.clientState.get(host).online = true;
			}
			
			
			while(Server.clientState.get(host).online) {
				synchronized(Server.clientState.get(host)) {
					Server.clientState.get(host).wait();
					while(!Server.clientState.get(host).info.isEmpty()) {
						String info = Server.clientState.get(host).info.get(0);
						socket.getOutputStream().write(info.getBytes("utf-8"));
					}
				}
			}
			System.out.println(host+"log off");
			socket.close();
			}
			else {
				synchronized(Server.carState.get(host)) {
					Server.carState.get(host).online = true;
				}
				while(Server.carState.get(host).online) {
					synchronized(Server.carState.get(host)) {
						Server.carState.get(host).wait();
						while(!Server.carState.get(host).info.isEmpty()) {
							String info = Server.carState.get(host).info.get(0);
							socket.getOutputStream().write(info.getBytes("utf-8"));
						}
					}
				}
				System.out.println(host+"log off");
				socket.close();
			}
		}     catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
	}
}
