package drawDialogues;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class DrawDonut extends JDialog {

	private final JPanel pnlCenter = new JPanel();
	private JButton btnConfirm;
	private JButton btnCancel;
	private JTextField txtRadius;
	private JTextField txtInnerRadius;
	private boolean isConfirmed;
	private JLabel lblDonut;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DrawDonut dialog = new DrawDonut();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DrawDonut() {
		setBounds(100, 100, 450, 300);
		setTitle("Draw donut");
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		pnlCenter.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(pnlCenter, BorderLayout.CENTER);
		
		JLabel lblNewLabel = new JLabel("Radius");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblRadius = new JLabel("Inner Radius");
		lblRadius.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtRadius = new JTextField();
		txtRadius.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtRadius.setColumns(10);
		
		txtInnerRadius = new JTextField();
		txtInnerRadius.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtInnerRadius.setColumns(10);
		
		lblDonut = new JLabel("Donut");
		lblDonut.setFont(new Font("Arial", Font.PLAIN, 15));
		GroupLayout gl_pnlCenter = new GroupLayout(pnlCenter);
		gl_pnlCenter.setHorizontalGroup(
			gl_pnlCenter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlCenter.createSequentialGroup()
					.addGroup(gl_pnlCenter.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlCenter.createSequentialGroup()
							.addGap(44)
							.addGroup(gl_pnlCenter.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRadius)
								.addComponent(lblNewLabel))
							.addGap(87)
							.addGroup(gl_pnlCenter.createParallelGroup(Alignment.LEADING)
								.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtInnerRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_pnlCenter.createSequentialGroup()
							.addGap(87)
							.addComponent(lblDonut)))
					.addContainerGap(85, Short.MAX_VALUE))
		);
		gl_pnlCenter.setVerticalGroup(
			gl_pnlCenter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlCenter.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblDonut)
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addGroup(gl_pnlCenter.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addGap(47)
					.addGroup(gl_pnlCenter.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRadius)
						.addComponent(txtInnerRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(63, Short.MAX_VALUE))
		);
		pnlCenter.setLayout(gl_pnlCenter);
		{
			JPanel pnlSouth = new JPanel();
			getContentPane().add(pnlSouth, BorderLayout.SOUTH);
			{
				btnConfirm = new JButton("Confirm");
				btnConfirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(txtRadius.getText().trim().equals("") || txtInnerRadius.getText().trim().equals(""))
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Some fields are empty", "Error", JOptionPane.ERROR_MESSAGE, null);
							isConfirmed = false;
							return;
						}
						try {
							validate(txtRadius.getText(), txtInnerRadius.getText());
							
						} catch (NumberFormatException exc) {
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Invalid data type!", "Error", JOptionPane.ERROR_MESSAGE, null);
							isConfirmed = false;
							return;
						}
						if(Integer.parseInt(txtRadius.getText()) < 1 || Integer.parseInt(txtInnerRadius.getText()) < 1)
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius or inner Radius can't be negative!", "Error", JOptionPane.ERROR_MESSAGE, null);
							isConfirmed = false;
							return;
						}else if (Integer.parseInt(txtRadius.getText()) < Integer.parseInt(txtInnerRadius.getText()))
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius must be greater than inner Radius!", "Error", JOptionPane.ERROR_MESSAGE, null);
							isConfirmed = false;
							return;
						}
						else if (Integer.parseInt(txtRadius.getText()) == Integer.parseInt(txtInnerRadius.getText()))
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius and inner radius can not be equal!", "Error", JOptionPane.ERROR_MESSAGE, null);
						}else
						{
							isConfirmed = true;
							dispose();
						}
					}
				});
				btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 15));
				btnConfirm.setActionCommand("OK");
				getRootPane().setDefaultButton(btnConfirm);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
				btnCancel.setActionCommand("Cancel");
			}
			GroupLayout gl_pnlSouth = new GroupLayout(pnlSouth);
			gl_pnlSouth.setHorizontalGroup(
				gl_pnlSouth.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_pnlSouth.createSequentialGroup()
						.addGap(158)
						.addComponent(btnConfirm)
						.addGap(73)
						.addComponent(btnCancel)
						.addGap(88))
			);
			gl_pnlSouth.setVerticalGroup(
				gl_pnlSouth.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_pnlSouth.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_pnlSouth.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnConfirm)
							.addComponent(btnCancel))
						.addGap(2))
			);
			pnlSouth.setLayout(gl_pnlSouth);
		}
	}
	public void validate (String radius, String innerRadius)
	{
		String supp = "^(([+-])?([1-9]{1})([0-9]+)?)$";
		if(!radius.matches(supp)|| !innerRadius.matches(supp))
		{
			throw new NumberFormatException();
		}
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public JTextField getTxtRadius() {
		return txtRadius;
	}

	public void setTxtRadius(JTextField txtRadius) {
		this.txtRadius = txtRadius;
	}

	public JTextField getTxtInnerRadius() {
		return txtInnerRadius;
	}

	public void setTxtInnerRadius(JTextField txtInnerRadius) {
		this.txtInnerRadius = txtInnerRadius;
	}
	
}
