package inicialization;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class Inicialization extends JFrame{
	private static final long serialVersionUID = 1L;
	public String pathJavafx;
    CardLayout cardLayout;
    JPanel painelPrincipal;
    JTextField txtfArquivo;
	
	// Inicia a aplicação
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicialization frame = new Inicialization();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Cria a interface
	public Inicialization() {
		setTitle("DeepCode");
        setSize(460, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        painelPrincipal = new JPanel(cardLayout);
        
        
        // Cria as janelas
        JPanel tela1 = criarTela1();
        JPanel tela2 = criarTela2();
        JPanel tela3 = criarTela3();
        JPanel tela4 = criarTela4();
        JPanel tela5 = criarTela5();
        
        painelPrincipal.add(tela1, "Principal");
        painelPrincipal.add(tela2, "Requisitos");
        painelPrincipal.add(tela3, "JavaFX");
        painelPrincipal.add(tela4, "Ollama");
        painelPrincipal.add(tela5, "Créditos");
        
        add(painelPrincipal);
        setVisible(true);
	}
	
	// Gradiente da tela de fundo
	public class PainelGradiente extends JPanel {
	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2d = (Graphics2D) g;

	        Color cor1 = new Color(0, 0, 0);
	        Color cor2 = new Color(106, 13, 173);
	        GradientPaint gp = new GradientPaint(0, 60, cor1, 0, getHeight(), cor2);

	        g2d.setPaint(gp);
	        g2d.fillRect(0, 0, getWidth(), getHeight());
	    }
	}
	
    private JPanel criarTela1() {
        JPanel panel = new PainelGradiente();
        panel.setLayout(null);
        
		JLabel lblWelcome = new JLabel("Bem-vindo(a) ao");
		lblWelcome.setForeground(new Color(255, 255, 255));
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblWelcome.setBounds(10, 11, 250, 31);
		panel.add(lblWelcome);
		
		JLabel lblTitulo1 = new JLabel("Projeto");
		lblTitulo1.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblTitulo1.setForeground(new Color(186, 81, 249));
		lblTitulo1.setBounds(30, 89, 164, 31);
		panel.add(lblTitulo1);
		
		JLabel lblTitulo2 = new JLabel("DeepCode");
		lblTitulo2.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblTitulo2.setForeground(new Color(186, 81, 249));
		lblTitulo2.setBounds(29, 127, 175, 31);
		panel.add(lblTitulo2);
		
		// Cria a imagem da Logo
		JLabel imgLogo = new JLabel("");
		imgLogo.setIcon(new ImageIcon(Inicialization.class.getResource("/img/IconDeepCode_Roxa.png")));
		imgLogo.setBounds(196, 23, 175, 175);
		panel.add(imgLogo);

		Image img = ((ImageIcon) imgLogo.getIcon()).getImage();
		Image imgRedimensionada = img.getScaledInstance(
		    imgLogo.getWidth(),
		    imgLogo.getHeight(),
		    Image.SCALE_SMOOTH
		);
		imgLogo.setIcon(new ImageIcon(imgRedimensionada));
		
		JButton btnIniciar = new JButton("Iniciar");
		btnIniciar.setForeground(new Color(255, 255, 255));
		btnIniciar.setBackground(new Color(166, 74, 255));
		btnIniciar.setBounds(292, 209, 120, 41);
		panel.add(btnIniciar);
		
		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(iniciarPrograma()) {
					dispose();
				}
			}
		});
		
		JButton btnConfig = new JButton("Configurações");
		btnConfig.setForeground(new Color(255, 255, 255));
		btnConfig.setBackground(new Color(166, 74, 255));
		btnConfig.setBounds(160, 209, 120, 41);
		panel.add(btnConfig);
		
		btnConfig.addActionListener(e -> cardLayout.show(painelPrincipal, "Requisitos"));
		
		JButton btnCreditos = new JButton("Créditos");
		btnCreditos.setForeground(new Color(255, 255, 255));
		btnCreditos.setBackground(new Color(166, 74, 255));
		btnCreditos.setBounds(28, 209, 120, 41);
		panel.add(btnCreditos);
		
		btnCreditos.addActionListener(e -> cardLayout.show(painelPrincipal, "Créditos"));

        return panel;
    }

    private JPanel criarTela2() {
        JPanel panel = new PainelGradiente();
        panel.setLayout(null);
        
		JLabel lblRequisitos = new JLabel("Requisitos do Projeto");
		lblRequisitos.setForeground(new Color(255, 255, 255));
		lblRequisitos.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblRequisitos.setBounds(10, 11, 200, 22);
		panel.add(lblRequisitos);

		JLabel lblJavafx = new JLabel("- JavaFX (Interface gráfica)");
		lblJavafx.setForeground(new Color(255, 255, 255));
		lblJavafx.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblJavafx.setBounds(28, 80, 175, 22);
		panel.add(lblJavafx);
		
		JLabel lblOllama = new JLabel("- Ollama (Inteligência artificial)");
		lblOllama.setForeground(new Color(255, 255, 255));
		lblOllama.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOllama.setBounds(28, 130, 206, 22);
		panel.add(lblOllama);

		// Cria os Botões de Navegação
		panel.add(criarBtnHome());
		panel.add(criarBtnAvancar("JavaFX"));
		
        return panel;
    }
    
    private JPanel criarTela3() {
    	JPanel panel = new PainelGradiente();
    	panel.setLayout(null);
    	
		txtfArquivo = new JTextField();
		txtfArquivo.setEnabled(false);
		txtfArquivo.setBounds(40, 165, 249, 23);
		txtfArquivo.setColumns(10);
		panel.add(txtfArquivo);

		JButton btnArquivo = new JButton("");
		btnArquivo.setBackground(new Color(255, 255, 255));
		btnArquivo.setBounds(299, 162, 30, 30);
		btnArquivo.setIcon(new ImageIcon(Inicialization.class.getResource("/img/folder.png")));
		btnArquivo.setBorder(null);
		panel.add(btnArquivo);

		Image img = ((ImageIcon) btnArquivo.getIcon()).getImage();
		Image imgRedimensionada = img.getScaledInstance(
		    btnArquivo.getWidth() - 5,
		    btnArquivo.getHeight() - 5,
		    Image.SCALE_SMOOTH
		);
		btnArquivo.setIcon(new ImageIcon(imgRedimensionada));
		
		btnArquivo.addActionListener(e -> abrirPasta());
		
		JLabel lblJavafx = new JLabel("Instale o JavaFX");
		lblJavafx.setForeground(new Color(255, 255, 255));
		lblJavafx.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblJavafx.setBounds(10, 11, 189, 20);
		panel.add(lblJavafx);
		
		JLabel lblFXpath = new JLabel("Indique o caminho da pasta \".../java-sdk-24/lib\"");
		lblFXpath.setForeground(new Color(255, 255, 255));
		lblFXpath.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFXpath.setBounds(40, 115, 370, 20);
		panel.add(lblFXpath);
		
		JLabel lblInstallFx = new JLabel("Instale o JavaFX SDK 24:");
		lblInstallFx.setForeground(new Color(255, 255, 255));
		lblInstallFx.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInstallFx.setBounds(40, 68, 204, 20);
		panel.add(lblInstallFx);
		
		JLabel link = new JLabel("<html><a href=''>Download JavaFX</a></html>");
		link.setFont(new Font("Tahoma", Font.PLAIN, 14));
        link.setBounds(205, 63, 200, 30);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(link);
        
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirLinkNoNavegador("https://gluonhq.com/products/javafx/");
            }
        });
        
		// Cria os Botões de Navegação
		panel.add(criarBtnHome());
		panel.add(criarBtnVoltar("Requisitos"));
		panel.add(criarBtnAvancar("Ollama"));
		
		return panel;
    }
	
	public JPanel criarTela4() {
		JPanel panel = new PainelGradiente();
		panel.setLayout(null);
		
		JLabel lblOllama = new JLabel("Instale o Ollama");
		lblOllama.setForeground(new Color(255, 255, 255));
		lblOllama.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblOllama.setBounds(10, 11, 189, 20);
		panel.add(lblOllama);
		
		JLabel lblInstallOllama = new JLabel("Instale Ollama por aqui:");
		lblInstallOllama.setForeground(new Color(255, 255, 255));
		lblInstallOllama.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInstallOllama.setBounds(40, 55, 150, 20);
		panel.add(lblInstallOllama);
		
		JLabel link = new JLabel("<html><a href=''>Download Ollama</a></html>");
		link.setFont(new Font("Tahoma", Font.PLAIN, 14));
        link.setBounds(200, 55, 200, 20);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(link);
        
        link.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirLinkNoNavegador("https://ollama.com/download");
            }
        });
        
		JLabel lblInstrucao = new JLabel("Execute esses códigos no cmd e substitua o necessário:");
		lblInstrucao.setForeground(new Color(255, 255, 255));
		lblInstrucao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInstrucao.setBounds(40, 86, 350, 20);
		panel.add(lblInstrucao);
		
		JTextField txtfCode1 = new JTextField("ollama pull qwen2.5-coder:3b");
		txtfCode1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtfCode1.setBounds(40, 120, 200, 23);
		txtfCode1.setEditable(false);
		txtfCode1.setForeground(Color.BLACK);
		panel.add(txtfCode1);
		
		JButton btnCopiar1 = new JButton("");
		btnCopiar1.setBackground(new Color(255, 255, 255));
		btnCopiar1.setBounds(250, 117, 30, 30);
		btnCopiar1.setIcon(new ImageIcon(Inicialization.class.getResource("/img/copy.png")));
		btnCopiar1.setBorder(null);
		panel.add(btnCopiar1);

		Image img = ((ImageIcon) btnCopiar1.getIcon()).getImage();
		Image imgRedimensionada = img.getScaledInstance(
		    btnCopiar1.getWidth() - 5,
		    btnCopiar1.getHeight() - 5,
		    Image.SCALE_SMOOTH
		);
		btnCopiar1.setIcon(new ImageIcon(imgRedimensionada));

        // Ação do botão
        btnCopiar1.addActionListener((ActionEvent e) -> {
            String texto = txtfCode1.getText();
            Toolkit.getDefaultToolkit()
                   .getSystemClipboard()
                   .setContents(new StringSelection(texto), null);
        });
        
		JLabel lblMemory = new JLabel("(Requer 1.9GB)");
		lblMemory.setForeground(new Color(255, 255, 255));
		lblMemory.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMemory.setBounds(292, 122, 100, 17);
		panel.add(lblMemory);
        
		JTextField txtfCode2 = new JTextField("ollama create meu_qwen -f ...\\API2\\Interface\\Modelfile");
		txtfCode2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtfCode2.setBounds(40, 160, 300, 23);
		txtfCode2.setEditable(false);
		txtfCode2.setForeground(Color.BLACK);
		panel.add(txtfCode2);
		
		JButton btnCopiar2 = new JButton("");
		btnCopiar2.setBackground(new Color(255, 255, 255));
		btnCopiar2.setBounds(350, 156, 30, 30);
		btnCopiar2.setIcon(new ImageIcon(Inicialization.class.getResource("/img/copy.png")));
		btnCopiar2.setBorder(null);
		panel.add(btnCopiar2);
		
		Image img2 = ((ImageIcon) btnCopiar2.getIcon()).getImage();
		Image imgRedimensionada2 = img2.getScaledInstance(
		    btnCopiar2.getWidth() - 5,
		    btnCopiar2.getHeight() - 5,
		    Image.SCALE_SMOOTH
		);
		btnCopiar2.setIcon(new ImageIcon(imgRedimensionada2));
		
		btnCopiar2.addActionListener((ActionEvent e) -> {
            String texto = txtfCode2.getText();
            Toolkit.getDefaultToolkit()
                   .getSystemClipboard()
                   .setContents(new StringSelection(texto), null);
        });
		
		// Cria os Botões de Navegação
		panel.add(criarBtnHome());
		panel.add(criarBtnVoltar("JavaFX"));
		
		return panel;
	}
	
	public JPanel criarTela5() {
		JPanel panel = new PainelGradiente();
		panel.setLayout(null);
		
		JLabel lblDevs = new JLabel("Desenvolvedores do Projeto :)");
		lblDevs.setForeground(new Color(255, 255, 255));
		lblDevs.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblDevs.setBounds(10, 11, 281, 20);
		panel.add(lblDevs);
		
		JLabel lblFabio = new JLabel("Fábio Hiromitsu Nawa");
		lblFabio.setForeground(new Color(255, 255, 255));
		lblFabio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFabio.setBounds(28, 90, 175, 20);
		panel.add(lblFabio);
		
		JLabel lblCelso = new JLabel("Celso Moreira Freitas");
		lblCelso.setForeground(new Color(255, 255, 255));
		lblCelso.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCelso.setBounds(28, 60, 143, 20);
		panel.add(lblCelso);
		
		JLabel lblLuiz = new JLabel("Luiz Roberto Briz Quirino");
		lblLuiz.setForeground(new Color(255, 255, 255));
		lblLuiz.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLuiz.setBounds(210, 90, 166, 20);
		panel.add(lblLuiz);
		
		JLabel lblGustavo = new JLabel("Gustavo Felipe Morais");
		lblGustavo.setForeground(new Color(255, 255, 255));
		lblGustavo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblGustavo.setBounds(28, 120, 175, 20);
		panel.add(lblGustavo);
		
		JLabel lblPedro = new JLabel("Pedro Henrique de Paula Soares");
		lblPedro.setForeground(new Color(255, 255, 255));
		lblPedro.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPedro.setBounds(210, 150, 204, 20);
		panel.add(lblPedro);
		
		JLabel lblNicolas = new JLabel("Nícolas Ferreira Fernandes");
		lblNicolas.setForeground(new Color(255, 255, 255));
		lblNicolas.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNicolas.setBounds(210, 120, 204, 20);
		panel.add(lblNicolas);
		
		JLabel lblJosue = new JLabel("Josué da Cunha Lopes");
		lblJosue.setForeground(new Color(255, 255, 255));
		lblJosue.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblJosue.setBounds(210, 60, 181, 20);
		panel.add(lblJosue);
		
		JLabel lblJoao = new JLabel("João Pedro Barni Lima");
		lblJoao.setForeground(new Color(255, 255, 255));
		lblJoao.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblJoao.setBounds(28, 150, 175, 20);
		panel.add(lblJoao);
		
		// Cria os Botões de Navegação
		panel.add(criarBtnHome());
		
		return panel;
	}
	
	// Função para iniciar o programa principal
	public boolean iniciarPrograma() {
		if(pathJavafx == null) {
			JOptionPane.showMessageDialog(null, "Clique no botão 'Configurações' antes de iniciar", "Configure o caminho JavaFX", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else {
			try {
	            ProcessBuilder pb = new ProcessBuilder(
	                	"java",
	                	"--module-path", pathJavafx,
	                	"--add-modules", "javafx.controls",
	                	"-jar",
	                	"MainApplication.jar"
	            );
	            pb.start();
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public JButton criarBtnHome() {
		ImageIcon imgHome = new ImageIcon(getClass().getResource("/img/home.png"));

		JButton btnHome = new JButton("Home", imgHome);
		btnHome.setForeground(Color.WHITE);
		btnHome.setBackground(new Color(166, 74, 255));
		btnHome.setBounds(28, 209, 120, 41);

		// Posicionamento do texto em relação ao ícone
		btnHome.setHorizontalTextPosition(SwingConstants.RIGHT); // texto à direita do ícone
		btnHome.setVerticalTextPosition(SwingConstants.CENTER);  // texto centralizado verticalmente
		
		// Voltar para a tela inicial
		btnHome.addActionListener(e -> cardLayout.show(painelPrincipal, "Principal"));
		
		return btnHome;
	}
	
	public JButton criarBtnAvancar(String destino) {
		ImageIcon imgAvancar = new ImageIcon(getClass().getResource("/img/right_arrow.png"));
		
		JButton btnAvancar = new JButton("Avançar", imgAvancar);
		btnAvancar.setForeground(new Color(255, 255, 255));
		btnAvancar.setBackground(new Color(166, 74, 255));
		btnAvancar.setBounds(292, 209, 122, 41);
		
		btnAvancar.setHorizontalTextPosition(SwingConstants.LEFT);
		btnAvancar.setVerticalTextPosition(SwingConstants.CENTER);
		
		btnAvancar.addActionListener(e -> cardLayout.show(painelPrincipal, destino));
		
		return btnAvancar;
	}
	
	public JButton criarBtnVoltar(String destino) {
		ImageIcon imgVoltar = new ImageIcon(getClass().getResource("/img/left_arrow.png"));
		
		JButton btnVoltar = new JButton("Voltar", imgVoltar);
		btnVoltar.setForeground(new Color(255, 255, 255));
		btnVoltar.setBackground(new Color(166, 74, 255));
		btnVoltar.setBounds(160, 209, 120, 41);
		
		btnVoltar.setHorizontalTextPosition(SwingConstants.RIGHT);
		btnVoltar.setVerticalTextPosition(SwingConstants.CENTER);
		
		btnVoltar.addActionListener(e -> cardLayout.show(painelPrincipal, destino));
		
		return btnVoltar;
	}
	
	// Funções da tela JavaFX
	private void abrirPasta() {
		JFileChooser seletor = new JFileChooser();
		
		seletor.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		seletor.setDialogTitle("Caminho para a pasta .../javafx-sdk-24/lib");
		
		int resultado = seletor.showOpenDialog(this);
		
		if (resultado == JFileChooser.APPROVE_OPTION) {
			File caminhoFx = seletor.getSelectedFile();
			this.pathJavafx = caminhoFx.getAbsolutePath();
			txtfArquivo.setText(caminhoFx.getAbsolutePath());
		}
	}
	
	private void abrirLinkNoNavegador(String url) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url)); // abre no navegador padrão
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Não foi possível abrir o link: " + e.getMessage());
        }
    }
}