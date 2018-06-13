import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

class MyEditor implements ActionListener{
	
	JFrame jf;
	JLabel jl;
	JTextField jtf;
	JTextArea jta,jta1;
	JButton Jbcompile,Jbrun;
	JScrollPane jsp,jsp1;
	Runtime r;
	String str="";
	String fname="";
	String result="";
	String result1="";
	
	MyEditor(){
		jf=new JFrame("MyEditor");
		jf.setLayout(null);
		jl=new JLabel("Enter Java Class Name");
		jl.setBounds(20,20,130,25);
		
		jtf=new JTextField();
		jtf.setBounds(180, 20, 230, 25);
		
		jf.add(jl);
		jf.add(jtf);
		jta=new JTextArea(50,50);
		
		jta.addFocusListener(new MyFocusListener(this));
		jta1=new JTextArea(50,50);
		
		jta.setFont(new Font("varinda",Font.PLAIN,15));
		jta1.setFont(new Font("varinda",Font.PLAIN,15));
		
		jsp=new JScrollPane(jta);
		jsp1=new JScrollPane(jta1);
		
		jsp.setBounds(50,60,320,150);
		jsp1.setBounds(50,270,320,150);
		
		jf.add(jsp);
		jf.add(jsp1);
		
		Jbcompile=new JButton("Compile");
		Jbrun=new JButton("Run");
		
		Jbcompile.setBounds(100,230,80,25);
		Jbrun.setBounds(280,230,80,25);
		
		r=Runtime.getRuntime();
		jf.add(Jbcompile);
		jf.add(Jbrun);
		
		Jbcompile.addActionListener(this);
		Jbrun.addActionListener(this);
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(550,550);
		jf.setVisible(true);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==Jbcompile)
		{
			jta1.setText("");
			str=" ";
			if(!jtf.getText().toString().equals(""))
			{
				try{
					fname=jtf.getText().trim()+".java";
					FileWriter fw=new FileWriter(fname);
					String s1=jta.getText();
					
					PrintWriter pw=new PrintWriter(fw);
					pw.println(s1);
					pw.flush();
					
					Process error=r.exec("C:\\Program Files\\Java\\jdk1.8.0_121\\bin\\javac.exe -d . "+ fname);
					
					BufferedReader err = new BufferedReader(new InputStreamReader(error.getErrorStream()));
					
					while(true)
					{
						String temp= err.readLine();
						if(temp!=null)
						{
							result+=temp;
							result+="\n";
						}
						else 
							break;
					}
					if(result.equals(""))
					{
						jta1.setText("Compilation Sucessful!"+ fname);
						err.close();
					}
					
					else{
						jta1.setText(result);
					}
				}
				catch(Exception e1){
					System.out.println(e1);
				}
			}
			else{
				jta1.setText("Please Enter java Program name!");
			}
		}
		else if(e.getSource()==Jbrun)
		{
			int start=0;
			try{
				String fn=jtf.getText().trim();
				
				ProcessBuilder pb = new ProcessBuilder("java", fn);
				Process p=pb.start();
				BufferedReader output= new BufferedReader(new InputStreamReader(p.getInputStream()));
				BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				
				while(true)
				{
					String temp=output.readLine();
					if(temp!=null)
					{
						result+=temp+"\n";
					}
					else 
						break;
				}
				
				while(true)
				{
					String temp=error.readLine();
					if(temp!=null)
					{
						result1+=temp+"\n";
					}
					else 
						break;
				}
				//output.reset();
				//error.reset();
				output.close();
				error.close();
				jta1.setText(result+"\n"+result1);
			}
			catch(Exception e1)
			{
				System.out.println(e1);
			}
		}
		
	}
	
	public static void main(String s[]){
		new MyEditor();
	}
	
}

class MyFocusListener extends FocusAdapter{
	
	MyEditor e;
	
	MyFocusListener(MyEditor e){
		this.e=e;
	}
	
	public void focusGained(FocusEvent fe){
		
		String str=e.jtf.getText().trim();
		
		if(str.equals("")){}
		else if(!e.jta.getText().equals("")){}
		else{
			e.jta.setText("public class "+ str+"\n"+
						"{"+"\n"+
						"public static void main (String ...s)"+"\n"+
						"{\n"+
						"                                         \n"+
						"}" + "\n"
						+"}");
		}
		
	}
	
}