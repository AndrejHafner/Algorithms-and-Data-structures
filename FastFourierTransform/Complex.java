import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.RoundingMode;
import java.math.BigDecimal;

class Complex
{
    double im,re;

    public Complex(double re, double im)
    {
        this.im = im;
        this.re = re;
    }

    /*
        This Complex number parser (string -> Complex) found on StackExchange, written by user rickyjoepr.
        Source: https://codereview.stackexchange.com/questions/121741/parsing-a-complex-number-using-regular-expressions?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
     */
    public Complex(String c) {

        String numberNoWhiteSpace = c.replaceAll("\\s","");

        // Matches complex number with BOTH real AND imaginary parts.
        // Ex: -3-2.0i
        Pattern patternA = Pattern.compile("([-]?[0-9]+\\.?[0-9]?)([-|+]+[0-9]+\\.?[0-9]?)[i$]+");

        // Matches ONLY real number.
        // Ex: 3.145
        Pattern patternB = Pattern.compile("([-]?[0-9]+\\.?[0-9]?)$");

        // Matches ONLY imaginary number.
        // Ex: -10i
        Pattern patternC = Pattern.compile("([-]?[0-9]+\\.?[0-9]?)[i$]");

        Matcher matcherA = patternA.matcher(numberNoWhiteSpace);
        Matcher matcherB = patternB.matcher(numberNoWhiteSpace);
        Matcher matcherC = patternC.matcher(numberNoWhiteSpace);

        if (matcherA.find()) {
            re = Double.parseDouble(matcherA.group(1));
            im = Double.parseDouble(matcherA.group(2));
        } else if (matcherB.find()) {
            re = Double.parseDouble(matcherB.group(1));
            im = 0;
        } else if (matcherC.find()) {
            re= 0;
            im = Double.parseDouble(matcherC.group(1));
        }
    }



    Complex add(Complex a)
    {
        return new Complex(this.re + a.re,this.im + a.im);
    }

    Complex subtract(Complex a)
    {
        return new Complex(this.re - a.re,this.im - a.im);
    }

    Complex multiply(Complex a)
    {
        double real = this.re * a.re - this.im * a.im;
        double imaginary = this.im * a.re + this.re * a.im;
        return new Complex(real,imaginary);
    }

    Complex multiply(double a)
    {
        return new Complex(this.re * a, this.im * a);
    }

    Complex divide(Complex a)
    {
        double div = Math.pow(a.re,2) + Math.pow(a.im,2);
        double real = (this.re * a.re + this.im * a.im)/div;
        double imaginary = (this.im * a.re - this.re * a.im)/div;
        return new Complex(real,imaginary);
    }

    static Complex n_root(int n)
    {
        double pow = (2 * Math.PI) / n;
        double real = Math.cos(pow);
        double imaginary = Math.sin(pow);
        return new Complex(real,imaginary);
    }

    public static double round(double value, int places) {

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public String toString() {
        return (re != 0 ? round(re,1)+((im <= 0 ) ? "" : "+") : "")+(im != 0 ? round(im,1)+"i" : "") + (re == 0 && im == 0 ? "0.0" : "");
    }
}