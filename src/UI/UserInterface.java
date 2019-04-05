package UI;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JSpinner;
import javax.swing.AbstractListModel;
import javax.swing.SpinnerListModel;
import javax.swing.JScrollPane;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;
import java.awt.List;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class UserInterface extends JFrame {

	private JPanel contentPane;
	private JTextField txtms;
	private JTextField textField;
	private NewPanel rightPanel;

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
		setTitle("\u667A\u80FD\u9664\u51B0\u8F66");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1000, 618);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Image image = Toolkit.getDefaultToolkit().getImage("src//UI//icon.png");
		this.setIconImage(image);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 982, 34);
		contentPane.add(menuBar);
		
		JMenu map = new JMenu("\u767B\u5F55");
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
		
		rightPanel = new NewPanel("src//UI//map.png");
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
		
		JPanel head = new NewPanel("src//UI//pikacu.jpg");
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
	public void updateUI() {
		this.rightPanel.resetPath("src//UI//camera.jpg");
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
