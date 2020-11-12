package loanAssistant;
import javax.swing.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
public class LoanAssistant {
	private boolean isValid(String str) {
		return str != null && str.matches("[-+]?\\d*\\.?\\d+");
	}
	LoanAssistant(){
		JFrame f=new JFrame("Loan Assistant");		
		JLabel bal=new JLabel("Loan Balance");
		JLabel rate=new JLabel("Interest Rate");
		JLabel no=new JLabel("Number of Payments");
		JLabel monthly=new JLabel("Monthly Payments");
		JLabel analysis=new JLabel("Loan Analysis:");		
		JButton exit=new JButton("Exit");
		JButton compute=new JButton();
		JButton next=new JButton("New Loan Analysis");
		JButton byMonth=new JButton("X");
		JButton byNo=new JButton("X");		
		JTextField loan=new JTextField();
		JTextField interest=new JTextField();
		JTextField payments=new JTextField();
		JTextField months=new JTextField();		
		JTextArea result=new JTextArea();		
		f.add(bal);f.add(rate);f.add(no);f.add(monthly);f.add(analysis);
		f.add(exit);f.add(compute);f.add(next);f.add(byMonth);f.add(byNo);
		f.add(loan);f.add(interest);f.add(payments);f.add(months);f.add(result);		
		f.setSize(650,250);
		bal.setBounds(30,20,170,20);
		rate.setBounds(30,50,170,20);
		no.setBounds(30,80,170,20);
		monthly.setBounds(30,110,170,20);
		analysis.setBounds(380,20,170,20);
		compute.setBounds(50,140,195,20);
		next.setBounds(70,170,150,20);
		exit.setBounds(450,180,56,20);
		byNo.setBounds(327,80,45,20);
		byMonth.setBounds(327,110,45,20);
		loan.setBounds(220,20,100,20);
		interest.setBounds(220,50,100,20);
		payments.setBounds(220,80,100,20);
		months.setBounds(220,110,100,20);
		result.setBounds(380,50,200,120);
		f.setLayout(null);
		f.setVisible(true);
		loan.setHorizontalAlignment(SwingConstants.RIGHT);
		interest.setHorizontalAlignment(SwingConstants.RIGHT);
		payments.setHorizontalAlignment(SwingConstants.RIGHT);
		months.setHorizontalAlignment(SwingConstants.RIGHT);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		compute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double balance,rat,month,mutiplier,last;
				int payment;
				if(!isValid(loan.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Loan Balance field \nPlease correct","Balance Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					loan.setText("");
					loan.requestFocus();
					return;
				}
				if(!isValid(interest.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Interest Rate field \nPlease correct","Interest Rate Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					interest.setText("");
					interest.requestFocus();
					return;
				}
				if(byMonth.isVisible()&&!isValid(payments.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Payment No. field \nPlease correct","Payment Number Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					payments.setText("");
					payments.requestFocus();
					return;
				}
				if(byNo.isVisible()&&!isValid(months.getText())) {
					JOptionPane.showConfirmDialog(null,"Invalid or empty Monthly Payment field \nPlease correct","Monthly Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
					months.setText("");
					months.requestFocus();
					return;
				}
				balance=Double.valueOf(loan.getText()).doubleValue();
				rat=Double.valueOf(interest.getText()).doubleValue();
				rat/=1200;
				last=balance;
				if(byMonth.isVisible()) {
					payment=Integer.valueOf(payments.getText()).intValue();
					if(payment>balance||payment==0) {
						JOptionPane.showConfirmDialog(null,"Invalid No. of Payment field \nPlease provide proper value","Number of Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
						payments.setText("");
						payments.requestFocus();
						return;
					}
					if(rat==0) {
						month=balance/payment;
						months.setText(new DecimalFormat("0.00").format(month));
					}
					else {
					mutiplier=Math.pow(1+rat,payment);
					month=balance*rat*mutiplier/(mutiplier-1);
					months.setText(new DecimalFormat("0.00").format(month));
					}
				}
				else {
					month=Double.valueOf(months.getText()).doubleValue();
					if(month>balance||month==0){
						JOptionPane.showConfirmDialog(null,"Invalid Monthly Payment field \nPlease provide proper value","Monthly Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
						months.setText("");
						months.requestFocus();
						return;
					}
					if(month<=balance*rat+1.0) {
						if(JOptionPane.showConfirmDialog(null,"Minimum payment must be Rs. "+new DecimalFormat("0.00").format((int)(balance*rat+1.0))+"\nDo you want to use minimum payment?","Input Error",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
							months.setText(new DecimalFormat("0.00").format((int)(balance*rat+1.0)));
							month=(int)(balance*rat+1.0);
						}
						else {
							JOptionPane.showConfirmDialog(null,"Enter Monthly Payment field again","Monthly Payment Input Error",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
							months.setText("");
							months.requestFocus();
							return;
						}
					}
					if(rat==0) payment=(int)(balance/month);
					else  payment=(int)((Math.log(month)-Math.log(month-balance*rat))/Math.log(1+rat));
					for(int i=1;i<=payment-1;i++) last+=last*rat-month;
					if(last>month) {
						last+=last*rat-month;
						payment++;
					}
					payments.setText(String.valueOf(payment));
				}
				
				result.setText("Loan Balance : Rs. "+new DecimalFormat("0.00").format(balance));
				result.append("\n"+"Interest Rate : "+new DecimalFormat("0.00").format(rat*1200)+"%");
				result.append("\n\n"+String.valueOf(payment-1)+" Payments of Rs. "+new DecimalFormat("0.00").format(month));
				result.append("\n"+"Final Payment of Rs. "+new DecimalFormat("0.00").format(last));
				result.append("\n"+"Total Payment of Rs. "+new DecimalFormat("0.00").format((payment-1)*month+last));
				result.append("\n"+"Interest Paid : Rs. "+new DecimalFormat("0.00").format((payment-1)*month+last-balance));
				compute.setEnabled(false);
				next.setEnabled(true);
				next.requestFocus();
			}
		});
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(byMonth.isVisible()) months.setText("");
				else payments.setText("");
				result.setText("");
				next.setEnabled(false);
				compute.setEnabled(true);
				loan.requestFocus();
			}
		});
		byNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byMonth.setVisible(true);
				byNo.setVisible(false);
				compute.setText("Compute Monthly Payments");
				months.setText("");
				payments.setEditable(true);
				months.setEditable(false);
				months.setBackground(Color.YELLOW);
				payments.setBackground(Color.WHITE);
				months.setFocusable(false);
				payments.setFocusable(true);
				if(loan.getText().equals("")) loan.requestFocus();
				else payments.requestFocus();
			}
		});
		byMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byMonth.setVisible(false);
				byNo.setVisible(true);
				compute.setText("Compute No. of Payments");
				payments.setText("");
				payments.setEditable(false);
				months.setEditable(true);
				months.setBackground(Color.WHITE);
				payments.setBackground(Color.YELLOW);
				months.setFocusable(true);
				payments.setFocusable(false);
				if(loan.getText().equals("")) loan.requestFocus();
				else months.requestFocus();
			}
		});
		result.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		result.setFocusable(false);
		byNo.setFocusable(false);
		byMonth.setFocusable(false);
		compute.setFocusable(false);
		next.setFocusable(false);
		exit.setFocusable(false);
		byNo.doClick();
	}
	public static void main(String[] args) {
		new LoanAssistant();
	}
}
