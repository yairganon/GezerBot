package jezerbot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Login {
	public JFrame framemain = new JFrame("gezer bot");

	public Login() {

		framemain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framemain.setResizable(false);
		framemain.setLayout(null);
		framemain.setPreferredSize(new Dimension(200, 300));
		framemain.setLocation(400, 200);

		// Heading: LOGIN
		JLabel heading = new JLabel("GEZER LOGIN");
		heading.setBounds(60, 0, 100, 40);
		framemain.add(heading);

		// Label Username
		JLabel username_label = new JLabel("username: ");
		username_label.setBounds(5, 40, 80, 20);
		framemain.add(username_label);
		// Textfield Username
		final JTextField username_field = new JTextField();
		username_field.setBounds(90, 40, 80, 20);
		framemain.add(username_field);

		// Label passlabel
		JLabel passlabel = new JLabel("password: ");
		passlabel.setBounds(5, 70, 80, 20);
		framemain.add(passlabel);
		// Textfield pass
		final JTextField pass = new JTextField();
		pass.setBounds(90, 70, 80, 20);
		framemain.add(pass);

		// Label idlabel
		JLabel idlabel = new JLabel("id: ");
		idlabel.setBounds(5, 100, 80, 20);
		framemain.add(idlabel);
		// Textfield id
		final JTextField id = new JTextField();
		id.setBounds(90, 100, 80, 20);
		framemain.add(id);
		
		
		JLabel ref = new JLabel("<html>refresh every &#160 &#160 &#160 &#160 &#160 &#160 minutes<html>");
		ref.setBounds(5, 120, 200, 40);
		framemain.add(ref);
		final JComboBox<String> combo = new JComboBox<String>();
		combo.setBounds(85, 130, 30, 20);
		combo.addItem("1");
        combo.addItem("3");
        combo.addItem("5");
        framemain.add(combo);
        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println(combo.getSelectedItem().toString());
            	Main.sleep_time = Integer.valueOf(combo.getSelectedItem().toString());
              }
            });

		// Button Login
		JButton loginBtn = new JButton("LOGIN");
		loginBtn.setBounds(40, 170, 120, 25);
		framemain.add(loginBtn);

		JLabel info = new JLabel("<html>keep minimize upon connection <br/>&#160 &#160 &#160 i will triger alert for you <br/> when new exam scan uploaded<html>");
		info.setBounds(9, 210, 200, 50);
		framemain.add(info);
		framemain.pack();
		framemain.setVisible(true);

		//login event
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.userName = username_field.getText();
				Main.pass = pass.getText();
				Main.userId = id.getText();
				framemain.getContentPane().removeAll();
				framemain.setPreferredSize(null);
				framemain.setLayout(new BorderLayout());
				JLabel con = new JLabel("conecting...");
				framemain.add(con);
				framemain.repaint();
				framemain.pack();
				Thread loopThread = new Thread() {
					public void run() {
						try {
							Main.loop();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(framemain, e);
							e.printStackTrace();
							framemain.dispose();
						}
					}
				};
				loopThread.start();
			}});}}


