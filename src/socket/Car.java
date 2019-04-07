package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Car {
	private String host = "127.0.0.1";
	private int portForSending = 55533;
	private int portForRecving = 55532;
	private Socket sendingSocket = null;
	private Socket recvingSocket = null;
	private boolean imageModel = false;// 用于指明现在小车是否需要传输图像给用户
	private String imageUser = "";// 用于指明图像传给谁
	private Location location;
	private boolean authorized = false;
	private long time;
	// 用户名和地址
	private String carName;
	private String password;

	public static void main(String[] args) {
		// 用到小车的时候此处可以改成用args传递小车名字和密码
		Car car = new Car("car1","abc");
		car.showMenu();	
	}

	public Car(String carName, String password) {
		this.carName = carName;
		this.password = password;
	}

	public void logOn() throws IOException, Exception{
			sendingSocket = new Socket(host, portForSending);

			System.out.println("Car init");
			// 第一次握手
			String message = "HOST:" + carName + "\r\n" + "CODE:" + password + "\r\n" + "FUCTION:REQUIRE\r\n";
			System.out.println("First handshaking sent");
			sendingSocket.getOutputStream().write(message.getBytes("UTF-8"));

			// 第二次握手
			System.out.println("Second handshaking get");
			BufferedReader br = new BufferedReader(new InputStreamReader(sendingSocket.getInputStream(), "utf-8"));
			String second = this.readPackage(2, br);
			int location = second.indexOf("FUCTION:PERMISSION");
			if (location == -1) {
				sendingSocket.close();
				System.out.println(second.split("\r\n")[1]);
				throw new Exception(second.split("\r\n")[1]);
			}

			// 第三次握手
			System.out.println("Third handshaking sent");
			sendingSocket.getOutputStream().write(("HOST:" + carName + "\r\n" + "FUCTION:BUILD\r\n").getBytes("utf-8"));
			// 握手完毕

			// 接受socket握手
			recvingSocket = new Socket(host, portForRecving);
			message = "HOST:" + carName + "\r\n" + "CODE:" + password + "\r\n" + "FUCTION:REQUIRE\r\n";
			recvingSocket.getOutputStream().write(message.getBytes("UTF-8"));
			System.out.println("First handshaking sent");

			System.out.println("Second handshaking get");
			br = new BufferedReader(new InputStreamReader(recvingSocket.getInputStream(), "utf-8"));
			second = this.readPackage(2, br);
			location = second.indexOf("FUCTION:PERMISSION");
			if (location == -1) {
				recvingSocket.close();
				System.out.println(second.split("\r\n")[1]);
				throw new Exception(second.split("\r\n")[1]);
			}

			System.out.println("Third handshaking sent");
			recvingSocket.getOutputStream().write(("HOST:" + carName + "\r\n" + "FUCTION:BUILD\r\n").getBytes("utf-8"));
			System.out.println("Connection built");
	}

	private Location refreshLocation() {
		Random r = new Random();
		if (this.location == null) {
			double x = 100 + r.nextDouble() * 100;
			double y = r.nextDouble() * 100 + 100;
			return new Location(x, y);
		} else {
			double x = location.x + r.nextDouble() * 20;
			double y = location.y + r.nextDouble() * 20;
			return new Location(x%500, y%500);
		}
	}

	public void showMenu() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Menu");
			System.out.println("1.get on");
			System.out.println("2.get off");
			System.out.println("3.exit");
			int a = sc.nextInt();
			sc.nextLine();
			if (a == 1) {
				if (authorized) {
					System.out.println("Already log on.");
				} else {
					try {
						logOn();
						authorized = true;
						new Thread(new CarSendingInfo()).start();
						new Thread(new CarRecvingInfo(this)).start();
					} catch(IOException e) {
						e.printStackTrace();
					}
					catch (Exception e) {
						System.out.println("failed to log on");
						e.printStackTrace();
					}
				}
			} else if (a == 2) {
				if (!authorized) {
					System.out.println("you don't log on.");
				} else {
					authorized = false;
				}
			} else if (a==3) {
				sc.close();
				break;
			}
			}
		}

	private String readPackage(int n, BufferedReader br) throws IOException{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
				sb.append(br.readLine() + "\r\n");
		}
		return sb.toString();
	}

	private void move() {
		System.out.println("The car is Moving");
	}

	private void cease() {
		System.out.println("The car ceases");
	}

	class CarSendingInfo implements Runnable {
		@Override
		public void run() {
			while (authorized) {
				try {
					Location l = refreshLocation();
					if (location == null) {
						time = System.currentTimeMillis();
						location = l;
						sendingSocket.getOutputStream().write(
								("HOST:" + carName + "\r\nLOCATION:ALL " + location.x + " " + location.y + "0\r\n")
										.getBytes("utf-8"));
					} else {
						long currentTime = System.currentTimeMillis();
						double speed = Math
								.sqrt((l.x - location.x) * (l.x - location.x) + (l.y - location.y) * (l.y - location.y))
								/ (currentTime - time);
						speed *= 1000;
						sendingSocket.getOutputStream().write(("HOST:" + carName + "\r\nLOCATION:ALL " + l.x
								+ " " + l.y + " " + speed + "\r\n").getBytes("utf-8"));
						location = l;
						time = currentTime;
					}
					Thread.sleep(500);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				while (true) {
					sendingSocket.getOutputStream().write(("HOST:" + carName + "\r\nDISCONNECT\r\n").getBytes("utf-8"));
					BufferedReader br = new BufferedReader(new InputStreamReader(sendingSocket.getInputStream()));
					if (br.readLine().equals("GET DISCONNECT")) {
						sendingSocket.getOutputStream().write("ALREADY DISCONNECT\r\n".getBytes("utf-8"));
						break;
					}
				}
				sendingSocket.close();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	class CarRecvingInfo implements Runnable {
		Car c;

		CarRecvingInfo(Car c) {
			this.c = c;
		}

		@Override
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(c.recvingSocket.getInputStream()));
				while (authorized) {
					String command = readPackage(2, br);
					if (command.indexOf("MOVE") != -1) {
						c.move();
						// TODO
					} else if (command.indexOf("CEASE") != -1) {
						c.cease();
						// TODO
					}
				}
				recvingSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private String getFieldValue(String s, String field) {
			int start = s.indexOf(field + ":") + field.length() + 1;
			int end = s.indexOf("\r\n", start);
			return s.substring(start, end);
		}
	}
}
