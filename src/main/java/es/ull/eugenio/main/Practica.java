package es.ull.eugenio.main;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
/**
 * LDH Practica 5
 * Practica.java
 * Purpose: Clase que tokeniza un fichero .txt
 *
 * @author Eugenio José González Luis
 * @version 1.0.0 09/12/2018
 */
public class Practica extends JFrame implements ActionListener {
			
	JPanel panelContenido;
	JButton documentSelector;
	String path;
	private final JFileChooser fc = new JFileChooser();
	public static final Logger logger = Logger.getLogger("log");
	InputStream modelIn;
	
	/** Metodo main, solamente lanza un constructor */
	public static void main( String[] args ) throws Exception
	{
		new Practica();
	}
	
	/**
     * @brief Constructor de la clase, crea la ventana con el boton
     * @throws Exception 
     */
	public Practica() throws Exception {
		modelIn = new FileInputStream( "models/en-token.model" );
		panelContenido = new JPanel();
		GroupLayout layout = new GroupLayout(panelContenido);
		panelContenido.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		documentSelector = new JButton("Abrir Documento");
			
			
		documentSelector.addActionListener(this);
			
			
			
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(documentSelector));
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(documentSelector)));
		this.setContentPane(panelContenido);
		this.setTitle("Tokenizador");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(0, 0);
		pack();
		setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setResizable(true);
		this.setVisible(true);
	}
	
	/**
     * @brief Metodo de actionlistener que detecta cuando se realiza una acción en un elemento de swing, al tocar el boton abre un documento, lo tokeniza y guarda la solucion
     * @param arg0 elemento sobre el que se realizó la acción.
     */
	public void actionPerformed(ActionEvent arg0) {
		TokenizerModel model;
		Tokenizer tokenizer = null;
		try {
			model = new TokenizerModel( modelIn );
			tokenizer = new TokenizerME(model);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int returnVal = fc.showOpenDialog(panelContenido);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				path = file.getAbsolutePath();
				String tokens = "";
				try (
						BufferedReader br = new BufferedReader(new FileReader(file))) {
				    	String line;
				    	while ((line = br.readLine()) != null) {
				    		tokens += " ";
				    		tokens += line;
				    	}
				}
				String tokenList[] = tokenizer.tokenize(tokens);
				PrintWriter writer = new PrintWriter("solucion.txt", "UTF-8");
				for( String token : tokenList )
				{
					writer.println( token );
				}
				writer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Practica.logger.log(Level.WARNING, "No se pudo abrir el documento", e1);
			}
		} else {
			System.out.println("Open command cancelled by user.");
		}
		
	}
}
