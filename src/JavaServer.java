/**
 * @author Aldo Dwie Rizky
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*; 
import javax.swing.*;
import javax.swing.text.DefaultCaret;
public class JavaServer extends JFrame implements ActionListener{
	static String message= "";
	static String username= "";

	static ServerSocket server=null;
	static Socket socket=null;
	static PrintWriter writer=null;
    
	static JTextArea msgRec=new JTextArea(100,50);
	static JTextArea msgSend=new JTextArea(100,50);
	JButton send= new JButton("Send");
	JScrollPane pane2, pane1;
	
	JMenuBar bar=new JMenuBar();
	
	JMenu messanger=new JMenu("ChatBox V1.0");
	JMenuItem logOut=new JMenuItem("Log Out");
	
	JMenuItem about=new JMenuItem("about");
	
	public JavaServer() {
		super("Java Server");
		setBounds(0, 0, 407, 495);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		msgRec.setEditable(false);
		msgRec.setBackground(Color.BLACK);
		msgRec.setForeground(Color.WHITE);
	
		msgRec.setText("");
		
		msgRec.setWrapStyleWord(true);
		msgRec.setLineWrap(true);
		
		pane2=new JScrollPane(msgRec);
		pane2.setBounds(0, 0, 400, 200);
		pane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(pane2);
		
		msgSend.setBackground(Color.WHITE);
		msgSend.setForeground(Color.BLACK);
		msgSend.setLineWrap(true);
		msgSend.setWrapStyleWord(true);
		
		msgSend.setText("Tuliskan Pesan Disini!");
		
		pane1=new JScrollPane(msgSend);
		pane1.setBounds(0, 200, 400, 200);
		pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(pane1);
		
		send.setBounds(0, 400, 400, 40);
		add(send);
		send.addActionListener(this);
		
		bar.add(messanger); 
		messanger.add(logOut);
		logOut.addActionListener(this);
		bar.add(about);
		about.addActionListener(this);
		
		setJMenuBar(bar);
		
		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				if( !msgRec.getText().equals("")) {
					System.out.println("Yes Focus");
					writer.flush();
				}
			}
			@Override
			public void windowLostFocus(WindowEvent e) {
				if(!msgRec.getText().equals("")) {
					writer.flush();
				}
				
			}
		
	});
	if((username)!=null) {
		setVisible(true);
	}
	else {
		System.exit(0);
	}
  }
	public static void main(String[] args) {
		username=JOptionPane.showInputDialog("User Name (Server)");
		
		(new Thread(new Runnable() {
			public void run() {
				new JavaServer();
			}
		})).start();
		try {
			
		server=new ServerSocket(8888);
		System.out.println(server.getInetAddress().getLocalHost());
		
		socket=server.accept();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		msgRec.setText("Terkoneksi");
		(new Thread(new Runnable() {
			public void run() {
				try {
					BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					String line=null;
					boolean testFlag=true;
					while((line=reader.readLine())!=null) {
						msgRec.append("\n"+line);
						
						cursorUpdate();
					}
				}
				catch(IOException ee)
				{
					try {
						server.close();
						socket.close();
					}
					catch(IOException eee)
					{
						eee.printStackTrace();
					}
				}
			}
		})).start();
		try {
			writer=new PrintWriter(socket.getOutputStream(), true);
		}
		catch(IOException e) {
			try {
				server.close();
				socket.close();
			}
			catch(IOException eee) {
				
			}
		}
	}
	public void actionPerformed(ActionEvent e) {
		
		Object scr=e.getSource();
		if(scr==send) {
			sendMessage();
		}
		else if(scr==logOut) {
			System.exit(0);
		}
		else if(scr==about) {
			JOptionPane.showMessageDialog(this, "UTS_Aldodwrzy\n");
		}
	}
	public void sendMessage() {
		writer.println(username+":"+msgSend.getText()); 
		writer.flush();
		msgRec.append("\nMe: "+msgSend.getText());
		cursorUpdate();
		msgSend.setText("");
		msgSend.setCaretPosition(0);
	}
	public static void cursorUpdate() {
		DefaultCaret caret=(DefaultCaret) msgRec.getCaret();
		caret.setDot(msgRec.getDocument().getLength());
		
		DefaultCaret caret2=(DefaultCaret) msgSend.getCaret();
		caret2.setDot(msgSend.getDocument().getLength());
	}
}
