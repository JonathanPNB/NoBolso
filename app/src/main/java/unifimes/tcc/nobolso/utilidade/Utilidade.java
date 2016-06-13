package unifimes.tcc.nobolso.utilidade;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import unifimes.tcc.nobolso.entity.Transacao;

/**
 * Created by Jonathan on 16/03/2016.
 */
public class Utilidade {

    private static final KeylistenerNumber keylistenerNumber = new KeylistenerNumber();

    private Context ctx;
    private InputMethodManager imm;
    private static DecimalFormat formatoMoeda = new DecimalFormat("###,###,##0.00");

    private static final Calendar c = Calendar.getInstance();
    private static int mes = c.get(Calendar.MONTH) + 1;
    private static int ano = c.get(Calendar.YEAR);
    private static int dia = c.get(Calendar.DAY_OF_MONTH);

    public Utilidade(Context ctx) {
        this.ctx = ctx;
    }

    private static class KeylistenerNumber extends NumberKeyListener {

        public int getInputType() {
            return InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
        }

        @Override
        protected char[] getAcceptedChars() {
            return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        }
    }

    public void alteraKeyboard(Context context, boolean exibir) {
        imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        //  Log.e("alteraKeyboard", String.valueOf(exibir));
        if (imm != null) {
            if (!exibir) {
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                return;
            } else {
                imm.toggleSoftInput(-1, InputMethodManager.HIDE_NOT_ALWAYS);
                return;
            }
        }

    }

    public void escondeTeclado(Context context, View v) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String formataMoeda(BigDecimal valor) {
        return String.valueOf(formatoMoeda.format(valor));
    }

    public static class mascaraMoeda implements TextWatcher {
        final EditText campo;
        final Context ctx;
        private boolean isUpdating;

        public mascaraMoeda(EditText campo, Context ctx) {
            super();
            this.campo = campo;
            this.ctx = ctx;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int after) {
            if (isUpdating) {
                isUpdating = false;
                return;
            }
            String valorFormatado = this.formataBigDecimalDigitacao(s.toString());

            isUpdating = true;
            this.campo.setText(valorFormatado);
            this.campo.setSelection(valorFormatado.length());
        }

        public String formataBigDecimalDigitacao(String valor) {
            String aux = "";
            int i;
            char separadorCentavo = ',';
            char separadorReal = '.';
            try {
                for (i = 0; i < valor.length(); i++) {
                    char c = valor.charAt(i);
                    if (c == '.')
                        c = separadorCentavo;
                    aux += c;

                    if (c == separadorCentavo) {
                        break;
                    }
                }

                for (int j = i + 1; j < valor.length(); j++) {
                    aux += valor.charAt(j);
                    i++;
                }

                valor = valor.replaceAll("[.]", "").replaceAll("[,]", "");
                int tamanho = valor.length();

                //          Log.e("formataBigDecimal", "Valor: " + valor + "/ Tamanho: " + tamanho);

                switch (tamanho) {
                    case 1:
                        valor = valor.substring(0, tamanho - 1) + "0" + separadorCentavo + "0" + valor.substring(tamanho - 1, tamanho);
                        break;
                    case 2:
                        valor = valor.substring(0, tamanho - 2) + "0" + separadorCentavo + valor.substring(tamanho - 2, tamanho);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        if (valor.charAt(0) == '0')
                            valor = valor.substring(1, tamanho - 2) + separadorCentavo + valor.substring(tamanho - 2, tamanho);
                        else
                            valor = valor.substring(0, tamanho - 2) + separadorCentavo + valor.substring(tamanho - 2, tamanho);
                        break;
                    case 6:
                    case 7:
                    case 8:
                        valor = valor.substring(0, tamanho - 5) + separadorReal + valor.substring(tamanho - 5, tamanho - 2) +
                                separadorCentavo + valor.substring(tamanho - 2, tamanho);
                        break;
                    case 9:
                    case 10:
                    case 11:
                        valor = valor.substring(0, tamanho - 8) + separadorReal + valor.substring(tamanho - 8, tamanho - 5) +
                                separadorReal + valor.substring(tamanho - 5, tamanho - 2) + separadorCentavo +
                                valor.substring(tamanho - 2, tamanho);
                        break;
                    default:
                        break;
                }
            } catch (NumberFormatException e) {
            }
            //        Log.e("formataBigDecimal", "Valor saída: " + valor);
            return valor;
        }
    }

    public static int getDia() {
        return dia;
    }

    public static int getAno() {
        return ano;
    }

    public static int getMes() {
        return mes;
    }

    public static String formataNumero(int numero, int casas) {
        String mascara = "";
        String c = "0";
        for (int i = 0; i < casas; i++) {
            mascara = mascara.concat(c);
        }
        DecimalFormat formato = new DecimalFormat(mascara);
      //  Log.e("formataNumero",numero+"-"+casas+" / "+mascara+" -- "+formato.format(numero));
        return formato.format(numero);
    }

    public static KeylistenerNumber getKeylistenerNumber() {
        return keylistenerNumber;
    }

    public static BigDecimal somaValores(ArrayList<Transacao> lista) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < lista.size(); i++) {
            total = total.add(lista.get(i).getValor());
            //  Log.e("somaValores", "i = " + i + " / " + total);
        }
        return total;
    }

    public static int ultimoDiadoMes(int mes) {
        int ultimoDia;
        //alterar para o mes de abril
        c.set(Calendar.MONTH, mes-1);
        //pega o ultimo dia do mes de abril
        ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("ultimoDiadoMes","dia = "+ultimoDia+"/"+mes);
        return ultimoDia;
    }

    public static BigDecimal calculaPorcentagem(BigDecimal valorItem, BigDecimal valorTotal) {
        BigDecimal resultado = valorItem.multiply(new BigDecimal("100")).divide(valorTotal, BigDecimal.ROUND_HALF_UP);
    //    Log.e("calculaPorcentagem",valorItem+" - "+valorTotal+" - "+resultado);
        return resultado;
    }

    public static String formataData(String formatoAntigo, String formatoNovo, String data) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatoAntigo);
        Date dataTransacao = null;
        try {
            dataTransacao = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(formatoNovo);
        return sdf.format(dataTransacao);
    }

}