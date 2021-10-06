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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DrawCircle extends JDialog {

	private final JPanel pnlCenter = new JPanel();
	private JButton btnConfirm;
	private JButton btnCancel;
	private JTextField txtRadius;
	
	private boolean isConfirmed;

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DrawCircle dialog = new DrawCircle();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DrawCircle() {
		setBounds(100, 100, 450, 300);
		setTitle("Draw circle");
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(null); //centriranje dijagola
		getContentPane().setLayout(new BorderLayout());
		pnlCenter.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(pnlCenter, BorderLayout.CENTER);
		
		JLabel lblRadius = new JLabel("Radius:");
		
		txtRadius = new JTextField();
		txtRadius.setColumns(10);
		
		JLabel lblCircle = new JLabel("Circle");
		GroupLayout gl_pnlCenter = new GroupLayout(pnlCenter);
		gl_pnlCenter.setHorizontalGroup(
			gl_pnlCenter.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_pnlCenter.createSequentialGroup()
					.addGap(51)
					.addComponent(lblRadius)
					.addPreferredGap(ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
					.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(119))
				.addGroup(gl_pnlCenter.createSequentialGroup()
					.addGap(114)
					.addComponent(lblCircle)
					.addContainerGap(264, Short.MAX_VALUE))
		);
		gl_pnlCenter.setVerticalGroup(
			gl_pnlCenter.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlCenter.createSequentialGroup()
					.addGap(27)
					.addComponent(lblCircle)
					.addGap(74)
					.addGroup(gl_pnlCenter.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblRadius))
					.addContainerGap(81, Short.MAX_VALUE))
		);
		pnlCenter.setLayout(gl_pnlCenter);
		{
			JPanel pnlSouth = new JPanel();
			getContentPane().add(pnlSouth, BorderLayout.SOUTH);
			{
				btnConfirm = new JButton("Confirm");
				btnConfirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(txtRadius.getText().trim().equals("")){
							getToolkit().beep(); // zvuk
							JOptionPane.showMessageDialog(null, "Please insert radius!", "Error", JOptionPane.ERROR_MESSAGE, null);
							isConfirmed = false;
							return;
						}
						try {
							validate (txtRadius.getText());
						} catch (NumberFormatException exc){
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Invalid data type!", "Error", JOptionPane.ERROR_MESSAGE, null);
							isConfirmed = false;
							return;	
						}
						if(Integer.parseInt(txtRadius.getText()) < 0)
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE, null);
							isConfirmed = false;
							return;
						}else
						{
							isConfirmed = true;
							dispose();
						}
						
						
					}
				});
				btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 15));
				getRootPane().setDefaultButton(btnConfirm);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
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
						.addGap(73)
						.addComponent(btnConfirm)
						.addGap(161)
						.addComponent(btnCancel)
						.addGap(90))
			);
			gl_pnlSouth.setVerticalGroup(
				gl_pnlSouth.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_pnlSouth.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_pnlSouth.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnCancel)
							.addComponent(btnConfirm)))
			);
			pnlSouth.setLayout(gl_pnlSouth);
		}
	}
	
	public void validate (String radius)
	{
		String supp = "^(([+-])?([1-9]{1})([0-9]+)?)$";
		if(!radius.matches(supp))
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
}


