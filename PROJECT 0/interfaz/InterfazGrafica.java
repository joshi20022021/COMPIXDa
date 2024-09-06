package interfaz;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URI;
import java.util.LinkedList;
import src.analizadores.LexerScanner;  // Importa el lexer
import src.analizadores.parser;        // Importa el parser
import excepciones.Errores;            // Importa la clase Errores (en la carpeta excepciones)
import excepciones.Token;
import java.util.List;

public class InterfazGrafica extends JFrame {

    private JTextArea entradaTexto;
    private JTextArea consolaTexto;

    public InterfazGrafica() {
        // Configuración de la ventana principal
        setTitle("Window Title");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu archivoMenu = new JMenu("Archivo");
        JMenu ejecutarMenu = new JMenu("Ejecutar");
        JMenu reportesMenu = new JMenu("Reportes");

        menuBar.add(archivoMenu);
        menuBar.add(ejecutarMenu);
        menuBar.add(reportesMenu);

        // Asignar la barra de menú a la ventana
        setJMenuBar(menuBar);

        // Panel para la entrada y consola (parte izquierda)
        JPanel panelIzquierdo = new JPanel(new GridLayout(2, 1));

        // Área de texto para la entrada
        entradaTexto = new JTextArea();
        entradaTexto.setLineWrap(true);
        JScrollPane entradaScroll = new JScrollPane(entradaTexto);
        TitledBorder entradaTitle = BorderFactory.createTitledBorder("Entrada");
        entradaScroll.setBorder(entradaTitle);
        panelIzquierdo.add(entradaScroll);

        // Área de texto para la consola
        consolaTexto = new JTextArea();
        consolaTexto.setLineWrap(true);
        JScrollPane consolaScroll = new JScrollPane(consolaTexto);
        TitledBorder consolaTitle = BorderFactory.createTitledBorder("Consola");
        consolaScroll.setBorder(consolaTitle);
        panelIzquierdo.add(consolaScroll);

        // Agregar el panel izquierdo a la ventana
        add(panelIzquierdo, BorderLayout.CENTER);

        // Panel para las gráficas y botones (parte derecha)
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BorderLayout());

        // Panel de la gráfica (simulada por ahora)
        JPanel graficaPanel = new JPanel();
        graficaPanel.setPreferredSize(new Dimension(200, 150));
        graficaPanel.setBorder(BorderFactory.createTitledBorder("Gráficas"));
        graficaPanel.setBackground(Color.LIGHT_GRAY);
        panelDerecho.add(graficaPanel, BorderLayout.CENTER);

        // Panel para los botones
        JPanel botonesPanel = new JPanel(new FlowLayout());
        JButton btnAnterior = new JButton("Anterior");
        JButton btnSiguiente = new JButton("Siguiente");
        botonesPanel.add(btnAnterior);
        botonesPanel.add(btnSiguiente);

        // Botón para ejecutar el análisis
        JButton btnEjecutar = new JButton("Ejecutar");
        btnEjecutar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarAnalisis();
            }
        });
        botonesPanel.add(btnEjecutar);

        panelDerecho.add(botonesPanel, BorderLayout.SOUTH);

        // Agregar el panel derecho a la ventana
        add(panelDerecho, BorderLayout.EAST);

        // Hacer visible la ventana
        setVisible(true);
    }

// Método para ejecutar los analizadores
private void ejecutarAnalisis() {
    String input = entradaTexto.getText();  // Obtener el texto del área de entrada
    consolaTexto.setText("");  // Limpiar el área de consola

    try {
        // Crear un lexer con el texto de entrada
        LexerScanner lexer = new LexerScanner(new StringReader(input));
        parser parser = new parser(lexer);  // Crear un parser con el lexer

        // Ejecutar el parser
        parser.parse();

        // Obtener y mostrar la lista de errores léxicos
        LinkedList<Errores> erroresLexicos = lexer.getErroresLexicos();
        System.out.println("Errores Léxicos encontrados: " + erroresLexicos.size());
        for (Errores error : erroresLexicos) {
            System.out.println("Error Léxico: " + error.getDescripcion());
        }

        // Obtener y mostrar la lista de errores sintácticos
        LinkedList<Errores> erroresSintacticos = parser.getErroresSintacticos();
        System.out.println("Errores Sintácticos encontrados: " + erroresSintacticos.size());
        for (Errores error : erroresSintacticos) {
            System.out.println("Error Sintáctico: " + error.getDescripcion());
        }

        // Mostrar mensajes en la consola (si hay errores léxicos o sintácticos)
        if (erroresLexicos.isEmpty() && erroresSintacticos.isEmpty()) {
            consolaTexto.append("Análisis completado sin errores.\n");
        } else {
            for (Errores error : erroresLexicos) {
                consolaTexto.append("Error Léxico: " + error.getDescripcion() + "\n");
            }
            for (Errores error : erroresSintacticos) {
                consolaTexto.append("Error Sintáctico: " + error.getDescripcion() + "\n");
            }
        }
    } catch (Exception ex) {
        consolaTexto.append("Error durante el análisis: " + ex.getMessage() + "\n");
    }
}


