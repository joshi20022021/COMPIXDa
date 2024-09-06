package interfaz;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;
import src.analizadores.LexerScanner;
import src.analizadores.parser;
import excepciones.Errores;
import excepciones.Token;
import java.util.List;

public class InterfazGrafica extends JFrame {

    private JTextArea entradaTexto;
    private JTextArea consolaTexto;
    private File archivoActual;

    public InterfazGrafica() {
        // Configuración de la ventana principal
        setTitle("Analizador Léxico y Sintáctico");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu archivoMenu = new JMenu("Archivo");
        JMenuItem nuevoArchivoItem = new JMenuItem("Nuevo Archivo");
        JMenuItem guardarArchivoItem = new JMenuItem("Guardar");
        JMenuItem guardarComoItem = new JMenuItem("Guardar como");
        JMenuItem abrirArchivoItem = new JMenuItem("Abrir archivo");

        // Configurar colores del menú
        archivoMenu.setForeground(Color.WHITE);
        archivoMenu.setBackground(new Color(70, 130, 180));  // Steel Blue
        nuevoArchivoItem.setBackground(new Color(176, 224, 230));  // Light Blue
        guardarArchivoItem.setBackground(new Color(176, 224, 230));
        guardarComoItem.setBackground(new Color(176, 224, 230));
        abrirArchivoItem.setBackground(new Color(176, 224, 230));

        // Agregar acciones de los menús
        nuevoArchivoItem.addActionListener(e -> nuevoArchivo());
        guardarArchivoItem.addActionListener(e -> guardarArchivo());
        guardarComoItem.addActionListener(e -> guardarComo());
        abrirArchivoItem.addActionListener(e -> abrirArchivo());

        archivoMenu.add(nuevoArchivoItem);
        archivoMenu.add(guardarArchivoItem);
        archivoMenu.add(guardarComoItem);
        archivoMenu.add(abrirArchivoItem);

        JMenu ejecutarMenu = new JMenu("Ejecutar");
        JMenuItem ejecutarItem = new JMenuItem("Ejecutar Análisis");
        ejecutarItem.addActionListener(e -> ejecutarAnalisis());
        ejecutarMenu.setForeground(Color.WHITE);
        ejecutarMenu.setBackground(new Color(70, 130, 180));
        ejecutarItem.setBackground(new Color(176, 224, 230));
        ejecutarMenu.add(ejecutarItem);

        JMenu reportesMenu = new JMenu("Reportes");
        JMenuItem reporteTokensItem = new JMenuItem("Reporte de Tokens");
        JMenuItem reporteErroresItem = new JMenuItem("Reporte de Errores");

        reporteTokensItem.addActionListener(e -> abrirReporte("tokens.html"));
        reporteErroresItem.addActionListener(e -> abrirReporte("errores.html"));

        reportesMenu.setForeground(Color.WHITE);
        reportesMenu.setBackground(new Color(70, 130, 180));
        reporteTokensItem.setBackground(new Color(176, 224, 230));
        reporteErroresItem.setBackground(new Color(176, 224, 230));

        reportesMenu.add(reporteTokensItem);
        reportesMenu.add(reporteErroresItem);

        menuBar.add(archivoMenu);
        menuBar.add(ejecutarMenu);
        menuBar.add(reportesMenu);
        menuBar.setBackground(new Color(70, 130, 180));  // Color de fondo del menú

        // Asignar la barra de menú a la ventana
        setJMenuBar(menuBar);

        // Panel para la entrada y consola (parte izquierda)
        JPanel panelIzquierdo = new JPanel(new GridLayout(2, 1));
        panelIzquierdo.setBackground(new Color(240, 248, 255));  // Alice Blue

        // Área de texto para la entrada
        entradaTexto = new JTextArea();
        entradaTexto.setLineWrap(true);
        entradaTexto.setBackground(new Color(255, 250, 240));  // Floral White
        entradaTexto.setForeground(Color.BLACK);
        entradaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane entradaScroll = new JScrollPane(entradaTexto);
        TitledBorder entradaTitle = BorderFactory.createTitledBorder("Entrada");
        entradaTitle.setTitleColor(new Color(0, 102, 204));  // Dark Blue
        entradaScroll.setBorder(entradaTitle);
        panelIzquierdo.add(entradaScroll);

        // Área de texto para la consola
        consolaTexto = new JTextArea();
        consolaTexto.setLineWrap(true);
        consolaTexto.setBackground(new Color(255, 250, 240));  // Floral White
        consolaTexto.setForeground(Color.BLACK);
        consolaTexto.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane consolaScroll = new JScrollPane(consolaTexto);
        TitledBorder consolaTitle = BorderFactory.createTitledBorder("Consola");
        consolaTitle.setTitleColor(new Color(0, 102, 204));  // Dark Blue
        consolaScroll.setBorder(consolaTitle);
        panelIzquierdo.add(consolaScroll);

        // Agregar el panel izquierdo a la ventana
        add(panelIzquierdo, BorderLayout.CENTER);

        // Panel para las gráficas (parte derecha)
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BorderLayout());
        panelDerecho.setBackground(new Color(240, 248, 255));  // Alice Blue

