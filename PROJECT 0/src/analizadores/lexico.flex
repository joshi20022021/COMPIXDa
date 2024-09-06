package src.analizadores;

import java_cup.runtime.Symbol;
import java.util.LinkedList;
import excepciones.Errores;
import excepciones.Token;
import java.util.List;
import java.util.ArrayList;

%%
%public
%class LexerScanner
%unicode
%cup
%line
%column
%type java_cup.runtime.Symbol

%{
    private LinkedList<Errores> listaErroresLexicos = new LinkedList<>();
    private List<Token> tokens = new ArrayList<>();

    public LinkedList<Errores> getErroresLexicos() {
        return listaErroresLexicos;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    private Symbol symbol(int type, Object value, String description) {
        tokens.add(new Token(value.toString(), description, yyline + 1, yycolumn + 1));  // Agregar los tokens a la lista
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }


    private Symbol symbol(int type) {
        return symbol(type, yytext(), "DESCONOCIDO");
    }

    private void reportErrorLexico(String lexema) {
        listaErroresLexicos.add(new Errores("Error Léxico", 
            "Carácter o palabra no reconocida: '" + lexema + "'", 
            yyline + 1, 
            yycolumn + 1,
            lexema));
    }
%}

%eofval{
    return symbol(sym.EOF);
%eofval}

/* Definición de caracteres ASCII imprimibles */
ASCII = [\x20-\x7E]  // Esto ya debería incluir las comillas

/* Palabras reservadas */
CONJ = "CONJ"
OPERA = "OPERA"
EVALUAR = "EVALUAR"

/* Operadores */
UNION = "U"
INTERSECCION = "&"
COMPLEMENTO = "^"
DIFERENCIA = "-"
RANGO = "~"

/* Otros símbolos y estructuras */
IDENT = [a-zA-Z_][a-zA-Z_0-9]*
NUM = [0-9]+
LLAVE_IZQ = "{"
LLAVE_DER = "}"
ARROW = "->"
COMA = ","
PAREN_IZQ = "("
PAREN_DER = ")"
DOS_PUNTOS = ":"
PUNTO_COMA = ";"

%%

/* Palabras reservadas */
{CONJ} { return symbol(sym.CONJ, yytext(), "Palabra Reservada"); }
{OPERA} { return symbol(sym.OPERA, yytext(), "Palabra Reservada"); }
{EVALUAR} { return symbol(sym.EVALUAR, yytext(), "Palabra Reservada"); }

/* Operadores */
{UNION} { return symbol(sym.UNION, yytext(), "Operador"); }
{INTERSECCION} { return symbol(sym.INTERSECCION, yytext(), "Operador"); }
{COMPLEMENTO} { return symbol(sym.COMPLEMENTO, yytext(), "Operador"); }
{DIFERENCIA} { return symbol(sym.DIFERENCIA, yytext(), "Operador"); }
{RANGO} { return symbol(sym.RANGO, yytext(), "Operador"); }

/* Identificación de identificadores y números */
{IDENT} { return symbol(sym.IDENT, yytext(), "Identificador"); }
{NUM} { return symbol(sym.NUM, yytext(), "Número"); }

/* Identificación de símbolos */
{LLAVE_IZQ} { return symbol(sym.LLAVE_IZQ, yytext(), "Símbolo"); }
{LLAVE_DER} { return symbol(sym.LLAVE_DER, yytext(), "Símbolo"); }
{ARROW} { return symbol(sym.ARROW, yytext(), "Símbolo"); }
{COMA} { return symbol(sym.COMA, yytext(), "Símbolo"); }
{PAREN_IZQ} { return symbol(sym.PAREN_IZQ, yytext(), "Símbolo"); }
{PAREN_DER} { return symbol(sym.PAREN_DER, yytext(), "Símbolo"); }
{DOS_PUNTOS} { return symbol(sym.DOS_PUNTOS, yytext(), "Símbolo"); }
{PUNTO_COMA} { return symbol(sym.PUNTO_COMA, yytext(), "Símbolo"); }

/* Caracteres ASCII */
{ASCII} { return symbol(sym.ASCII, yytext(), "Carácter ASCII"); }

/*manejo de comillas*/
\" { return symbol(sym.ASCII, yytext(), "Carácter ASCII"); }


/* Comentarios de una línea */
"#"[^\n]* { /* Ignorar comentarios de una línea */ }

/* Comentarios multilínea */
"<!([^!]|[!]([^>]))*!>" { /* Ignorar comentarios multilínea */ }

/*Ignorar espacios, tabulaciones y saltos de linea*/
[ \t\r\n]+ { /* Ignorar espacios, tabulaciones y saltos de línea */ }

. { /* Cualquier carácter no reconocido */
    throw new Error("Error: could not match input \"" + yytext() + "\" en la línea " + (yyline+1) + ", columna " + (yycolumn+1));
}