// Método para escapar caracteres especiales de HTML
private String escapeHTML(String text) {
    if (text == null) {
        return "";
    }
    return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
            .replace(" ", "&nbsp;")  // Agrega espacio no rompible para mostrar correctamente espacios
            .replace("\n", "<br>");  // Maneja saltos de línea
}


// Método para generar el archivo HTML de los tokens
private void generarHTMLTokens(List<Token> tokens) {
    try {
        FileWriter writer = new FileWriter("tokens.html");
        writer.write("<html><head><title>Reporte de Tokens</title>");
        writer.write("<style>table { border-collapse: collapse; width: 100%; }");
        writer.write("th, td { border: 1px solid black; padding: 8px; text-align: center; }");
        writer.write("th { background-color: #A9C1FF; }</style></head><body>");
        writer.write("<h1>Reporte de Tokens</h1>");
        writer.write("<table>");
        writer.write("<tr><th>#</th><th>Lexema</th><th>Tipo</th><th>Línea</th><th>Columna</th></tr>");

        int count = 1;
        for (Token token : tokens) {
            writer.write("<tr><td>" + count++ + "</td><td>" + escapeHTML(token.getLexema()) + "</td><td>" + escapeHTML(token.getTipo()) + "</td><td>" + token.getLinea() + "</td><td>" + token.getColumna() + "</td></tr>");
        }

        writer.write("</table></body></html>");
        writer.close();

        // Abrir el archivo HTML automáticamente
        Desktop.getDesktop().browse(new File("tokens.html").toURI());

    } catch (IOException e) {
        consolaTexto.append("Error al generar el reporte de tokens: " + e.getMessage() + "\n");
    }
}
    
    
    private void generarHTMLErrores(LinkedList<Errores> erroresLexicos, LinkedList<Errores> erroresSintacticos) {
        try {
            FileWriter writer = new FileWriter("errores.html");
            writer.write("<html><head><title>Reporte de Errores</title>");
            writer.write("<style>table { border-collapse: collapse; width: 100%; }");
            writer.write("th, td { border: 1px solid black; padding: 8px; text-align: center; }");
            writer.write("th { background-color: #A9C1FF; }</style></head><body>");
            writer.write("<h1>Reporte de Errores</h1>");
    
            writer.write("<table>");
            writer.write("<tr><th>#</th><th>Tipo</th><th>Descripción</th><th>Línea</th><th>Columna</th></tr>");
            
            int count = 1;
    
            if (!erroresLexicos.isEmpty()) {
                for (Errores error : erroresLexicos) {
                    writer.write("<tr><td>" + count++ + "</td><td>Léxico</td><td>" + error.getDescripcion() + "</td><td>" + error.getLinea() + "</td><td>" + error.getColumna() + "</td></tr>");
                }
            }
    
            if (!erroresSintacticos.isEmpty()) {
                for (Errores error : erroresSintacticos) {
                    writer.write("<tr><td>" + count++ + "</td><td>Sintáctico</td><td>" + error.getDescripcion() + "</td><td>" + error.getLinea() + "</td><td>" + error.getColumna() + "</td></tr>");
                }
            }
    
            writer.write("</table></body></html>");
            writer.close();
    
            // Abrir el archivo HTML automáticamente
            Desktop.getDesktop().browse(new File("errores.html").toURI());
    
        } catch (IOException e) {
            consolaTexto.append("Error al generar el reporte de errores: " + e.getMessage() + "\n");
        }
    }
    

    public static void main(String[] args) {
        // Ejecutar la interfaz en el hilo principal de Swing
        SwingUtilities.invokeLater(() -> new InterfazGrafica());
    }
}
