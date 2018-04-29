import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FastFourierTransform {


    // Andrej Hafner - 63160122
    static Scanner sc;
    public static void main(String[] args) {

        if(args.length  == 0 || args.length > 1)
        {
            System.out.println("Invalid arguments.");
            return;
        }
        sc = new Scanner(System.in);

        int coefLen = Integer.parseInt(args[0]);

        // If the length of coefficients is not a power of 2, we need to set the size of array to the neareast higher number, thats a power of 2
        int twoPowLen = (coefLen > 0 && ((coefLen & (coefLen - 1)) == 0)) ? coefLen : ((int) Math.pow(2, Math.ceil(Math.log(coefLen)/Math.log(2))));

        Complex[] coef = new Complex[twoPowLen];

        for(int i = 0; i < twoPowLen; i++)
        {
            coef[i] = (i < coefLen) ? new Complex(sc.nextDouble(),0) : new Complex(0,0);
        }

        recursiveFFT(coef);
    }

    static Complex[] recursiveFFT(Complex[] coef)
    {
        if(coef.length == 1) return coef;

        // split to odd and even coefficients
        Complex[] sodi = new Complex[(coef.length % 2 == 0) ? coef.length / 2 : coef.length / 2 + 1];
        Complex[] lihi = new Complex[(coef.length % 2 == 0) ? coef.length / 2 : coef.length / 2];

        for(int i = 0; i < coef.length; i++)
            if(i % 2 == 0)
                sodi[i / 2] = coef[i];
            else
                lihi[i / 2] = coef[i];

        // reuse the arrays
        sodi = recursiveFFT(sodi);
        lihi = recursiveFFT(lihi);

        // get n-th primitive root
        Complex w = Complex.n_root(coef.length);
        Complex wk = new Complex(1,0);
        Complex[] merged = new Complex[coef.length];

        // do the butterfly merging
        for(int i = 0; i < (coef.length / 2); i++)
        {
            merged[i] = sodi[i].add(wk.multiply(lihi[i]));
            merged[i + (coef.length / 2)] = sodi[i].subtract(wk.multiply(lihi[i]));
            wk = wk.multiply(w);
        }

        for(int i = 0; i < merged.length; i++)
            System.out.print(merged[i] + " ");
        System.out.println();

        return merged;

    }
}


