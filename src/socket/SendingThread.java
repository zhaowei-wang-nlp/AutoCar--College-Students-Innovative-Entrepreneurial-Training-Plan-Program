package socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

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
			synchronized(Server.usersState.get(host)) {
				Server.usersState.get(host).online = true;
			}
			
			System.out.println("online users");
			for(Map.Entry<String, State> e:Server.usersState.entrySet()) {
				if(e.getValue().online) {
					System.out.print(e.getKey()+"  ");
				}
			}
			System.out.println();
			
			while(Server.usersState.get(host).online) {
				synchronized(Server.usersState.get(host)) {
					Server.usersState.get(host).wait();
					while(!Server.usersState.get(host).info.isEmpty()) {
						String info = Server.usersState.get(host).info.remove(0);
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
				
				System.out.println("online cars:");
				for(Map.Entry<String, State> e:Server.carState.entrySet()) {
					if(e.getValue().online) {
						System.out.print(e.getKey()+"  ");
					}
				}
				System.out.println();
				
				while(Server.carState.get(host).online) {
					synchronized(Server.carState.get(host)) {
						Server.carState.get(host).wait();
						while(!Server.carState.get(host).info.isEmpty()) {
							String info = Server.carState.get(host).info.remove(0);
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
