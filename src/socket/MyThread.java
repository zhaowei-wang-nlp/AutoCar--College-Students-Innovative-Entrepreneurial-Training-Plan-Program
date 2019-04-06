package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

public class MyThread {
	protected Socket socket;
	protected boolean isUser = false;

	MyThread() {

	}

	MyThread(Socket socket) {
		this.socket = socket;
	}

	protected String getFieldValue(String s, String field) {
		int start = s.indexOf(field + ":") + field.length() + 1;
		int end = s.indexOf("\r\n", start);
		return s.substring(start, end);
	}

	protected String readPackage(int n, BufferedReader br) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			try {
				sb.append(br.readLine() + "\r\n");
			} catch (IOException e) {
				System.out.println("Read Packet Wrongly");
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	protected String handShaking(Socket socket) throws Exception {// 将异常抛到run方法中
		//第一次握手
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String s = readPackage(3, br);
		String host = getFieldValue(s, "HOST");
		String password = getFieldValue(s, "CODE");
		if (Server.clientMap.get(host) != null)
			isUser = true;
		else if (Server.carMap.get(host) != null)
			isUser = false;
		else {
			socket.getOutputStream().write(("HOST:" + host + "\r\nInvalid username\r\n").getBytes("utf-8"));
			socket.close();
			throw new Exception("Invalid username");
		}//检测用户名
		
		Map<String, String> map;
		if (isUser)
			map = Server.clientMap;
		else
			map = Server.carMap;
		String rightPassword = map.get(host);
		if (!rightPassword.equals(password)) {
			socket.getOutputStream().write(("HOST:" + host + "\r\nInvalid password\r\n").getBytes("utf-8"));
			socket.close();
			throw new Exception("Wrong password");
		}//检测密码
		System.out.println("user confirtmed");

		// second handshake
		socket.getOutputStream().write(("HOST:" + host + "\r\n" + "FUCTION:PERMISSION\r\n").getBytes("utf-8"));
		System.out.println("PERSION sent");

		// thrid handshake
		s = readPackage(2, br);
		String confirmHost = getFieldValue(s, "HOST");
		if (!(confirmHost.equals(host) && s.indexOf("BUILD") != -1)) {
			socket.getOutputStream().write(("HOST:" + host + "\r\n" + "different user\r\n").getBytes("utf-8"));
			socket.close();
			throw new Exception("Wrong confirming");
		}
		return host;
	}
}
