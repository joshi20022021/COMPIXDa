package src.analizadores;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Generador {

    public static void main(String[] args) {
        generarCompilador();
    }                           

    public static void generarCompilador() {
        try {
            String ruta = "src" + File.separator + "analizadores" + File.separator;
            String cupFile = ruta + "sintactico.cup";
            String jflexFile = ruta + "lexico.flex";
    
            // Check if the CUP file exists
            if (!Files.exists(Paths.get(cupFile))) {
                throw new RuntimeException("El archivo sintactico.cup no se encuentra en: " + cupFile);
            }
            // Check if the JFlex file exists
            if (!Files.exists(Paths.get(jflexFile))) {
                throw new RuntimeException("El archivo lexico.flex no se encuentra en: " + jflexFile);
            }
    
            long timestamp = System.currentTimeMillis();

            // Generate the parser and sym with CUP
            String[] cupArgs = {"-destdir", ruta, "-parser", "parser", "-symbols", "sym", cupFile};
            java_cup.Main.main(cupArgs);
            System.out.println("Parser y sym generados con éxito.");
    
            // Generate the scanner with JFlex
            String[] jflexArgs = {jflexFile}; // Change this line
            jflex.Main.generate(jflexArgs);
            System.out.println("Scanner generado con éxito.");
    
            List<String> archivosEsperados = Arrays.asList("parser.java", "sym.java", "LexerScanner.java");
            for (String archivo : archivosEsperados) {
                File f = new File(ruta + archivo);
                if (f.exists()) {
                    System.out.println("Archivo generado: " + archivo);
                } else {
                    System.out.println("Advertencia: No se encontró el archivo " + archivo);
                }
            }

            // Check all modified or created files
            File dir = new File(ruta);
            File[] archivos = dir.listFiles();
            if (archivos != null) {
                for (File archivo : archivos) {
                    if (archivo.lastModified() > timestamp) {
                        System.out.println("Archivo generado o modificado: " + archivo.getName());
                    }
                }
            }
    
        } catch (Exception e) {
            System.err.println("Error durante la generación del compilador:");
            e.printStackTrace();
        }
    }
}