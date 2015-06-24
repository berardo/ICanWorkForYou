package au.com.yourcompany.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import au.com.yourcompany.HTTPServer.Status;
import au.com.yourcompany.HTTPServerListener;
import au.com.yourcompany.HTTPServer;

public class Screen extends JFrame implements HTTPServerListener {

	private static final long serialVersionUID = 1L;
	private HTTPServer server;
	private JLabel lblTitle;
	private JLabel lblPHPPath;
	private JTextField txtPHPPath;
	private JButton start;
	private JButton stop;
	
	public Screen() {
		server = new HTTPServer();
		this.loadScreen();
		this.putObjects();
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void loadScreen() {
		this.setTitle("My Simple Java Web Server localhost:8001");
		this.setSize(300, 300);
		
	}
	
	public void putObjects() {
		// Creating objects
		lblTitle = new JLabel("This is the console to start and stop the service");
		lblTitle.setFont(new Font("Helvetica", Font.BOLD, 20));
		lblPHPPath = new JLabel("Choose your PHP path to execute the server");
		
		txtPHPPath = new JTextField("/usr/bin/php", 30);
		server.setPHPPath(txtPHPPath.getText());
		
		start = new JButton("Start");
		stop = new JButton("Stop");
		
		// Putting in order
		JPanel panel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		panel.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		panel.add(lblTitle, constraints);
		
		constraints.gridy = 2;
		panel.add(lblPHPPath, constraints);
		
		constraints.gridy = 3;
		panel.add(txtPHPPath, constraints);
		
		constraints.gridy = 4;
		Panel btns = new Panel();
		btns.add(start);
		btns.add(stop);
		panel.add(btns, constraints);
		
		this.add(panel);
		
		// Events
		txtPHPPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				server.setPHPPath(txtPHPPath.getText());
			}
		});
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Executing in another Thread to do not freeze the interface
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						start.setEnabled(false);
						stop.setEnabled(true);
						server.start(Screen.this);						
					}
				});
				t.start();
			}
		});
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// Executing in another Thread to do not freeze the interface
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						start.setEnabled(true);
						stop.setEnabled(false);
						server.stop(Screen.this);
					}
				});
				t.start();
			}
		});
	}

	@Override
	public void onActionStart(Status status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActionStop(Status status) {
		// TODO Auto-generated method stub
		
	}

}
