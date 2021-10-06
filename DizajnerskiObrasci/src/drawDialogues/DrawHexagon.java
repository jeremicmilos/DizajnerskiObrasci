package drawDialogues;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DrawHexagon extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private boolean isConfirmed;
	private JLabel lblHexagon;
	private JPanel buttonPane;
	private JTextField txtRadius;
	private JButton btnConfirm;
	private JButton cancelButton;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DrawHexagon dialog = new DrawHexagon();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DrawHexagon() {
		setBounds(100, 100, 450, 300);
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(null);
		setTitle("Draw Hexagon");
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 436, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 18, Short.MAX_VALUE)
		);
		contentPanel.setLayout(gl_contentPanel);
		
		{
			buttonPane = new JPanel();
			{
				btnConfirm = new JButton("Confirm");
				btnConfirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
                            validate(txtRadius.getText());
                            if (txtRadius.getText().trim().equals("")) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Field is empty! Please insert radius", "Error",
                                        JOptionPane.ERROR_MESSAGE, null);
                                isConfirmed = false;
                                return;
                            } else if (Integer.parseInt(txtRadius.getText()) < 0) {
                                getToolkit().beep();
                                JOptionPane.showMessageDialog(null, "Radius can't be negative number!", "Error",
                                        JOptionPane.ERROR_MESSAGE, null);
                                isConfirmed = false;
                                return;
                            } else {
                            	isConfirmed = true;
                                dispose();
                            }
                        } catch (NumberFormatException exc) {
                            getToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Invalid data type inserted!", "Error",
                                    JOptionPane.ERROR_MESSAGE, null);
                            isConfirmed = false;
                            return;
                        }
					}
				});
				btnConfirm.setActionCommand("OK");
				getRootPane().setDefaultButton(btnConfirm);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
			}
		}
		{
			lblHexagon = new JLabel("Hexagon");
			lblHexagon.setFont(new Font("Tahoma", Font.PLAIN, 20));
		}
		
		JLabel lblRadius = new JLabel("Radius:");
		lblRadius.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		txtRadius = new JTextField();
		txtRadius.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(48)
							.addComponent(lblRadius, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addGap(21)
							.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(150)
							.addComponent(lblHexagon)))
					.addContainerGap(185, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, 446, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(contentPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblHexagon)
					.addGap(67)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRadius, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(61)
					.addComponent(buttonPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(29))
		);
		GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
		gl_buttonPane.setHorizontalGroup(
			gl_buttonPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addGap(201)
					.addComponent(btnConfirm)
					.addGap(18)
					.addComponent(cancelButton)
					.addGap(95))
		);
		gl_buttonPane.setVerticalGroup(
			gl_buttonPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnConfirm)
						.addComponent(cancelButton)))
		);
		buttonPane.setLayout(gl_buttonPane);
		getContentPane().setLayout(groupLayout);
	}
	
	public void validate(String radius) {
        String supp = "^(([+-])?([1-9]{1})([0-9]+)?)$";
        if (!radius.matches(supp)) {
            throw new NumberFormatException();
        }
    }

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public JTextField getTxtRadius() {
		return txtRadius;
	}
}
