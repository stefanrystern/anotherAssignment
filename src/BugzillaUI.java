import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class BugzillaUI {

	private JFrame frame;
	private JTextField textUser;
	private JPasswordField passwordField;
	private JComboBox<Bugzilla.MemberType> comboBoxType;
	private Bugzilla bz;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BugzillaUI window = new BugzillaUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BugzillaUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		try {
			bz = new Bugzilla(true);
		} catch (BugzillaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			displayMsg(e1);
			System.exit(0);
		}
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 418, 176);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(10, 27, 84, 14);
		frame.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(10, 65, 84, 14);
		frame.getContentPane().add(lblPassword);
		
		textUser = new JTextField();
		textUser.setBounds(104, 24, 140, 20);
		frame.getContentPane().add(textUser);
		textUser.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(104, 62, 140, 20);
		frame.getContentPane().add(passwordField);
		
		JLabel lblUserType = new JLabel("User Type");
		lblUserType.setBounds(10, 101, 84, 14);
		frame.getContentPane().add(lblUserType);
		
		comboBoxType = new JComboBox<Bugzilla.MemberType>();
		comboBoxType.setModel(new DefaultComboBoxModel<Bugzilla.MemberType>(Bugzilla.MemberType.values()));
		comboBoxType.setBounds(104, 98, 140, 20);
		frame.getContentPane().add(comboBoxType);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					bz.login(textUser.getText(),String.valueOf(passwordField.getPassword()));
					frame.setVisible(false);
					try {
						MemberUI window = new MemberUI(frame, bz, textUser.getText());
						window.show(true);
					} catch (Exception ex) {
						ex.printStackTrace();
						throw new BugzillaException(BugzillaException.ErrorType.UNKNOWN_ERROR);
					}
					
				} catch (BugzillaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					displayMsg(e1);
				}
				
				
			}
		});
		btnLogin.setBounds(273, 23, 124, 23);
		frame.getContentPane().add(btnLogin);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bugzilla.MemberType type = comboBoxType.getItemAt(comboBoxType.getSelectedIndex());
				try {
					bz.register(textUser.getText(),String.valueOf(passwordField.getPassword()), type);
					bz.saveData();
					
				} catch (BugzillaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					displayMsg(e1);
				}
			}
		});
		
		btnRegister.setBounds(273, 61, 124, 23);
		frame.getContentPane().add(btnRegister);

	}
	
	private void displayMsg(BugzillaException ex) {
		JOptionPane.showMessageDialog(null, ex.getErrorMsg(), "Error", JOptionPane.ERROR_MESSAGE);
	}
}
