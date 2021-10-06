package modification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import geometry.Donut;
import geometry.Point;
import geometry.Rectangle;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgDonutModify extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnConfirm;
	private JButton btnCancel;
	private JButton btnInnerColor;
	private JButton btnEdgeColor;
	private JTextField txtXcoordinate;
	private JTextField txtYcoordinate;
	private JTextField txtRadius;
	private JTextField txtInnerRadius;
	private Color edgeColor = new Color(0, 0, 0);
	private Color innerColor = new Color(255, 255, 255);
	private Donut donut;
	private boolean isConfirmed;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgDonutModify dialog = new DlgDonutModify();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgDonutModify() {
		setBounds(100, 100, 450, 400);
		setTitle("Donut modify");
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblXcoordinate = new JLabel("Center X coordinate");
		JLabel lblYcoordinate = new JLabel("Center Y coordinate");
		JLabel lblRadius = new JLabel("Radius");
		JLabel lblInnerRadius = new JLabel("InnerRadius");
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
		txtXcoordinate = new JTextField();
		txtXcoordinate.setColumns(10);
		txtYcoordinate = new JTextField();
		txtYcoordinate.setColumns(10);
		txtRadius = new JTextField();
		txtRadius.setColumns(10);
		txtInnerRadius = new JTextField();
		txtInnerRadius.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addGap(27)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnInnerColor, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
						.addComponent(btnEdgeColor, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblXcoordinate)
								.addComponent(lblInnerRadius)
								.addComponent(lblYcoordinate)
								.addComponent(lblRadius))
							.addPreferredGap(ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(txtXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtInnerRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(94))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblXcoordinate))
					.addGap(25)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblYcoordinate)
						.addComponent(txtYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRadius)
						.addComponent(txtRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(27)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblInnerRadius)
						.addComponent(txtInnerRadius, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(37)
					.addComponent(btnEdgeColor)
					.addGap(18)
					.addComponent(btnInnerColor)
					.addContainerGap(57, Short.MAX_VALUE))
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
							validate(txtXcoordinate.getText(), txtYcoordinate.getText(), txtRadius.getText(), txtInnerRadius.getText());
						} catch (NumberFormatException ne) {
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Invalid data type!", "Error", JOptionPane.ERROR_MESSAGE, null);
							return;
						}
						
						if(txtXcoordinate.getText().trim().equals("") || txtYcoordinate.getText().trim().equals("") || txtRadius.getText().trim().equals("") || txtRadius.getText().trim().equals(""))
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Values must be field!", "Error", JOptionPane.ERROR_MESSAGE, null);
						}
						else if (Integer.parseInt(txtRadius.getText()) < 0 || Integer.parseInt(txtInnerRadius.getText()) < 0)
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius and inner radius must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE, null);
						}
						else if (Integer.parseInt(txtRadius.getText()) < Integer.parseInt(txtInnerRadius.getText()))
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius must be greater than inner Radius!", "Error", JOptionPane.ERROR_MESSAGE, null);
						}
						else if (Integer.parseInt(txtRadius.getText()) == Integer.parseInt(txtInnerRadius.getText()))
						{
							getToolkit().beep();
							JOptionPane.showMessageDialog(null, "Radius and inner radius can not be equal!", "Error", JOptionPane.ERROR_MESSAGE, null);
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
						.addGap(107)
						.addComponent(btnConfirm)
						.addGap(142)
						.addComponent(btnCancel)
						.addGap(75))
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
	
	public void validate (String x, String y, String radius, String innerRadius)
	{
		String supp = "^(([+-])?([1-9]{1})([0-9]+)?)$";
		if(x.matches("") || y.matches("") || radius.matches("") || innerRadius.matches(""))
		{
			throw new NumberFormatException();
		}
		else if(!x.matches(supp) || !y.matches(supp) || !radius.matches(supp) || !innerRadius.matches(supp))
		{
			throw new NumberFormatException();
		}
	}

	public JButton getBtnInnerColor() {
		return btnInnerColor;
	}

	public JButton getBtnEdgeColor() {
		return btnEdgeColor;
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

	public JTextField getTxtInnerRadius() {
		return txtInnerRadius;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

}
