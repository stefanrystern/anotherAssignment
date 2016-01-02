import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MemberUI {

	private enum ActionType {
		SUBMIT,
		INVALIDATE,
		CONFIRM,
		START,
		STOP,
		FIX,
		APPROVE,
		REJECT
	}
	
	
	private JFrame frame;
	private JComboBox<ActionType> comboBox;
	private JList<Object> list;
	private JComboBox<Bug.Resolution> comboBoxType;
	
	private JTextArea textDescription;
	private JTextArea textSolution;
	
	private JLabel lblStateVal;
	private Bugzilla bz;
	private String username;
	private Map<Integer, Bug> bugs;
	private int bugID;
	private JFrame login;

	/**
	 * Create the application.
	 * @param parent 
	 */
	public MemberUI(JFrame frame, Bugzilla b, String user) {
		bz = b;
		username = user;
		login = frame;
		initialize();
	}


	/**
	 * Display window
	 */
	public void show(boolean val) {
		frame.setVisible(val);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					bz.logout(username);
					login.setVisible(true);
				} catch (BugzillaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					displayMsg(e1);
					
				}
			}
		});
		frame.setResizable(false);
		frame.setBounds(100, 100, 502, 321);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		list = new JList<Object>();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				
				bugID = (list.getSelectedValue() == null? bugID: (int) list.getSelectedValue());
				if(bugs.containsKey(bugID)) {
					
					textDescription.setText(
							bugs.get(bugID).getBugDescription());
					textSolution.setText(
							bugs.get(bugID).getSolutionInfo());
					lblStateVal.setText(
							bugs.get(bugID).getState().toString());
					comboBoxType.setSelectedItem(
							bugs.get(bugID).getSolutionType());
				}
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setBounds(20, 35, 135, 155);
		frame.getContentPane().add(list);
		
		JLabel lblBugList = new JLabel("Bug List");
		lblBugList.setBounds(20, 10, 67, 14);
		frame.getContentPane().add(lblBugList);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(165, 10, 87, 14);
		frame.getContentPane().add(lblDescription);
		
		JLabel lblSolution = new JLabel("Sol. Info");
		lblSolution.setBounds(165, 107, 67, 14);
		frame.getContentPane().add(lblSolution);
		
		JLabel lblState = new JLabel("State");
		lblState.setBounds(20, 201, 39, 14);
		frame.getContentPane().add(lblState);
		
		lblStateVal = new JLabel("");
		lblStateVal.setBounds(64, 201, 91, 14);
		frame.getContentPane().add(lblStateVal);
		
		JLabel lblResolve = new JLabel("Type");
		lblResolve.setBounds(20, 234, 39, 14);
		frame.getContentPane().add(lblResolve);
		
		JLabel lblActions = new JLabel("Actions");
		lblActions.setBounds(165, 208, 54, 14);
		frame.getContentPane().add(lblActions);
		
		comboBox = new JComboBox<ActionType>();
		comboBox.setModel(new DefaultComboBoxModel<ActionType>(ActionType.values()));
		comboBox.setBounds(227, 205, 87, 20);
		frame.getContentPane().add(comboBox);
		
		
		comboBoxType = new JComboBox<Bug.Resolution>();
		comboBoxType.setModel(new DefaultComboBoxModel<Bug.Resolution>(Bug.Resolution.values()));
		comboBoxType.setBounds(64, 231, 91, 20);
		frame.getContentPane().add(comboBoxType);
		
		
		textDescription = new JTextArea();
		textDescription.setBounds(165, 35, 305, 61);
		frame.getContentPane().add(textDescription);
		
		textSolution = new JTextArea();
		textSolution.setBounds(165, 129, 305, 61);
		frame.getContentPane().add(textSolution);
		
		JButton btnPerformAction = new JButton("Perform Action");
		btnPerformAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ActionType act = comboBox.getItemAt(comboBox.getSelectedIndex());
				try {
					
					String solution = textSolution.getText();
					String description = textDescription.getText();
					Bug.Resolution resType = comboBoxType.getItemAt(comboBoxType.getSelectedIndex());
					
					if(act == ActionType.SUBMIT) {
						bz.submitBug(username, description);
					}
					else if (act == ActionType.CONFIRM) {
						bz.confirmBug(username, bugID);
					}
					else if (act == ActionType.INVALIDATE) {
						bz.invalidateBug(username, bugID, solution);
					}
					else if (act == ActionType.START) {
						bz.startDevelopment(username, bugID);
					}
					else if (act == ActionType.STOP) {
						bz.stopDevelopment(username, bugID);
					}
					else if (act == ActionType.FIX) {
						bz.fixedBug(username, bugID, resType, solution);
					}
					else if (act == ActionType.REJECT) {
						bz.rejectFix(username, bugID);
					}
					else if (act == ActionType.APPROVE) {
						bz.approveFix(username, bugID);
					}
					
					bz.saveData();
					loadUIData();
					
				} catch (BugzillaException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					displayMsg(e1);
				}
			}
		});
		btnPerformAction.setBounds(324, 204, 146, 23);
		frame.getContentPane().add(btnPerformAction);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		btnLogout.setBounds(324, 248, 146, 23);
		frame.getContentPane().add(btnLogout);
		
		bugs = bz.getBugList();
		
		try {
			loadUIData();
		} catch (BugzillaException e1) {
			displayMsg(e1);
		}
		
	}
	
	private void displayMsg(BugzillaException ex) {
		JOptionPane.showMessageDialog(null, ex.getErrorMsg(), "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void loadUIData() throws BugzillaException {
		bugID = list.getSelectedValue() == null? bugID: (int)list.getSelectedValue();

		try {
			if(!bugs.isEmpty()) {
				list.setListData((Object[])bugs.keySet().toArray());
				if(!bugs.containsKey(bugID)) {
					bugID = 0;
				}
				list.setSelectedValue(bugID, true);

				textDescription.setText(
						bugs.get(bugID).getBugDescription());
				textSolution.setText(
						bugs.get(bugID).getSolutionInfo());
				lblStateVal.setText(
						bugs.get(bugID).getState().toString());
				comboBoxType.setSelectedItem(
						bugs.get(bugID).getSolutionType());
			}
		} catch (Exception ex) {

			ex.printStackTrace();
			throw new BugzillaException(BugzillaException.ErrorType.UNKNOWN_ERROR);
		}
	}
}
