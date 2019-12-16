package br.com.gestao.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.swing.text.MaskFormatter;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.UUID;

public class Utils {
    private Integer TIMEOUT_VALUE = 5000;

    /**
     * Exemplo
     * CPF - ###.###.###-#
     * CNPJ - ##.###.###/####-##
     *
     * @param valor
     * @param mascara
     * @return
     */
    public static String formatarMascaraTexto(String valor, String mascara) {
        // Remover formatacao do CNPJ
        valor = valor.replace(".", "").replace(".", "").replace("/", "").replace("-", "");

        // Remove formatacao do CPF
        valor = valor.replace(".", "").replace(".", "").replace("-", "");

        try {
            MaskFormatter mask = new MaskFormatter(mascara);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(valor);
        } catch (Exception ex) {
            return " --> ERRO <--";
        }
    }

    public static String formatarDinheiroRS(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor);
    }

    public static String formatarDinheiroPorExtenso(BigDecimal valor) {
        return new NumeroExtenso(valor).toMonetario();
    }

    public static String formatarTelefone(String valor) {
        try {
            if (valor.isEmpty()) {
                return "";
            }

            String mascara = valor.length() == 10 ? "(##) ####-####" : "(##) #####-####";

            MaskFormatter mask = new MaskFormatter(mascara);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(valor);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getHoraFormatada(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(data);
    }

    public static String getDataFormatada(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(data);
    }

    public static String getDataHoraFormatada(Date data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(data);
    }

    public static String getDataPorExtenso(Date data) {
        return getDataPorExtenso(data, false);
    }

    public static String getDataPorExtenso(Date data, Boolean mostrarDiaDaSemana) {
        // CRIO AQUI UM FORMATADOR PASSANDO UM ESTILO DE FORMATAÇÃO : DateFormat.FULL E PASSANDO UM LOCAL DA DATA : new Locale("pt", "BR")
        DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));

        // FORMATO A DATA, O FORMATADOR ME RETORNA A STRING DA DATA FORMATADA
        String dataExtenso = formatador.format(data);

        if (mostrarDiaDaSemana) {
            return dataExtenso;
        }

        // AQUI É CASO VOCÊ QUEIRA TIRAR O DIA DA SEMANA QUE APARECE NO PRIMEIRO EXEMPLO
        int index = dataExtenso.indexOf(",");
        int lenght = dataExtenso.length();

        // MOSTRA A DATA
        return dataExtenso.substring(++index, lenght);
    }

    public static String StrZeroEsquerda(String value, int n) {
        String s = value.trim();
        StringBuffer resp = new StringBuffer();
        int fim = n - s.length();
        for (int x = 0; x < fim; x++)
            resp.append('0');
        return resp + s;
    }

    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static Date convertDate(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }

    public String getURL(String urlParam) throws IOException {
        try {
            URL url = new URL(urlParam);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(TIMEOUT_VALUE);
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder jsonSb = new StringBuilder();
            br.lines().forEach(l -> jsonSb.append(l.trim()));

            is.close();
            br.close();
            return jsonSb.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public String gerarNomeUnico() {
        return UUID.randomUUID().toString();
    }

    /**
     * Verifica se o cnpj informado e valido
     *
     * @param CNPJ CNPJ a validar
     * @return boolean
     */
    public static boolean validarCNPJ(String CNPJ) {

        CNPJ = CNPJ.replace(".", "").replace("/", "").replace("-", "");
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                (CNPJ.length() != 14))
            return (false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else dig14 = (char) ((11 - r) + 48);

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return (true);
            else return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    /**
     * Verifica se o cpf informado e valido
     *
     * @param CPF CPF a validar
     * @return boolean
     */
    public static boolean validarCPF(String CPF) {
        CPF = CPF.replace(".", "").replace("-", "");
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return (false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char) (r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return (true);
            else return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }

    /**
     * Converte uma imagem em base64
     *
     * @param file
     * @return
     */
    private static String encodeFileToBase64Binary(File file) throws IOException {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return encodedfile;
    }
}
