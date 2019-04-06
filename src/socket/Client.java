package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class Client {
	//要连接的地址
		private String host = "127.0.0.1"; 
		private int portForSending = 55533;
		private int portForRecving = 55532;
		private Socket sendingSocket = null;
		private Socket recvingSocket = null;
	//用户名和地址
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
			    try {
			    	if(sendingSocket!=null) {
			    	BufferedReader br = new BufferedReader(new InputStreamReader(sendingSocket.getInputStream(),"utf-8"));;
			    	while(true) {
				    	sendingSocket.getOutputStream().write(("HOST:"+userName+"\r\n"+
				    					"DISCONNECT\r\n").getBytes("utf-8"));
				    	if(br.readLine().equals("GET DISCONNECT")) {
				    		sendingSocket.getOutputStream().write("ALREADY DISCONNECT\r\n".getBytes("utf-8"));
				    		break;
				    	}
			    	}

			    		sendingSocket.close();
			    	}
			    	if(recvingSocket!=null)
			    		recvingSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		public void getPicture(String car) {
			try {
				sendingSocket.getOutputStream().write(("HOST:"+userName+"\r\n"+
						"GETPICTURE:"+car).getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void moveCar(String car,String direction,String length) {
			try {
				sendingSocket.getOutputStream().write(("HOST:"+userName+"\r\n"+
						"MOVE:"+car+" "+direction+" "+length+"\r\n").getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public void stopCar(String car) {
			try {
				sendingSocket.getOutputStream().write(("HOST:"+userName+"\r\n"+
				"CEASE:"+car).getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public String logOn(String userName,String password){
		    this.userName = userName;
		    this.password = password;
			try {
				sendingSocket = new Socket(host, portForSending);

				System.out.println("Client init");
				//第一次握手
			    String message="HOST:"+userName+"\r\n" + 
			    		"CODE:"+password+"\r\n" + "FUCTION:REQUIRE\r\n";
			    System.out.println("First handshaking sent");
			    sendingSocket.getOutputStream().write(message.getBytes("UTF-8"));
			    
			    //第二次握手
			    System.out.println("Second handshaking get");
			    BufferedReader br = new BufferedReader(new InputStreamReader(sendingSocket.getInputStream(),"utf-8"));
			    String second = this.readPackage(2,br);
			    int location = second.indexOf("FUCTION:PERMISSION");
			    if(location==-1) {
			    	sendingSocket.close();
			    	return second.split("\r\n")[1];//获得第二个包中的提示信息
			    }
			    
			    //第三次握手
			    System.out.println("Third handshaking sent");
			    sendingSocket.getOutputStream().write(("HOST:"+userName+"\r\n" + 
			    		"FUCTION:BUILD\r\n").getBytes("utf-8"));
			    //握手完毕
			    
			    //接受socket握手
			    recvingSocket = new Socket(host, portForRecving);
			    message="HOST:"+userName+"\r\n" + 
			    		"CODE:"+password+"\r\n" + "FUCTION:REQUIRE\r\n";
			    recvingSocket.getOutputStream().write(message.getBytes("UTF-8"));
			    System.out.println("First handshaking sent");
			    
			    System.out.println("Second handshaking get");
			    br = new BufferedReader(new InputStreamReader(recvingSocket.getInputStream(),"utf-8"));
			    second = this.readPackage(2,br);
			    location = second.indexOf("FUCTION:PERMISSION");
			    if(location==-1) {
			    	recvingSocket.close();
			    	return second.split("\r\n")[1];//获得第二个包中的提示信息
			    }
			    
			    System.out.println("Third handshaking sent");
			    recvingSocket.getOutputStream().write(("HOST:"+userName+"\r\n" + 
			    		"FUCTION:BUILD\r\n").getBytes("utf-8"));
			   	return "Connection built";
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "Data transfer wrongly";
		  }
}
