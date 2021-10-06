package modification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import geometry.Circle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgCircleModify extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblYcoordinate;
	private JTextField txtXcoordinate;
	private JTextField txtYcoordinate;
	private JTextField txtRadius;
	private JButton btnConfirm;
	private JButton btnCancel;
	private Color innerColor = new Color (0, 0, 0);
	private Color edgeColor = new Color (255, 255, 255);
	private boolean isConfirmed;
	private JButton btnEdgeColor;
	private JButton btnInnerColor;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgCircleModify dialog = new DlgCircleModify();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgCircleModify() {
		setBounds(100, 100, 450, 400);
		setTitle("Circle modify");
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		{
			lblYcoordinate = new JLabel("Center x coordinate:");
		}
		
		JLabel lblXcoordinate = new JLabel("Center y coordinate:");
		txtXcoordinate = new JTextField();
		txtXcoordinate.setColumns(10);
		txtYcoordinate = new JTextField();
		txtYcoordinate.setColumns(10);
		JLabel lblRadius = new JLabel("Radius:");
		btnEdgeColor = new JButton("Select edge color");
		btnEdgeColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edgeColor = JColorChooser.showDialog(null, "Edge color", edgeColor);
				if(edgeColor != null)
				{
					btnEdgeColor.setBackground(edgeColor);
				}
				
				
				
				
			}
		});
		btnInnerColor = new JButton("Select inner color");
		btnInnerColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				innerColor = JColorChooser.showDialog(null, "Inner color", innerColor);
				if(innerColor != null)
				{
					btnInnerColor.setBackground(innerColor);
				}
				
			}
		});
		txtRadius = new JTextField();
		txtRadius.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
							.addGap(20)
							.addComponent(btnInnerColor, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
							.addGap(20)
							.addComponent(btnEdgeColor, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, gl_contentPanel.createSequentialGroup()
							.addGap(30)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblRadius)
									.addPreferredGap(ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
									.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblYcoordinate)
									.addPreferredGap(ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
									.addComponent(txtXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblXcoordinate)
									.addPreferredGap(ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
									.addComponent(txtYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(28)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblYcoordinate))
					.addGap(35)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblXcoordinate)
						.addComponent(txtYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(29)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblRadius)
						.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(36)
					.addComponent(btnEdgeColor, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addGap(33)
					.addComponent(btnInnerColor, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(55, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnConfirm = new JButton("Confirm");
				btnConfirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						try {
							validate(txtXcoordinate.getText(), txtYcoordinate.getText(), txtRadius.getText());
						} catch (NumberFormatException ne) {
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Invalid data type!", "Error", JOptionPane.ERROR_MESSAGE, null);
							return;
						}
						
						if(txtXcoordinate.getText().trim().equals("") || txtYcoordinate.getText().trim().equals("") || txtRadius.getText().trim().equals(""))
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Values must be field!", "Error", JOptionPane.ERROR_MESSAGE, null);
						}
						else if (Integer.parseInt(txtRadius.getText()) < 0)
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE, null);
						}
						else
						{
							isConfirmed = true;
							setVisible(false);
						}
						
					}
				});
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
				btnCancel.setActionCommand("Cancel");
			}
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(98)
						.addComponent(btnConfirm)
						.addGap(137)
						.addComponent(btnCancel)
						.addGap(89))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnConfirm)
							.addComponent(btnCancel)))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}
	
	public void validate (String x, String y, String radius)
	{
		String supp = "^(([+-])?([1-9]{1})([0-9]+)?)$";
		if(x.matches("") || y.matches("") || radius.matches(""))
		{
			throw new NumberFormatException();
		}
		else if(!x.matches(supp) || !y.matches(supp) || !radius.matches(supp))
		{
			throw new NumberFormatException();
		}
	}

	public JTextField getTxtXcoordinate() {
		return txtXcoordinate;
	}

	public JTextField getTxtYcoordinate() {
		return txtYcoordinate;
	}

	public JTextField getTxtRadius() {
		return txtRadius;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public JButton getBtnEdgeColor() {
		return btnEdgeColor;
	}

	public JButton getBtnInnerColor() {
		return btnInnerColor;
	}
}
