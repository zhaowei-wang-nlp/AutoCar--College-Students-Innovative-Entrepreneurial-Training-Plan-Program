package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class Client {
		private String host = "127.0.0.1"; 
		private int port = 55533;
		private Socket socket = null;
		private String userName;
		private String password;
		public static void main(String[] args) {
			Client c = new Client();
			c.logOn("JumpingBear","19986");
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
		public Client() {
			
		}
		
		public void logOff() {
			if(socket!=null) {
			    try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public void getPicture(String car) {
			try {
				socket.getOutputStream().write(("HOST:"+userName+"\r\n"+
						"GETPICTURE:"+car).getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void moveCar(String car,String direction,String length) {
			try {
				socket.getOutputStream().write(("HOST:"+userName+"\r\n"+
						"MOVE:"+car+" "+direction+" "+length+"\r\n").getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void stopCar(String car) {
			try {
				socket.getOutputStream().write(("HOST:"+userName+"\r\n"+
				"CEASE:"+car).getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public boolean logOn(String userName,String password){
		    this.userName = userName;
		    this.password = password;
			try {
				socket = new Socket(host, port);
				// 建立连接后获得输出流
				System.out.println("Client Built");
			    String message="HOST:"+userName+"\r\n" + 
			    		"CODE:"+password+"\r\n" + "FUCTION:REQUIRE\r\n";
			    System.out.println("Sent");
			    socket.getOutputStream().write(message.getBytes("UTF-8"));
			    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
			    
			    String second = this.readPackage(2,br);
			    System.out.println("Get Permision");
			    int location = second.indexOf("FUCTION:PERMISSION");
			    if(location==-1) {
			    	throw new Exception("User authentication failed");
			    }
			    socket.getOutputStream().write(("HOST:"+userName+"\r\n" + 
			    		"FUCTION:BUILD\r\n").getBytes("utf-8"));
			    socket.getOutputStream().write(("HOST:"+userName+"\r\n"+"CONNECTION ").getBytes("utf-8"));
			    //握手完毕
			   	return true;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			return false;
		  }
		private String getField(String s,String field) {
			int start = s.indexOf(field+":")+field.length()+1;
			int end = s.indexOf("\r\n",start);
			return s.substring(start, end);
		}
}
