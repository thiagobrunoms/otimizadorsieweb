import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;


public class MainView2 {
		private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;
	   private DefaultListModel fileList;

	   public MainView2(){
	      prepareGUI();
	   }

	   public static void main(String[] args){
		  MainView2  swingControlDemo = new MainView2();      
	      swingControlDemo.showFileChooserDemo();
	   }

	   private void prepareGUI(){
	      mainFrame = new JFrame("Java Swing Examples");
	      mainFrame.setSize(400,400);
	      mainFrame.setLayout(new GridLayout(3, 1));
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
	      headerLabel = new JLabel("", JLabel.CENTER);        
	      statusLabel = new JLabel("",JLabel.CENTER);
	      
	      fileList = new DefaultListModel();
	      JList fruitList = new JList(fileList);
	      fruitList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	      fruitList.setSelectedIndex(0);
	      fruitList.setVisibleRowCount(3);

	      statusLabel.setSize(350,100);

	      controlPanel = new JPanel();
	      controlPanel.setLayout(new FlowLayout());

	      mainFrame.add(headerLabel);
	      mainFrame.add(controlPanel);
	      mainFrame.add(statusLabel);
	      mainFrame.add(fruitList);
	      
	      mainFrame.setVisible(true);  
	   }

	   private void showFileChooserDemo() {
	      headerLabel.setText("Control in action: JFileChooser"); 

	      final JFileChooser  fileDialog = new JFileChooser();
	      fileDialog.setMultiSelectionEnabled(true);
	      //TODO permitir apenas arquivos .xslx ou xls
	      
	      JButton showFileDialogButton = new JButton("Abrir arquivos (.xlsx)");
	      showFileDialogButton.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            int returnVal = fileDialog.showOpenDialog(mainFrame);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	               java.io.File[] files = fileDialog.getSelectedFiles();
	               for (File eachFile : files) {
	            	   fileList.addElement(eachFile.getName());
	               }
	            }
	            else{
	               statusLabel.setText("Open command cancelled by user." );           
	            }      
	         }
	      });
	      controlPanel.add(showFileDialogButton);
	      mainFrame.setVisible(true);  
	   }
}
