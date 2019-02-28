/**
	MIS Project, User Interface Class
	- Coded by:
	  @group Bo.Ma.
	  @author Bovi Andrea
	  @author Marsicovetere Alessio
*/

package gui;
import commons.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class UI extends JFrame {

	private Simulation simulation = null;
	private static final long serialVersionUID = 1L;
	
	private final JConsole console = new JConsole();
	
	public UI(Simulation sim) throws IOException{
		
		this.simulation = sim;
		this.console.setEditable(false);
		this.console.setBorder(new TitledBorder("Console"));
	
		// Collegamento del System.out alla JConsole
		System.setOut(console.getPrintStream());
	}
	
	public void initUI(){
		
		Image icon = Toolkit.getDefaultToolkit().getImage("./icons/ico.png");
		final Simulation s = this.simulation;
		
		this.setIconImage(icon);
		this.setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setTitle("Simulator");
		
		final JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JScrollPane pane = new JScrollPane();
        pane.setBounds(5, 5, 985, 540);
        pane.getViewport().add(console);
        console.setEditable(false);
		console.setBorder(new TitledBorder("Console"));
        pane.setVisible(true);
        panel.add(pane);
        
        System.out.println("View EDIT for more INFO.");
        System.out.println("If you decide to stabilize, not trusting us, we recommend you to change the console output.");
        System.out.println("\nOtherwise, choose carefully your options in the EDIT menu and, SIMULATE!\n\n");
        
        
        JMenuBar menubar = new JMenuBar();
        ImageIcon iconExit = new ImageIcon("./icons/exit.png");
        ImageIcon iconClear = new ImageIcon("./icons/clear.png");
        ImageIcon iconAbout = new ImageIcon("./icons/user.png");
        ImageIcon iconRandom = new ImageIcon("./icons/generator.png");
        ImageIcon iconEdit = new ImageIcon("./icons/edit.png");
        ImageIcon iconStab = new ImageIcon("./icons/stabilization.png");
        ImageIcon iconSim = new ImageIcon("./icons/simulate.png");
        ImageIcon iconEqn = new ImageIcon("./icons/eqn.png");
        final ImageIcon iconHistogram = new ImageIcon("./icons/histogram.png");
        JMenu commands = new JMenu("Commands");
        commands.setMnemonic(KeyEvent.VK_F);
        
        JMenuItem eMenuItemExit = new JMenuItem("Exit", iconExit);
        eMenuItemExit.setMnemonic(KeyEvent.VK_E);
        eMenuItemExit.setToolTipText("Quit Simulation");
        
        eMenuItemExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
                System.exit(0);
            }

        });
        
        JMenuItem eMenuItemView = new JMenuItem("View EQN", iconEqn);
        eMenuItemView.setMnemonic(KeyEvent.VK_V);
        eMenuItemView.setToolTipText("View EQN Model");
        
        eMenuItemView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
                JFrame viewF = new JFrame("Simulator [ View EQN ]");
                JPanel view = new JPanel();
                JLabel eqn = new JLabel();
                ImageIcon iconEqn = new ImageIcon("./graphs/eqn.png");
                eqn.setIcon(iconEqn);

                view.add(eqn);
                viewF.add(view);
                
                viewF.setSize(1050,400);
                viewF.setLocationRelativeTo(null);
                viewF.setVisible(true);
                viewF.setResizable(false);
            }

        });
        
        final JMenuItem eMenuItemSim = new JMenuItem("Simulate", iconSim);
        eMenuItemSim.setMnemonic(KeyEvent.VK_S);
        eMenuItemSim.setToolTipText("Start Simulation");
        
        eMenuItemSim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

            	SwingUtilities.invokeLater(new Runnable(){
                                        @Override
					public void run() {	
						System.out.println("\n");
						startObservation();
					}
            	});
            	
            	// DESCRIZIONE DEL COMANDOOOOO Simulate!!!!	
            	System.out.println("\nWAITING ELABORATION...");
            	System.out.println("\nWe are simulating the SISTEM with:");
            	System.out.println(CommonVariables.numberOfClient + " CLIENTs and " + CommonVariables.numberOfServer + " SERVERs");
            	System.out.println("for " + CommonVariables.numberOfRun + " RUNs.\n\n");
            	
            	
            }
        });
       
        JMenuItem eMenuItemReset = new JMenuItem("Clear", iconClear);
        eMenuItemReset.setMnemonic(KeyEvent.VK_R);
        eMenuItemReset.setToolTipText("Clear Console");
        
        eMenuItemReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            
            	console.setText("");
            }
        });
        
        JMenu about = new JMenu("?");
        about.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem eMenuItemAbout = new JMenuItem("About", iconAbout);
        eMenuItemAbout.setMnemonic(KeyEvent.VK_A);
        eMenuItemAbout.setToolTipText("About :)");
        
        eMenuItemAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
            	JFrame about = new JFrame("Simulator [ About ]");
        		Image iconAbout = Toolkit.getDefaultToolkit().getImage("./icons/user.png");
        		JPanel panelAbout = new JPanel();
        		JLabel labelImage = new JLabel();
        		JLabel label = new JLabel("Developed by: Andrea Bovi - Alessio Marsicovetere");
        		
        		labelImage.setBounds(5, 20, 24, 24);
        		labelImage.setIcon(new ImageIcon("./icons/ico.png"));
        		
        		label.setFont(new Font("Arial Bold", Font.PLAIN, 14));
        		panelAbout.add(labelImage);
        		panelAbout.add(label);
        		about.add(panelAbout);
        		about.setIconImage(iconAbout);
            	about.setVisible(true);
            	about.setSize(400,350);
            	about.setResizable(false);
            	about.setLocationRelativeTo(null);
            }

        });
        
        final JMenu tool = new JMenu("Tools");
        tool.setMnemonic(KeyEvent.VK_T);
        
        JMenuItem eMenuItemTest = new JMenuItem("Test Generators", iconRandom);
        eMenuItemTest.setMnemonic(KeyEvent.VK_T);
        eMenuItemTest.setToolTipText("Start Test Generators");
        
        eMenuItemTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	System.out.println("\n\nWe are performing Test of the used Generators, with setted values:");
            	System.out.println("\nRandom Generator -> Seed = 301");
            	System.out.println("Uniform Generator -> from Random Generator");
            	System.out.println("Exponential Generator -> from Uniform Generator, avg = 0.6");
            	System.out.println("HyperExponential Generator -> Seed = 5033, p = 0.3, avg = 0.8");
            	System.out.println("1-Erlang Generator -> Seed = 621, avg = 0.2");
            	System.out.println("5-Erlang Generator -> Seed = 621, avg = 0.3");
            	System.out.println("20-Erlang Generator -> Seed = 621, avg = 0.4\n\n");
        		
            	SwingUtilities.invokeLater(new Runnable(){
                                        @Override
					public void run() {
						
		            	JFrame chart = new JFrame("Simulator [ Test Generators ]");
		        		Image iconTest = Toolkit.getDefaultToolkit().getImage("./icons/generator.png");
		        		
		        		chart.setIconImage(iconTest);
		            	ArrayList<JFreeChart> list;
		            	list = testG();
		            	
		            	ChartPanel cp0 = new ChartPanel(list.get(0));
		                cp0.setVisible(true);
		            	ChartPanel cp1 = new ChartPanel(list.get(1));
		                cp1.setVisible(true);
		            	ChartPanel cp2 = new ChartPanel(list.get(2));
		                cp2.setVisible(true);
		            	ChartPanel cp3 = new ChartPanel(list.get(3));
		                cp3.setVisible(true);
		                ChartPanel cp4 = new ChartPanel(list.get(4));
		                cp4.setVisible(true);
		            	ChartPanel cp5 = new ChartPanel(list.get(5));
		                cp5.setVisible(true);
		                ChartPanel cp6 = new ChartPanel(list.get(6));
		                cp6.setVisible(true);
		                
		            	JTabbedPane tabbedPane = new JTabbedPane();
						tabbedPane.addTab("Random", iconHistogram, cp0, "");
						tabbedPane.addTab("Uniform", iconHistogram, cp1, "");
						tabbedPane.addTab("Exponential", iconHistogram, cp2, "");
						tabbedPane.addTab("Hyper Exp", iconHistogram, cp3, "");
						tabbedPane.addTab("1-Erlang", iconHistogram, cp4, "");
						tabbedPane.addTab("5-Erlang", iconHistogram, cp5, "");
						tabbedPane.addTab("20-Erlang", iconHistogram, cp6, "");
						
		                chart.setResizable(false);
		                chart.setSize(800, 600);
		                chart.setLocationRelativeTo(null);
		                chart.add(tabbedPane);
		                
		                chart.setVisible(true);
					}
            	});
            }
        });
        
        JMenuItem eMenuItemEdit = new JMenuItem("Edit", iconEdit);
        eMenuItemEdit.setMnemonic(KeyEvent.VK_E);
        eMenuItemEdit.setToolTipText("Edit System Configuration");
        
        eMenuItemEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
            	
            	SwingUtilities.invokeLater(new Runnable(){
                                        @Override
					public void run() {

						final JFrame editWin = new JFrame("Simulator [ Edit System Configuration ]");
						
		        		Image iconEdit = Toolkit.getDefaultToolkit().getImage("./icons/edit.png");
		        		
		        		editWin.setIconImage(iconEdit);
		 		        		
		        		ImageIcon iconSetts = new ImageIcon("./icons/settings.png");
		        		ImageIcon iconClients = new ImageIcon("./icons/clients.png");
		        		ImageIcon iconQueue = new ImageIcon("./icons/queue.png");
		        		ImageIcon iconCpu = new ImageIcon("./icons/cpu.png");
		        		ImageIcon iconMs = new ImageIcon("./icons/ms.png");
		        		ImageIcon iconGW = new ImageIcon("./icons/router.png");
		        		ImageIcon iconINF = new ImageIcon("./icons/info.png");
		        		ImageIcon iconShow = new ImageIcon("./icons/console.png");
		        		
		        		final JTabbedPane tabbedEditPane = new javax.swing.JTabbedPane();
		        		final JPanel jPanel1 = new javax.swing.JPanel();
		        		final JLabel jLabel1 = new javax.swing.JLabel();
		        		final JRadioButton jRadioButton1 = new javax.swing.JRadioButton();
		        		final JRadioButton jRadioButton2 = new javax.swing.JRadioButton();
		        		final JRadioButton jRadioButton3 = new javax.swing.JRadioButton();
		        		final JRadioButton jRadioButton4 = new javax.swing.JRadioButton();
		        		final JRadioButton jRadioButton5 = new javax.swing.JRadioButton();
		        		final JRadioButton jRadioButton6 = new javax.swing.JRadioButton();
		        		final JRadioButton jRadioButton7 = new javax.swing.JRadioButton();
		        		final JLabel jLabel2 = new javax.swing.JLabel();
		        		final JLabel jLabel3 = new javax.swing.JLabel();
		        		final JLabel jLabel4 = new javax.swing.JLabel();
		        		final JLabel jLabel5 = new javax.swing.JLabel();
		        		final JToggleButton jToggleButton1 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton2 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton3 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton4 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton5 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton6 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton7 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton8 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton9 = new javax.swing.JToggleButton();
		                final JLabel jLabel6 = new javax.swing.JLabel();
		                final JLabel jLabel7 = new javax.swing.JLabel();
		                final  JSeparator jSeparator4 = new javax.swing.JSeparator();
		                final JSeparator jSeparator5 = new javax.swing.JSeparator();
		                final JButton jButton3 = new javax.swing.JButton();
		                final JButton jButton4 = new javax.swing.JButton();
		                final JLabel jLabel9 = new javax.swing.JLabel();
		                final JPanel jPanel2 = new javax.swing.JPanel();
		                final JLabel jLabel8 = new javax.swing.JLabel();
		                final JLabel jLabel10 = new javax.swing.JLabel();
		                final JRadioButton jRadioButton8 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton9 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton10 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton11 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton12 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton13 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton14 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton15 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton16 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton17 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton18 = new javax.swing.JRadioButton();
		                final JRadioButton jRadioButton19 = new javax.swing.JRadioButton();
		                final JToggleButton jToggleButton10 = new javax.swing.JToggleButton();
		                final JToggleButton jToggleButton11 = new javax.swing.JToggleButton();
		                final JButton jButton1 = new javax.swing.JButton();

		                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		                
		                jLabel1.setText("CLIENTs NUMBER:");
		                jLabel1.setIcon(iconClients);
		                
		                jRadioButton1.setSelected(true);
                    	jRadioButton2.setSelected(false);
                    	jRadioButton3.setSelected(false);
                    	jRadioButton4.setSelected(false);
                    	jRadioButton5.setSelected(false);
                    	jRadioButton6.setSelected(false);
                    	jRadioButton7.setSelected(false);
                    	
                    	jToggleButton1.setSelected(false);
                    	jToggleButton2.setSelected(true);
                    	jToggleButton3.setSelected(false);
                    	jToggleButton4.setSelected(true);
                    	jToggleButton5.setSelected(false);
                    	jToggleButton6.setSelected(false);
                    	jToggleButton7.setSelected(false);
                    	jToggleButton8.setSelected(false);
                    	jToggleButton9.setSelected(true);

                    	jToggleButton10.setSelected(false);
                    	jToggleButton11.setSelected(true);
                    	jRadioButton8.setSelected(true);
                    	jRadioButton9.setSelected(false);
                    	jRadioButton10.setSelected(false);
                    	jRadioButton11.setSelected(false);
                    	jRadioButton12.setSelected(false);
                    	jRadioButton13.setSelected(false);
                    	jRadioButton14.setSelected(false);
                    	jRadioButton15.setSelected(true);
                    	jRadioButton16.setSelected(false);
                    	jRadioButton17.setSelected(true);
                    	jRadioButton18.setSelected(true);
                    	jRadioButton19.setSelected(false);
                    	
		                jRadioButton1.setText("12");
		                jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {

		                    	jRadioButton2.setSelected(false);
		                    	jRadioButton3.setSelected(false);
		                    	jRadioButton4.setSelected(false);
		                    	jRadioButton5.setSelected(false);
		                    	jRadioButton6.setSelected(false);
		                    	jRadioButton7.setSelected(false);
		                    }
		                });

		                jRadioButton2.setText("24");
		                jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jRadioButton1.setSelected(false);
		                    	jRadioButton3.setSelected(false);
		                    	jRadioButton4.setSelected(false);
		                    	jRadioButton5.setSelected(false);
		                    	jRadioButton6.setSelected(false);
		                    	jRadioButton7.setSelected(false);
		                    }
		                });

		                jRadioButton3.setText("30");
		                jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jRadioButton1.setSelected(false);
		                    	jRadioButton2.setSelected(false);
		                    	jRadioButton4.setSelected(false);
		                    	jRadioButton5.setSelected(false);
		                    	jRadioButton6.setSelected(false);
		                    	jRadioButton7.setSelected(false);
		                    }
		                });

		                jRadioButton4.setText("40");
		                jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jRadioButton1.setSelected(false);
		                    	jRadioButton2.setSelected(false);
		                    	jRadioButton3.setSelected(false);
		                    	jRadioButton5.setSelected(false);
		                    	jRadioButton6.setSelected(false);
		                    	jRadioButton7.setSelected(false);
		                    }
		                });

		                jRadioButton5.setText("60");
		                jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jRadioButton1.setSelected(false);
		                    	jRadioButton2.setSelected(false);
		                    	jRadioButton3.setSelected(false);
		                    	jRadioButton4.setSelected(false);
		                    	jRadioButton6.setSelected(false);
		                    	jRadioButton7.setSelected(false);
		                    }
		                });

		                jRadioButton6.setText("80");
		                jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jRadioButton1.setSelected(false);
		                    	jRadioButton2.setSelected(false);
		                    	jRadioButton3.setSelected(false);
		                    	jRadioButton4.setSelected(false);
		                    	jRadioButton5.setSelected(false);
		                    	jRadioButton7.setSelected(false);
		                    }
		                });

		                jRadioButton7.setText("100");
		                jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jRadioButton1.setSelected(false);
		                    	jRadioButton2.setSelected(false);
		                    	jRadioButton3.setSelected(false);
		                    	jRadioButton4.setSelected(false);
		                    	jRadioButton5.setSelected(false);
		                    	jRadioButton6.setSelected(false);
		                    }
		                });

		                jLabel2.setText("QUEUE Disciplines:");
		                jLabel2.setIcon(iconQueue);

		                jLabel3.setText("CPUs:");
		                jLabel3.setIcon(iconCpu);

		                jLabel4.setText("GWs:");
		                jLabel4.setIcon(iconGW);
		                
		                jLabel5.setText("MSs:");
		                jLabel5.setIcon(iconMs);
		                
		                jToggleButton1.setText("LIFO");
		                jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton2.isSelected() || jToggleButton3.isSelected() ){
		                    		
		                    		jToggleButton2.setSelected(false);
		                    		jToggleButton3.setSelected(false);
		                    	}
		                    }
		                });
		                
		                jToggleButton2.setText("FIFO");
		                jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton1.isSelected() || jToggleButton3.isSelected() ){
		                    		
		                    		jToggleButton1.setSelected(false);
		                    		jToggleButton3.setSelected(false);
		                    	}
		                    }
		                });

		                jToggleButton3.setText("RAND");
		                jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton1.isSelected() || jToggleButton2.isSelected() ){
		                    		
		                    		jToggleButton1.setSelected(false);
		                    		jToggleButton2.setSelected(false);
		                    	}
		                    }
		                });

		                jToggleButton4.setText("LIFO");
		                jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
                                    @Override
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton5.isSelected() || jToggleButton6.isSelected() ){
		                    		
		                    		jToggleButton5.setSelected(false);
		                    		jToggleButton6.setSelected(false);
		                    	}
		                    }
		                });

		                jToggleButton5.setText("FIFO");
		                jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton4.isSelected() || jToggleButton6.isSelected() ){
		                    		
		                    		jToggleButton4.setSelected(false);
		                    		jToggleButton6.setSelected(false);
		                    	}
		                    }
		                });
		                
		                jToggleButton6.setText("RAND");
		                jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton4.isSelected() || jToggleButton5.isSelected() ){
		                    		
		                    		jToggleButton4.setSelected(false);
		                    		jToggleButton5.setSelected(false);
		                    	}
		                    }
		                });
		                
		                jToggleButton7.setText("LIFO");
		                jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton8.isSelected() || jToggleButton9.isSelected() ){
		                    		
		                    		jToggleButton8.setSelected(false);
		                    		jToggleButton9.setSelected(false);
		                    	}
		                    }
		                });
		                
		                jToggleButton8.setText("FIFO");
		                jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton7.isSelected() || jToggleButton9.isSelected() ){
		                    		
		                    		jToggleButton7.setSelected(false);
		                    		jToggleButton9.setSelected(false);
		                    	}
		                    }
		                });
		                
		                jToggleButton9.setText("RAND");
		                jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	if(jToggleButton7.isSelected() || jToggleButton8.isSelected() ){
		                    		
		                    		jToggleButton7.setSelected(false);
		                    		jToggleButton8.setSelected(false);
		                    	}
		                    }
		                });
		                
		                jLabel6.setText("info");
		                jLabel6.setIcon(iconINF);
		                jLabel6.setToolTipText("Select number of client in simulation. If changed, and you don't trust of our stability, Stabilize!!! Default is: 12.");
		                
		                jLabel7.setText("info");
		                jLabel7.setIcon(iconINF);
		                jLabel7.setToolTipText("Change the discipline's queue of those centers. Default is: CPU-FIFO, GW-LIFO, MS-RAND.");
		                
		                jButton3.setText("DEFAULT");
		                jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		                jButton3.setMaximumSize(new java.awt.Dimension(80, 30));
		                jButton3.setMinimumSize(new java.awt.Dimension(80, 30));
		                jButton3.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jRadioButton1.setSelected(true);
		                    	jRadioButton2.setSelected(false);
		                    	jRadioButton3.setSelected(false);
		                    	jRadioButton4.setSelected(false);
		                    	jRadioButton5.setSelected(false);
		                    	jRadioButton6.setSelected(false);
		                    	jRadioButton7.setSelected(false);
		                    	
		                    	jToggleButton1.setSelected(false);
		                    	jToggleButton2.setSelected(true);
		                    	jToggleButton3.setSelected(false);
		                    	jToggleButton4.setSelected(true);
		                    	jToggleButton5.setSelected(false);
		                    	jToggleButton6.setSelected(false);
		                    	jToggleButton7.setSelected(false);
		                    	jToggleButton8.setSelected(false);
		                    	jToggleButton9.setSelected(true);
		                    }
		                });

		                jButton4.setText("OK");
		                jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		                jButton4.setMaximumSize(new java.awt.Dimension(80, 30));
		                jButton4.setMinimumSize(new java.awt.Dimension(80, 30));
		                jButton4.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	if(jRadioButton1.isSelected()){
		                    		CommonVariables.numberOfClient = 12;
		                    	}
		                    	
		                    	if(jRadioButton2.isSelected()){
		                    		CommonVariables.numberOfClient = 24;
		                    	}
		                    	
		                    	if(jRadioButton3.isSelected()){
		                    		CommonVariables.numberOfClient = 30;
		                    	}
		                    	
		                    	if(jRadioButton4.isSelected()){
		                    		CommonVariables.numberOfClient = 40;
		                    	}
		                    	
		                    	if(jRadioButton5.isSelected()){
		                    		CommonVariables.numberOfClient = 60;
		                    	}
		                    	
		                    	if(jRadioButton6.isSelected()){
		                    		CommonVariables.numberOfClient = 80;
		                    	}
		                    	
		                    	if(jRadioButton7.isSelected()){
		                    		CommonVariables.numberOfClient = 100;
		                    	}
		                    	
		                    	if(jToggleButton1.isSelected()){
		                    		s.getCpuServers().get(0).setQueueType(CommonVariables.LIFOQueue);
		                    		s.getCpuServers().get(1).setQueueType(CommonVariables.LIFOQueue);
		                    		s.getCpuServers().get(2).setQueueType(CommonVariables.LIFOQueue);
		                    	}
		                    	
		                    	if(jToggleButton2.isSelected()){
		                    		s.getCpuServers().get(0).setQueueType(CommonVariables.FIFOQueue);
		                    		s.getCpuServers().get(1).setQueueType(CommonVariables.FIFOQueue);
		                    		s.getCpuServers().get(2).setQueueType(CommonVariables.FIFOQueue);
		                    	}
		                    	
		                    	if(jToggleButton3.isSelected()){
		                    		s.getCpuServers().get(0).setQueueType(CommonVariables.RANDQueue);
		                    		s.getCpuServers().get(1).setQueueType(CommonVariables.RANDQueue);
		                    		s.getCpuServers().get(2).setQueueType(CommonVariables.RANDQueue);
		                    	}
		                    	
		                    	if(jToggleButton4.isSelected()){
		                    		s.getGW1().setQueueType(CommonVariables.LIFOQueue);
		                    		s.getGW2().setQueueType(CommonVariables.LIFOQueue);
		                    	}
		                    	
		                    	if(jToggleButton5.isSelected()){
		                    		s.getGW1().setQueueType(CommonVariables.FIFOQueue);
		                    		s.getGW2().setQueueType(CommonVariables.FIFOQueue);
		                    	}
		                    	
		                    	if(jToggleButton6.isSelected()){
		                    		s.getGW1().setQueueType(CommonVariables.RANDQueue);
		                    		s.getGW2().setQueueType(CommonVariables.RANDQueue);
		                    	}
		                    	
		                    	if(jToggleButton7.isSelected()){
		                    		s.getMs().get(0).setQueueType(CommonVariables.LIFOQueue);
		                    		s.getMs().get(1).setQueueType(CommonVariables.LIFOQueue);
		                    		s.getMs().get(2).setQueueType(CommonVariables.LIFOQueue);
		                    	}
		                    	
		                    	if(jToggleButton8.isSelected()){
		                    		s.getMs().get(0).setQueueType(CommonVariables.FIFOQueue);
		                    		s.getMs().get(1).setQueueType(CommonVariables.FIFOQueue);
		                    		s.getMs().get(2).setQueueType(CommonVariables.FIFOQueue);
		                    	}
		                    	
		                    	if(jToggleButton9.isSelected()){
		                    		s.getMs().get(0).setQueueType(CommonVariables.RANDQueue);
		                    		s.getMs().get(1).setQueueType(CommonVariables.RANDQueue);
		                    		s.getMs().get(2).setQueueType(CommonVariables.RANDQueue);
		                    	}
		                    	
		                    	editWin.setVisible(false);
		                    }
		                });

		                jLabel9.setText("info");
		                jLabel9.setIcon(iconINF);
		                jLabel9.setToolTipText("Change the configuration. View other info for a correct procedure. Press this button for restore default value.");
		                
		                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		                jPanel1.setLayout(jPanel1Layout);
		                jPanel1Layout.setHorizontalGroup(
		                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(jPanel1Layout.createSequentialGroup()
		                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
		                                .addContainerGap()
		                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
		                                    .addComponent(jSeparator5)
		                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
		                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                                            .addComponent(jLabel1)
		                                            .addGroup(jPanel1Layout.createSequentialGroup()
		                                                .addGap(12, 12, 12)
		                                                .addComponent(jLabel6)))
		                                        .addGap(59, 59, 59)
		                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                                            .addGroup(jPanel1Layout.createSequentialGroup()
		                                                .addComponent(jRadioButton1)
		                                                .addGap(18, 18, 18)
		                                                .addComponent(jRadioButton2)
		                                                .addGap(18, 18, 18)
		                                                .addComponent(jRadioButton3)
		                                                .addGap(18, 18, 18)
		                                                .addComponent(jRadioButton4))
		                                            .addGroup(jPanel1Layout.createSequentialGroup()
		                                                .addComponent(jRadioButton5)
		                                                .addGap(18, 18, 18)
		                                                .addComponent(jRadioButton6)
		                                                .addGap(18, 18, 18)
		                                                .addComponent(jRadioButton7)))
		                                        .addGap(0, 0, Short.MAX_VALUE))
		                                    .addComponent(jSeparator4)))
		                            .addGroup(jPanel1Layout.createSequentialGroup()
		                                .addGap(89, 89, 89)
		                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jLabel9)
		                                .addGap(0, 0, Short.MAX_VALUE)))
		                        .addContainerGap())
		                    .addGroup(jPanel1Layout.createSequentialGroup()
		                        .addContainerGap()
		                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
		                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addGroup(jPanel1Layout.createSequentialGroup()
		                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
		                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
		                                        .addGroup(jPanel1Layout.createSequentialGroup()
		                                            .addComponent(jLabel5)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                                            .addComponent(jToggleButton7)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                            .addComponent(jToggleButton8)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                            .addComponent(jToggleButton9))
		                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
		                                            .addComponent(jLabel4)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
		                                            .addComponent(jToggleButton4)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                            .addComponent(jToggleButton5)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                            .addComponent(jToggleButton6)))
		                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                                        .addGroup(jPanel1Layout.createSequentialGroup()
		                                            .addGap(20, 20, 20)
		                                            .addComponent(jLabel7)
		                                            .addGap(127, 127, 127)
		                                            .addComponent(jLabel3)
		                                            .addGap(18, 18, 18)
		                                            .addComponent(jToggleButton1)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                            .addComponent(jToggleButton2)
		                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                            .addComponent(jToggleButton3))
		                                        .addComponent(jLabel2)))
		                                .addGap(6, 6, 6)))
		                        .addGap(0, 0, Short.MAX_VALUE))
		                );
		                jPanel1Layout.setVerticalGroup(
		                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(jPanel1Layout.createSequentialGroup()
		                        .addGap(21, 21, 21)
		                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addGroup(jPanel1Layout.createSequentialGroup()
		                                .addComponent(jLabel1)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jLabel6))
		                            .addGroup(jPanel1Layout.createSequentialGroup()
		                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                                    .addComponent(jRadioButton1)
		                                    .addComponent(jRadioButton2)
		                                    .addComponent(jRadioButton3)
		                                    .addComponent(jRadioButton4))
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                                        .addComponent(jRadioButton6)
		                                        .addComponent(jRadioButton7))
		                                    .addComponent(jRadioButton5))))
		                        .addGap(18, 18, 18)
		                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGap(18, 18, 18)
		                        .addComponent(jLabel2)
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                            .addComponent(jLabel7)
		                            .addComponent(jLabel3)
		                            .addComponent(jToggleButton1)
		                            .addComponent(jToggleButton3)
		                            .addComponent(jToggleButton2))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                            .addComponent(jLabel4)
		                            .addComponent(jToggleButton4)
		                            .addComponent(jToggleButton5)
		                            .addComponent(jToggleButton6))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
		                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                            .addComponent(jLabel5)
		                            .addComponent(jToggleButton7)
		                            .addComponent(jToggleButton8)
		                            .addComponent(jToggleButton9))
		                        .addGap(18, 18, 18)
		                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                        .addGap(18, 18, 18)
		                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
		                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		                            .addComponent(jLabel9))
		                        .addContainerGap(53, Short.MAX_VALUE))
		                );

		                tabbedEditPane.addTab("General Settings", iconSetts, jPanel1);

		                jLabel10.setText("Select your View Output Option:");

		                jLabel8.setText("info");
		                jLabel8.setIcon(iconINF);
		                jLabel8.setToolTipText("Choose what you want to show in console output. Default options are selectable by buttons.");
		                
		                jRadioButton8.setText("Simulator");

		                jRadioButton9.setText("Scheduler");

		                jRadioButton10.setText("Calendar");

		                jRadioButton11.setText("Jobs");

		                jRadioButton12.setText("Queue");

		                jRadioButton13.setText("Stabilization info");

		                jRadioButton14.setText("Gordon output");

		                jRadioButton15.setText("Observation info");

		                jRadioButton16.setText("Observation Returns");

		                jRadioButton17.setText("Throuhput of WAN");

		                jRadioButton18.setText("View Throughput Graph");

		                jRadioButton19.setText("View Stabilization Graphs");

		                jToggleButton10.setText("Default for Stabilization Output");
		                jToggleButton10.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jToggleButton11.setSelected(false);
		                    	jRadioButton8.setSelected(true);
		                    	jRadioButton9.setSelected(false);
		                    	jRadioButton10.setSelected(false);
		                    	jRadioButton11.setSelected(false);
		                    	jRadioButton12.setSelected(false);
		                    	jRadioButton13.setSelected(false);
		                    	jRadioButton14.setSelected(false);
		                    	jRadioButton15.setSelected(false);
		                    	jRadioButton16.setSelected(false);
		                    	jRadioButton17.setSelected(false);
		                    	jRadioButton18.setSelected(false);
		                    	jRadioButton19.setSelected(true);
		                    }
		                });

		                jToggleButton11.setText("Default for Simulation Output");
		                jToggleButton11.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	jToggleButton10.setSelected(false);
		                    	jRadioButton8.setSelected(true);
		                    	jRadioButton9.setSelected(false);
		                    	jRadioButton10.setSelected(false);
		                    	jRadioButton11.setSelected(false);
		                    	jRadioButton12.setSelected(false);
		                    	jRadioButton13.setSelected(false);
		                    	jRadioButton14.setSelected(false);
		                    	jRadioButton15.setSelected(true);
		                    	jRadioButton16.setSelected(false);
		                    	jRadioButton17.setSelected(true);
		                    	jRadioButton18.setSelected(true);
		                    	jRadioButton19.setSelected(false);
		                    }
		                });
		                
		                jButton1.setText("OK");
		                jButton1.addActionListener(new java.awt.event.ActionListener() {
		                    public void actionPerformed(java.awt.event.ActionEvent evt) {
		                    	
		                    	if(jRadioButton8.isSelected()){ CommonVariables.printSim = true; }
		                    	else { CommonVariables.printSim = false; }
		                    	
		                    	if(jRadioButton9.isSelected()){ CommonVariables.printSched = true; }
		                    	else { CommonVariables.printSched = false; }
		                    	
		                    	if(jRadioButton10.isSelected()){ CommonVariables.printSchedCalendar = true; }
		                    	else { CommonVariables.printSchedCalendar = false; }
		                    	
		                    	if(jRadioButton11.isSelected()){ CommonVariables.printSchedJobs = true; }
		                    	else { CommonVariables.printSchedJobs = false; }
		                    	
		                    	if(jRadioButton12.isSelected()){ CommonVariables.printSchedQueue = true; }
		                    	else { CommonVariables.printSchedQueue = false; }
		                    	
		                    	if(jRadioButton13.isSelected()){ CommonVariables.printStabRun = true; }
		                    	else { CommonVariables.printStabRun = false; }
		                    	
		                    	if(jRadioButton14.isSelected()){ CommonVariables.printStabGordon = true; }
		                    	else { CommonVariables.printStabGordon = false; }
		                    	
		                    	if(jRadioButton15.isSelected()){ CommonVariables.printObs = true; }
		                    	else { CommonVariables.printObs = false; }
		                    	
		                    	if(jRadioButton16.isSelected()){ CommonVariables.printObsReturns = true; }
		                    	else { CommonVariables.printObsReturns = false; }
		                    	
		                    	if(jRadioButton17.isSelected()){ CommonVariables.printThroughput = true; }
		                    	else { CommonVariables.printThroughput = false; }
		                    	
		                    	if(jRadioButton18.isSelected()){ CommonVariables.graphThroughput = true; }
		                    	else { CommonVariables.graphThroughput = false; }
		                    	
		                    	if(jRadioButton19.isSelected()){ CommonVariables.graphStabState = true; }
		                    	else { CommonVariables.graphStabState = false; }
		                    	
		                    	editWin.setVisible(false);
		                    }
		                });

		                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		                jPanel2.setLayout(jPanel2Layout);
		                jPanel2Layout.setHorizontalGroup(
		                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(jPanel2Layout.createSequentialGroup()
		                        .addContainerGap()
		                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addGroup(jPanel2Layout.createSequentialGroup()
		                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                                    .addComponent(jLabel10)
		                                    .addComponent(jRadioButton8)
		                                    .addComponent(jRadioButton12)
		                                    .addComponent(jRadioButton13)
		                                    .addComponent(jRadioButton14)
		                                    .addComponent(jRadioButton15)
		                                    .addComponent(jRadioButton16)
		                                    .addComponent(jRadioButton17)
		                                    .addComponent(jRadioButton18))
		                                .addGap(0, 0, Short.MAX_VALUE))
		                            .addGroup(jPanel2Layout.createSequentialGroup()
		                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                                    .addGroup(jPanel2Layout.createSequentialGroup()
		                                        .addComponent(jRadioButton19)
		                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
		                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
		                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                                            .addComponent(jRadioButton10)
		                                            .addComponent(jRadioButton9)
		                                            .addComponent(jRadioButton11))
		                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
		                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
		                                            .addComponent(jToggleButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                                            .addComponent(jLabel8)
		                                            .addComponent(jToggleButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		                                        .addGap(15, 15, 15)))
		                                .addContainerGap())))
		                );
		                jPanel2Layout.setVerticalGroup(
		                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addGroup(jPanel2Layout.createSequentialGroup()
		                        .addContainerGap()
		                        .addComponent(jLabel10)
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addComponent(jRadioButton8)
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addGroup(jPanel2Layout.createSequentialGroup()
		                                .addComponent(jRadioButton9)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jRadioButton10)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jRadioButton11)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jRadioButton12))
		                            .addGroup(jPanel2Layout.createSequentialGroup()
		                                .addComponent(jToggleButton10)
		                                .addGap(12, 12, 12)
		                                .addComponent(jToggleButton11)))
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addComponent(jRadioButton13)
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addComponent(jRadioButton14)
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addComponent(jRadioButton15)
		                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                            .addGroup(jPanel2Layout.createSequentialGroup()
		                                .addComponent(jRadioButton16)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jRadioButton17)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jRadioButton18)
		                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                                .addComponent(jRadioButton19)
		                                .addComponent(jLabel8)
		                                .addGap(0, 0, Short.MAX_VALUE))
		                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
		                                .addGap(0, 0, Short.MAX_VALUE)
		                                .addComponent(jButton1)))
		                        .addContainerGap())
		                );
		                
		                tabbedEditPane.addTab("Console Outputs", iconShow, jPanel2);
		                
		                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		                getContentPane().setLayout(layout);
		                layout.setHorizontalGroup(
		                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addComponent(tabbedEditPane)
		                );
		                layout.setVerticalGroup(
		                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
		                    .addComponent(tabbedEditPane)
		                );

		                setVisible(true);
		                
		        		editWin.setResizable(false);
		        		editWin.setSize(600, 450);
		        		editWin.setLocationRelativeTo(null);
		        		editWin.add(tabbedEditPane);
		        		editWin.setVisible(true);
					}
            	});
            }
        });
        
        JMenuItem eMenuItemStab = new JMenuItem("Stabilization", iconStab);
        eMenuItemStab.setMnemonic(KeyEvent.VK_S);
        eMenuItemStab.setToolTipText("Stabilize");
        
        eMenuItemStab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	 
            	SwingUtilities.invokeLater(new Runnable(){
					public void run() {
						System.out.println("\n");
						startStabilization();
					}
            	});
            	
            	System.out.println("\n\nWAITING ELABORATION...");
            	System.out.println("\nWe are stabilizing the SISTEM with:");
            	System.out.println(CommonVariables.numberOfClient + " CLIENTs and " + CommonVariables.numberOfServer + " SERVERs");
            	System.out.println("for " + CommonVariables.numberOfRun + " RUNs.");
            	System.out.println("\n\nBE PATIENT :)\n\n");
            }
        });

        commands.add(eMenuItemSim);
        commands.add(eMenuItemReset);
        commands.add(eMenuItemView);
        commands.add(eMenuItemExit);
        menubar.add(commands);
        
        tool.add(eMenuItemTest);
        tool.add(eMenuItemEdit);
        tool.add(eMenuItemStab);
        menubar.add(tool);
        
        about.add(eMenuItemAbout);
        menubar.add(about);
        
        setJMenuBar(menubar);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(panel);
	    
	    setSize(1000, 600);
        setLocationRelativeTo(null);
	    
	    this.setVisible(true);
	}
	
	private ArrayList<JFreeChart> testG(){
		return this.simulation.testG();
	}
	
	private void startStabilization(){
		this.simulation.setupNet(CommonVariables.numberOfClient);
		this.simulation.startStabilization(CommonVariables.numberOfClient);
	}
	
	private void startObservation(){		
		this.simulation.startObservation(CommonVariables.numberOfClient);
	}
}