        JPanel graficaPanel = new JPanel();
        graficaPanel.setPreferredSize(new Dimension(200, 150));
        graficaPanel.setBorder(BorderFactory.createTitledBorder("Gráficas"));
        graficaPanel.setBackground(new Color(224, 255, 255));  // Light Cyan
        panelDerecho.add(graficaPanel, BorderLayout.CENTER);

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

            // Obtener tokens y errores
            LinkedList<Errores> erroresLexicos = lexer.getErroresLexicos();
            LinkedList<Errores> erroresSintacticos = parser.getErroresSintacticos();
            List<Token> tokens = lexer.getTokens();  // Usa List en lugar de LinkedList

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

            // Generar los reportes en HTML
            generarHTMLTokens(tokens);
            generarHTMLErrores(erroresLexicos, erroresSintacticos);

        } catch (Exception ex) {
            consolaTexto.append("Error durante el análisis: " + ex.getMessage() + "\n");
        }
    }

    private void nuevoArchivo() {
        entradaTexto.setText("");
        archivoActual = null;
    }

    private void guardarArchivo() {
        if (archivoActual != null) {
            escribirArchivo(archivoActual);
        } else {
            guardarComo();
        }
    }

    private void guardarComo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como...");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().endsWith(".ca")) {
                archivo = new File(archivo.getAbsolutePath() + ".ca");
            }
            archivoActual = archivo;
            escribirArchivo(archivo);
        }
    }

    private void abrirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir archivo...");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            leerArchivo(archivoActual);
        }
    }

    private void escribirArchivo(File archivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write(entradaTexto.getText());
            consolaTexto.append("Archivo guardado: " + archivo.getName() + "\n");
        } catch (IOException ex) {
            consolaTexto.append("Error al guardar el archivo: " + ex.getMessage() + "\n");
        }
    }

    private void leerArchivo(File archivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            entradaTexto.setText("");
            String linea;
            while ((linea = reader.readLine()) != null) {
                entradaTexto.append(linea + "\n");
            }
            consolaTexto.append("Archivo abierto: " + archivo.getName() + "\n");
        } catch (IOException ex) {
            consolaTexto.append("Error al abrir el archivo: " + ex.getMessage() + "\n");
        }
    }

    private void abrirReporte(String archivoReporte) {
        try {
            Desktop.getDesktop().browse(new File(archivoReporte).toURI());
        } catch (IOException e) {
            consolaTexto.append("Error al abrir el reporte: " + e.getMessage() + "\n");
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
                .replace(" ", "&nbsp;")
                .replace("\n", "<br>");
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

        } catch (IOException e) {
            consolaTexto.append("Error al generar el reporte de tokens: " + e.getMessage() + "\n");
        }
    }

    // Método para generar el archivo HTML de los errores
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

        } catch (IOException e) {
            consolaTexto.append("Error al generar el reporte de errores: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazGrafica());
    }
}
