package UI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.MenuListener;

import socket.Client;
import socket.Server;

import javax.swing.event.MenuEvent;

public class UserInterface extends JFrame {
	private JPanel contentPane;
	private JTextField txtms;
	private JTextField textField;
	private NewPanel rightPanel;
	private LogOn logOn;
	private JFrame jf = this;
	private boolean authorized = false;
	private Client client = new Client();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
					UserInterface frame = new UserInterface();
					frame.setVisible(true);
					try {
						System.in.read();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					frame.updateUI();
	}

	/**
	 * Create the frame.
	 */
	public UserInterface() {
		logOn = new LogOn();
		setTitle("\u667A\u80FD\u9664\u51B0\u8F66");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 618);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Image image = Toolkit.getDefaultToolkit().getImage("src//icon.png");
		this.setIconImage(image);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 982, 34);
		contentPane.add(menuBar);
		
		JMenu map = new JMenu("\u767B\u5F55");
		map.addMenuListener(new MenuListener() {
			public void menuCanceled(MenuEvent arg0) {
			}
			public void menuDeselected(MenuEvent arg0) {
				
			}
			public void menuSelected(MenuEvent arg0) {
				logOn.setVisible(true);
				logOn.setAlwaysOnTop(true);
			}
		});
		menuBar.add(map);
		
		JMenu carCamera = new JMenu("\u5730\u56FE\u754C\u9762");
		menuBar.add(carCamera);
		
		JMenu aboutUs = new JMenu("\u6444\u50CF\u5934");
		menuBar.add(aboutUs);
		
		JMenuItem menuItem = new JMenuItem("\u524D\u6444\u50CF\u5934");
		aboutUs.add(menuItem);
		
		JMenuItem menuItem_1 = new JMenuItem("\u540E\u6444\u50CF\u5934");
		aboutUs.add(menuItem_1);
		
		JMenu menu = new JMenu("\u5173\u4E8E\u6211\u4EEC");
		menuBar.add(menu);
		
		rightPanel = new NewPanel("src//map.png");
		rightPanel.setBounds(215, 35, 765, 535);
		contentPane.add(rightPanel);
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(0, 35, 212, 536);
		contentPane.add(leftPanel);
		leftPanel.setLayout(null);
		
		JRadioButton radioButton = new JRadioButton("\u81EA\u52A8\u7EA0\u6B63\u529F\u80FD");
		radioButton.setBounds(43, 212, 119, 27);
		radioButton.setSelected(true);
		leftPanel.add(radioButton);
		
		txtms = new JTextField();
		txtms.setText("3m/s");
		txtms.setBounds(43, 264, 119, 38);
		leftPanel.add(txtms);
		txtms.setColumns(10);
		
		JLabel label = new JLabel("\u9664\u51B0\u8F66\u884C\u9A76\u901F\u5EA6");
		label.setBounds(43, 315, 119, 18);
		leftPanel.add(label);
		
		textField = new JTextField();
		textField.setText("\u6B63\u5317");
		textField.setBounds(43, 346, 119, 38);
		leftPanel.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("\u9664\u51B0\u8F66\u884C\u9A76\u65B9\u5411");
		label_1.setBounds(43, 397, 119, 18);
		leftPanel.add(label_1);
		
		Component verticalGlue = Box.createVerticalGlue();
		verticalGlue.setBounds(212, 0, 0, 536);
		leftPanel.add(verticalGlue);
		
		JPanel head = new NewPanel("src//pikacu.jpg");
		head.setBounds(27, 13, 150, 130);
		leftPanel.add(head);
		
		JLabel label_2 = new JLabel("\u7528\u6237\uFF1A\u8DF3\u8DF3\u718A");
		label_2.setBounds(37, 168, 125, 18);
		leftPanel.add(label_2);
		
		JLabel label_3 = new JLabel("\u9009\u62E9\u5F53\u524D\u5C0F\u8F66");
		label_3.setBounds(43, 479, 119, 18);
		leftPanel.add(label_3);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"\u5C0F\u8F661", "\u5C0F\u8F662", "\u5C0F\u8F663"}));
		comboBox.setBounds(43, 419, 119, 38);
		leftPanel.add(comboBox);
		String s = "abc";
		s.split("\\n");
		
		
	}
	public class LogOn extends JFrame {    //继承JFrame顶层容器类
		
		//定义组件
		JPanel jp1,jp2,jp3;    //定义面板
		JTextField jtf1;        //定义文本框
		JPasswordField jpf1;    //定义密码框
		JLabel jlb1,jlb2;        //定义标签
		JButton jb1,jb2;        //定义按钮
		JFrame lg = this;
		
		public LogOn()        //构造函数
		{
			//创建组件
			jp1=new JPanel();    //创建三个面板
			jp2=new JPanel();
			jp3=new JPanel();
			
			jlb1=new JLabel("用户名");    //创建两个标签
			jlb2=new JLabel("密	  码");
			
			jb1=new JButton("登录");    //创建两个按钮
			jb2=new JButton("清空");
			jtf1=new JTextField(10);     //创建文本框
			jpf1=new JPasswordField(10);    //创建密码框
			
			jb1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String userName = jtf1.getText();
					String password = new String(jpf1.getPassword());
					authorized = client.logOn(userName, password);
					if(authorized) {
						JOptionPane.showMessageDialog(null,"密码或用户名输入错误","警告",JOptionPane.ERROR_MESSAGE); 
					}
				}
			});
			jb2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					jtf1.setText("");
					jpf1.setText("");
				}
			});
			//设置布局管理器
			getContentPane().setLayout(new GridLayout(3,1));    //网格布局，3行一列
			
			//添加组件
			getContentPane().add(jp1);    //添加面板
			getContentPane().add(jp2);
			getContentPane().add(jp3);
			
			jp1.add(jlb1);    //添加面板1的标签和文本框
			jp1.add(jtf1);
			
			jp2.add(jlb2);    //添加面板2的标签和密码框
			jp2.add(jpf1);
			
			jp3.add(jb1);    //添加面板3的按钮
			jp3.add(jb2);
			
			//设置窗口属性
			this.setTitle("登录界面");    //创建界面标题
			this.setSize(300, 200);        //设置界面像素
			this.setLocation(500, 100);    //设置界面初始位置
		}
	}
	public void updateUI() {
		this.rightPanel.resetPath("src//camera.jpg");
	}
	
}
class NewPanel extends JPanel {
	String path;
	  public NewPanel(String path) {
		  this.path = path;
	  }
      public void resetPath(String path) {
    	  this.path = path;
    	  this.updateUI();
      }
	  public void paintComponent(Graphics g) {
	   int x = 0, y = 0;
	   ImageIcon icon = new ImageIcon(path);// 003.jpg是测试图片在项目的根目录下
	   g.drawImage(icon.getImage(), x, y, getSize().width,
	     getSize().height, this);// 图片会自动缩放
	  }
}
