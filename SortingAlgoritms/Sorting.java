import java.util.Arrays;
import java.util.Scanner;

public class Sorting {

    // Andrej Hafner - 63160122

    private static boolean trace, count, sortAsc;
    private static String algTag = "";
    private static int size = -1;
    private static int[] data;
    private static Scanner sc = new Scanner(System.in);
    private static int cmpCnt, priCnt = 0;

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Premalo argumentov");
            System.exit(0);
        }

        readParams(args);
//        readData(args.length);
//        data = new int[]{8, 5, 6, 1, 7, 2};
//        data = new int[]{8, 5, 6, 1, 7, 2, 0, 9};
//        data = new int[]{5, 1, 6, 1, 6, 2, 0, 2};
        data = new int[]{256, 123456, 100, 65536, 300, 42, 7, 16777216};
        sort();

//        System.out.println(Arrays.toString(data));

    }

    static void sort() {
        switch (algTag) {
            case "bs":
                if(trace)
                    bubblesort();
                else {
                    priCnt = cmpCnt = 0;
                    bubblesort();
                    priCnt = cmpCnt = 0;
                    bubblesort();
                    priCnt = cmpCnt = 0;
                    sortAsc = !sortAsc;
                    bubblesort();
                }
                break;

            case "ss":
                if (trace)
                    selectionsort();
                else {
                    priCnt = cmpCnt = 0;
                    selectionsort();
                    priCnt = cmpCnt = 0;
                    selectionsort();
                    priCnt = cmpCnt = 0;
                    sortAsc = !sortAsc;
                    selectionsort();
                }

                break;

            case "is":
                if(trace)
                    insertionsort();
                else {
                    priCnt = cmpCnt = 0;
                    insertionsort();
                    priCnt = cmpCnt = 0;
                    insertionsort();
                    priCnt = cmpCnt = 0;
                    sortAsc = !sortAsc;
                    insertionsort();
                }
                break;

            case "hs":
                if(trace)
                    heapsort();
                else
                {
                    priCnt = cmpCnt = 0;
                    heapsort();
                    priCnt = cmpCnt = 0;
                    heapsort();
                    priCnt = cmpCnt = 0;
                    sortAsc = !sortAsc;
                    heapsort();
                }
                break;

            case "qs":
                if(count) {
                    priCnt = cmpCnt = 0;
                    quicksort();
                    priCnt = cmpCnt = 0;
                    quicksort();
                    priCnt = cmpCnt = 0;
                    sortAsc = !sortAsc;
                    quicksort();
                }
                else
                    quicksort();
                break;

            case "ms":
                if(trace)
                    mergesort();
                else
                {
                    priCnt = cmpCnt = 0;
                    mergesort();
                    priCnt = cmpCnt = 0;
                    mergesort();
                    priCnt = cmpCnt = 0;
                    sortAsc = !sortAsc;
                    mergesort();
                }
                break;

            case "cs":
                countingsort();
                break;

            case "rs":
                radixsort();
                break;

        }
    }

    private static void bubblesort() {
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            trace(k);
            for (int j = data.length - 1; j > i; j--) {
                if (compare(data[j - 1], data[j])) {
                    priCnt += 3;
                    int tmp = data[j - 1];
                    data[j - 1] = data[j];
                    data[j] = tmp;
                }
            }
            k++;
        }
        count();
    }

    private static void selectionsort() {
        for (int i = 0; i < data.length - 1; i++) {
            int idxMinMax = i;
            for (int j = i+1; j < data.length; j++) {
                if (compare(data[idxMinMax], data[j])) {
                    idxMinMax = j;
                }
            }
            int tmp = data[i];
            trace(i);
            data[i] = data[idxMinMax];
            data[idxMinMax] = tmp;
            priCnt += 3;


        }
        trace(data.length - 1);
        count();
    }

    private static void insertionsort() {
        for (int i = 1; i < data.length; i++) {
            trace(i);

            int k = i - 1;
            int tmpI = data[i];
            while (k >= 0 && compare(data[k], tmpI)) {
                data[k + 1] = data[k];
                priCnt+=3;
                k--;

            }
            data[k + 1] = tmpI;
//            priCnt += 2;

        }
        trace(data.length);
        count();

    }

    private static void heapsort()
    {
        //ustvarimo kopico do spodaj navgor ( od konca polja do začetka)
        for(int i = (data.length / 2) - 1; i >= 0; i--)
            ustvariKopico(data.length,i);

        for(int i = data.length-1; i >= 0; i--)
        {
            hstrace(i);
            //zamenjamo koren in zadnji element
            int tmp = data[i];
            data[i] = data[0];
            data[0] = tmp;
            priCnt += 3;

            // popravimo v kopico
            ustvariKopico(i,0);

        }
        priCnt -= 3;
        count();
    }

    private static void ustvariKopico(int size,int root)
    {
        int left = 2*root + 1;
        int right = 2*root + 2;
        int minmax = root;

        if(left < size && compare(data[left],data[minmax]))
            minmax = left;

        if(right < size && compare(data[right],data[minmax]))
            minmax = right;

        if(minmax == root) return;

        int tmp = data[root];
        data[root] = data[minmax];
        data[minmax] = tmp;
        priCnt += 3;
        ustvariKopico(size,minmax);


    }

    private static void quicksort()
    {
        quicksort(0,data.length-1);
        count();
    }

    private static void quicksort(int left, int right)
    {
        if(left >= right) return;
        int pivot = data[(left+right)/2];
        priCnt++;
        int i = left;
        int j = right;
        while(i <= j)
        {
            while(compare(pivot,data[i])) i++;
            while(compare(data[j],pivot)) j--;
            if(i <= j)
            {
                int tmp = data[i];
                data[i] = data[j];
                data[j] = tmp;
                priCnt+=3;
                i++;
                j--;
            }
        }
        qstrace(left,right,j+1,i);
        quicksort(left,j);
        quicksort(i,right);
    }

    private static void countingsort()
    {
        countingsort(1,false);
    }

    private static void countingsort(int nbyte,boolean radix)
    {
        if(radix)
        {
            int[] count = new int[256];
            int[] tmp = new int[data.length];
            int shift = (8*(nbyte-1));
            int mask = 0xFF << shift;
            int modulo = 256;
            String twes = Integer.toHexString(mask);
            // prestejemo
            // shift right is okay because we have only positive integers (sign bit never 1, so always fill with 0)
            for (int aData : data)
            {
                int shifted = (aData & mask) >> shift;
                count[shifted]++;
            }

            // kumulativa
            for (int i = 1; i < count.length; i++)
                count[i] += count[i - 1];

            for (int i = 0; i < count.length; i++)
                System.out.print(count[i] + ((i == count.length - 1) ? "" : " "));
            System.out.println();

            // uredimo
            for (int i = 0; i < data.length; i++) {
                tmp[count[(data[i] & mask) >> shift]-1] = data[i];
                count[(data[i] & mask) >> shift]--;
            }

            data = tmp;

        }
        else {
            int[] count = new int[256];
            int[] tmp = new int[data.length];
            // prestejemo
            for (int aData : data) count[aData]++;

            // kumulativa
            for (int i = 1; i < count.length; i++)
                count[i] += count[i - 1];

            for (int i = 0; i < count.length; i++)
                System.out.print(count[i] + ((i == count.length - 1) ? "" : " "));
            System.out.println();

            // uredimo
            for (int i = 0; i < data.length; i++) {
                tmp[count[data[i]] - 1] = data[i];
                count[data[i]]--;
            }
            data = tmp;
        }

        if (sortAsc)
            for (int i = 0; i < data.length; i++)
                System.out.print(data[i] + ((i == data.length - 1) ? "" : " "));
        else
            for (int i = data.length - 1; i >= 0; i--)
                System.out.print(data[i] + ((i == 0) ? "" : " "));
        System.out.println();

    }

    private static void radixsort()
    {
        for(int i =  1; i <= 4; i++)
            countingsort(i,true);
    }

    private static void mergesort()
    {
        mergesort(0, data.length - 1);
        priCnt += data.length;
        count();
    }
    private static void mergesort(int left,int right)
    {
        if(!(left < right)) return;

        int middle = (left+right) / 2;

        qstrace(left,right,middle+1,-1);
        // leva polovica
        mergesort(left,middle);

        // desna polovica
        mergesort(middle+1,right);

        // zdruzimo obe polovici
        merge(left,right,middle);

    }
    // Zdruzimo arraya: [left...middle] in [middle+1... right]
    private static void merge(int left, int right, int middle)
    {
        int[] first = new int[middle - left + 1];
        int[] second = new int[right - middle];

        System.arraycopy(data, left, first, 0, first.length);

        for(int i = 0; i < second.length; i++)
        {
            second[i] = data[middle+i+1];
        }

        int n,m;
        m = n = 0;
        int arrIdx = left;
        while( m < first.length && n < second.length)
        {
            if(compareEquals(second[n],first[m]))
            {
                data[arrIdx] = first[m];
                m++;
                priCnt++;
            }
            else
            {
                data[arrIdx] = second[n];
                n++;
                priCnt++;
            }
            arrIdx++;
        }

        while( m < first.length)
        {
            data[arrIdx] = first[m];
            priCnt++;
            arrIdx++;
            m++;
        }

        while( n < second.length)
        {
            data[arrIdx] = second[n];
            priCnt++;
            arrIdx++;
            n++;
        }
        qstrace(left,right,-1,-1);


    }

    private static void hstrace(int upper)
    {
        if(!trace) return;
        int exp = 1;
        int cnt = 1;
        for(int i = 0; i <= upper; i++)
        {


            if(exp == i) {
                System.out.print("| ");
                exp = exp*2 +1;

            }
            System.out.print(data[i] + " ");

        }
        System.out.println();
    }

    private static void qstrace(int left,int right,int idx1,int idx2)
    {
        if(!trace) return;
        for(int i = left; i <= right; i++)
        {
            if(idx1 == i || idx2 == i)
                System.out.print("| ");
            if(idx1 == i && idx2 == i)
                System.out.print("| ");
            System.out.print(data[i] + " ");

        }
        System.out.println();
    }

    private static void trace(int idx)
    {
        if(!trace) return;
        for(int i = 0; i < data.length; i++)
        {
            if(idx == i)
                System.out.print("| ");
            System.out.print(data[i] + " ");

        }
        if(idx == data.length)
            System.out.print("| ");
        System.out.println();
    }

    private static void count()
    {
        if(count)
            System.out.println(cmpCnt + " " + priCnt);
    }

    private static boolean compare(int a, int b)
    {
        cmpCnt++;
        if(sortAsc)
            return a > b;
        else
            return a < b;

    }

    private static boolean compareEquals(int a,int b)
    {
        cmpCnt++;
        if(sortAsc)
            return a >= b;
        else
            return a <= b;
    }

    private static void readParams(String[] args)
    {
        trace = args[0].equals("trace");
        count = args[0].equals("count");
        algTag = args[1];
        sortAsc = args[2].equals("up");
        if(args.length == 4) {
            size = Integer.parseInt(args[3]);
        }
    }

    private static void readData(int len)
    {
        if(len == 3) // dinamicno povecevanje tabele
        {
            readDataResize();
        }
        else
        {
            readDataNormal();
        }
    }

    private static void readDataNormal()
    {
        data = new int[size];
        int cnt = 0;
        while(sc.hasNext())
        {
            data[cnt] = sc.nextInt();
            cnt++;
        }
    }

    private static void readDataResize()
    {
        int cnt = 0;
        int tmpArrSize = 10;
        int[] tmpArr = new int[tmpArrSize];
        while(sc.hasNext())
        {
            tmpArr[cnt] = sc.nextInt();
            cnt++;
            if(cnt == tmpArrSize)
            {
                tmpArrSize *= 2;
                int[] tmp = new int[tmpArrSize];
                System.arraycopy(tmpArr, 0, tmp, 0, tmpArr.length);
                tmpArr = tmp;
            }

        }
        data = new int[cnt];
        System.arraycopy(tmpArr, 0, data, 0, cnt);
    }
}
