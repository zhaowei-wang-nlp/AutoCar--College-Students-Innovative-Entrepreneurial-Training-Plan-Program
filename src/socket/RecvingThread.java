package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

public class RecvingThread extends MyThread implements Runnable {
	RecvingThread(Socket socket) {
		super(socket);
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			/*
			 * byte[] bytes = new byte[1024]; int a = socket.getInputStream().read(bytes);
			 * String b = new String(bytes,0,a,"utf-8");
			 */

			String host = this.handShaking(socket);
			
			String s;
			if (isUser) {
				while (true) {
					s = readPackage(2, br);
					if (s.indexOf("MOVE") != -1 || s.indexOf("GETPICTURE") != -1 || s.indexOf("CEASE") != -1
							|| s.indexOf("STOPPICTURE") != -1) {
						this.storeCarInfo(s);
					}
					if (s.indexOf("DISCONNECT") != -1) {
						socket.getOutputStream().write("GET DISCONNECT\r\n".getBytes("utf-8"));

						byte[] bytes = new byte[1024];
						int len = socket.getInputStream().read(bytes);
						if (new String(bytes, 0, len, "utf-8").equals("ALREADY DISCONNECT")) {
							System.out.println(host + "log off");
							synchronized (Server.usersState.get(host)) {
									Server.usersState.get(host).online = false;
								}
							socket.close();
							break;
						}
					}
				}
			} else {
				while (true) {
					s = readPackage(2, br);
					if (s.indexOf("PICTURE") != -1) {
						this.storeUserInfo(s);
					} else if (s.indexOf("LOCATION") != -1) {
						if (s.indexOf("ALL") != -1) {
							int start = s.indexOf("ALL");
							String sub1 = s.substring(0, start);
							String sub2 = s.substring(start + 3, s.length());
							for (Map.Entry<String, State> e : Server.usersState.entrySet()) {
								synchronized (Server.usersState.get(e.getKey())) {
									if (e.getValue().online)
										this.storeUserInfo(sub1 + e.getKey() + sub2);
								}
							}
						} else {
							this.storeUserInfo(s);
						}
					} else if (s.indexOf("DISCONNECT") != -1) {
						socket.getOutputStream().write("GET DISCONNECT\r\n".getBytes("utf-8"));

						byte[] bytes = new byte[1024];
						int len = socket.getInputStream().read(bytes);
						if (new String(bytes, 0, len, "utf-8").equals("ALREADY DISCONNECT")) {
							System.out.println(host + "log off");
							synchronized (Server.carState.get(host)) {
								Server.carState.get(host).online = false;
							}
							socket.close();
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	void storeCarInfo(String s) {
		String command = s.split("\r\n")[1];
		int start = command.indexOf(":")+1;
		String carName = command.split("\\s")[0].substring(start);
		synchronized (Server.carState.get(carName)) {
			if (Server.carState.get(carName).online) {
				Server.carState.get(carName).info.add(s);
				Server.carState.get(carName).notifyAll();
			} else {
				// TODO 在UI上反馈信息
			}
		}
	}

	private void storeUserInfo(String s) {
		String command = s.split("\r\n")[1];// 第二行
		int start = command.indexOf(":") + 1;// 用冒号分割
		String userName = command.split("\\s")[0].substring(start);
		synchronized (Server.usersState.get(userName)) {
			if (Server.usersState.get(userName).online) {
				Server.usersState.get(userName).info.add(s);
				Server.usersState.get(userName).notifyAll();
			} else {
				// TODO
			}
		}
	}

}