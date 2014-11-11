package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.GridLayout;

import net.miginfocom.swing.MigLayout;

import javax.swing.BoxLayout;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class openAdmin implements Runnable{

	private JFrame frmAdminConsole;
	private JPanel panel;
	private JButton button;
	private JLabel label;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					openAdmin window = new openAdmin();
					window.frmAdminConsole.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public openAdmin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAdminConsole = new JFrame();
		frmAdminConsole.setForeground(Color.WHITE);
		frmAdminConsole.setIconImage(Toolkit.getDefaultToolkit().getImage(openAdmin.class.getResource("/Images/MetalBackground.jpg")));
		frmAdminConsole.getContentPane().setBackground(Color.WHITE);
		frmAdminConsole.setBackground(Color.WHITE);
		frmAdminConsole.setTitle("Admin console");
		frmAdminConsole.getContentPane().setForeground(Color.WHITE);
		frmAdminConsole.setBounds(100, 100, 233, 103);
		frmAdminConsole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAdminConsole.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		panel = new JPanel();
		panel.setForeground(Color.YELLOW);
		panel.setBackground(Color.LIGHT_GRAY);
		
		frmAdminConsole.getContentPane().add(panel);
		
		button = new JButton("Open Admin console");
		button.setBounds(30, 20, 175, 29);
		button.addActionListener(new ButtonActionListener());
		panel.setLayout(null);
		button.setBackground(Color.WHITE);
		panel.add(button);
		
		label = new JLabel("");
		label.setLocation(0, 0);
		panel.add(label);
		label.setIcon(new ImageIcon(openAdmin.class.getResource("/Images/MetalBackground.jpg")));
		label.setSize(new Dimension(233, 81));
		label.setHorizontalAlignment(SwingConstants.CENTER);
	}
	@Override
	public void run() {
		frmAdminConsole.setVisible(true);;
		
	}
	private class ButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			GUILogic admin = new GUILogic();
			admin.run();
		}
	}
}
