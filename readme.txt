这个项目是大学生创新创业参赛项目

UI
使用Java编程语言在eclipse编译器完成了UI的设计。用JFrame类作为界面的框架。JFrame类中放置了两个JPanel类的对象，左右放置。左侧的JPanel对象放置了一些组件用来显示小车的速度、方向、当前小车和用户信息。右侧的Jpanel显示地图。同时使用了JMenuBar、JMenu、JMenuItem类完成了一个菜单栏。用户点击摄像头按钮的时候，显示地图的位置改为显示小车的摄像头。


通讯协议
设计的协议有以下字段：HOST，CODE，FUCTION。HOST用于指明数据的发送方。CODE用于指明发送方的用户密码，以验证身份。FUCTION用于指明所发送数据的作用。每个数据包都需要有FUCTION，和HOST字段来标明这个数据包的发送方和作用。CODE字段是不一定要有的。具体来说FUCTION字段的值有八个，分别为REQUIRE,PERMISSION,BUILD,MOVE，LOCATION，GETPICTURE，CEASE，PICUTRE，DISCONNECT。这八个值对应的含义分别是：请求连接建立、允许连接建立、连接建立确认、命令小车移动、小车向服务器上报坐标、获得小车摄像头的图片、停止获得小车摄像头的图片、小车向服务器发送图片，断开连接。三次握手的具体实现方式如下：首先客户端（小车或者电脑）向服务器发送REQUIRE命令，连同发送HOST码和CODE码以验证身份；之后服务器确认身份之后返回PERMISSION命令，允许连接（如果用户输入的密码有误则不进行第二次握手）；之后客户端再返回BUILD命令，通告服务器客户端已经准备好。
1.第一次握手
HOST：xxx
CODE：xxx
FUCTION:REQUIRE
2.第二次握手
HOST:xxx
FUCTION:PERMISSION
3.第三次握手
HOST：xxx
FUCTION:BUILD

其他数据：
HOST:xxx\r\n
MOVE:car2，NORTH（|SOUTH|EAST|WEST），3.3METER\r\n

HOST:xxx\r\n
LOCATION:car3,