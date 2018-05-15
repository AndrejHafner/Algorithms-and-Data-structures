import java.util.Scanner;

/**
 * Created by Andrej Hafner on 15. 05. 2018.
 */
class Matrix {
    private int[][] matrix;
    private int n, m;

    Matrix(int[][] matrix, int n, int m) {
        this.matrix = matrix;
        this.n = n;
        this.m = m;
    }

    /***
     * Ordinary multiplication of matrices -> n^3
     * @param mat
     * @return
     */
    Matrix ordinaryMultiply(Matrix mat)
    {
        return ordinaryMultiply(mat,true);
    }

    /***
     * Ordinary multiplication of matrices -> n^3
     * @param mat
     * @param print
     * @return
     */
    private Matrix ordinaryMultiply(Matrix mat, boolean print)
    {
        if(this.m != mat.n)
        {
            System.out.println("Wrong dimensions.");
            return null;
        }
        if(print) System.out.println("DIMS: " + this.n + "x" + mat.m);
        int[][] tmp = new int[this.n][mat.m];
        for(int i = 0; i < this.n; i++) // cez vrstice
            for(int j = 0; j < mat.m; j++)
            {
                int sum = 0;
                for(int k = 0; k < this.matrix[i].length; k++)
                    sum += this.matrix[i][k] * mat.matrix[k][j];
                tmp[i][j] = sum;
            }
        return new Matrix(tmp,this.n,mat.m);
    }

    /***
     * Primitive divide and conquer multiplication of two matrices (n^2)
     * @return
     */
    Matrix dacMultiply(Matrix mat)
    {
        return dacMultiply(mat,1);
    }

    /***
     * Primitive divide and conquer multiplication of two matrices (n^2)
     * @param minSize Defines below which size (nearest 2^n below minSize) multiplication should be ordinary
     * @return
     */
    Matrix dacMultiply(Matrix mat, int minSize)
    {
        int matMax = Math.max(mat.n,mat.m);
        int thisMax = Math.max(this.n,this.m);
        int size1 = (matMax > 0 && ((matMax & (matMax - 1)) == 0)) ? matMax : ((int) Math.pow(2, Math.ceil(Math.log(matMax)/Math.log(2))));
        int size2 = (thisMax > 0 && ((thisMax & (thisMax - 1)) == 0)) ? thisMax : ((int) Math.pow(2, Math.ceil(Math.log(thisMax)/Math.log(2))));
        int max = Math.max(size1,size2);

        Matrix a,b;
        a = resizeToSquare(this,max);
        b = resizeToSquare(mat,max);


        Matrix res = dacMultiplyRec(a,b,minSize);
        System.out.println("DIMS: " + max + "x" + max);
        return res;
    }

    /***
     * Multiplies two matrices using Strassen algorithm (divide and conquer) -> n^2.807
     * @param minSize Defines below which size (nearest 2^n below minSize) multiplication should be ordinary
     * @return
     */
    Matrix strassenMultiply(Matrix mat,int minSize)
    {
        int matMax = Math.max(mat.n,mat.m);
        int thisMax = Math.max(this.n,this.m);
        int size1 = (matMax > 0 && ((matMax & (matMax - 1)) == 0)) ? matMax : ((int) Math.pow(2, Math.ceil(Math.log(matMax)/Math.log(2))));
        int size2 = (thisMax > 0 && ((thisMax & (thisMax - 1)) == 0)) ? thisMax : ((int) Math.pow(2, Math.ceil(Math.log(thisMax)/Math.log(2))));
        int max = Math.max(size1,size2);

        Matrix a,b;
        a = resizeToSquare(this,max);
        b = resizeToSquare(mat,max);

        Matrix res = strassenMultiplyRec(a,b,minSize);
        System.out.println("DIMS: " + max + "x" + max);
        return res;
    }

    /***
     * Multiplies two matrices using Strassen algorithm (divide and conquer) -> n^3
     * @return
     */
    Matrix strassenMultiply(Matrix mat)
    {
        return strassenMultiply(mat,1);
    }

    /***
     * Multiplies two matrices using Strassen algorithm (divide and conquer) -> n^2.807
     * @param a
     * @param b
     * @return
     */
    private Matrix strassenMultiplyRec(Matrix a, Matrix b)
    {
        return strassenMultiplyRec(a,b,1);
    }


    
    private Matrix strassenMultiplyRec(Matrix a, Matrix b, int minSize)
    {
        if (a.n == 1 && b.n == 1)
        {
            int[][] res = new int[1][1];
            res[0][0] = a.matrix[0][0] * b.matrix[0][0];
            return new Matrix(res,1,1);
        }
        else if (a.n <= minSize && b.n <= minSize)
        {
            return a.ordinaryMultiply(b,false);
        }

        // Divide
        Matrix[] aArr = a.divideMatrix();
        Matrix[] bArr = b.divideMatrix();

        // Conquer
        Matrix p1 = strassenMultiplyRec(aArr[0],bArr[1].subtract(bArr[3]),minSize);
        System.out.println(p1.sum());
        Matrix p2 = strassenMultiplyRec((aArr[0].add(aArr[1])),bArr[3],minSize);
        System.out.println(p2.sum());
        Matrix p3 = strassenMultiplyRec((aArr[2].add(aArr[3])),bArr[0],minSize);
        System.out.println(p3.sum());
        Matrix p4 = strassenMultiplyRec(aArr[3],(bArr[2].subtract(bArr[0])),minSize);
        System.out.println(p4.sum());
        Matrix p5 = strassenMultiplyRec((aArr[0].add(aArr[3])),(bArr[0].add(bArr[3])),minSize);
        System.out.println(p5.sum());
        Matrix p6 = strassenMultiplyRec((aArr[1].subtract(aArr[3])),(bArr[2].add(bArr[3])),minSize);
        System.out.println(p6.sum());
        Matrix p7 = strassenMultiplyRec((aArr[0].subtract(aArr[2])),(bArr[0].add(bArr[1])),minSize);
        System.out.println(p7.sum());


        Matrix[] mergeArr = new Matrix[4];
        mergeArr[0] = p5.add(p4).subtract(p2).add(p6);
        mergeArr[1] = p1.add(p2);
        mergeArr[2] = p3.add(p4);
        mergeArr[3] = p1.add(p5).subtract(p3).subtract(p7);

        // Merge
        return mergeMatrices(mergeArr);
    }

    private Matrix dacMultiplyRec(Matrix a, Matrix b)
    {
        return dacMultiplyRec(a,b,1);
    }


    /***
     * Primitive divide and conquer multiplication of two matrices (n^2)
     * @param a
     * @param b
     * @param minSize Defines below which size (nearest 2^n below minSize) multiplication should be ordinary
     * @return
     */
    private Matrix dacMultiplyRec(Matrix a, Matrix b,int minSize) {
        if (a.n == 1 && b.n == 1)
        {
            int[][] res = new int[1][1];
            res[0][0] = a.matrix[0][0] * b.matrix[0][0];
            return new Matrix(res,1,1);
        }
        else if (a.n <= minSize || b.n <= minSize)
        {
            return a.ordinaryMultiply(b,false);
        }

        // Divide
        Matrix[] aArr = a.divideMatrix();
        Matrix[] bArr = b.divideMatrix();

        // Conquer
        Matrix a11b11 = dacMultiplyRec(aArr[0],bArr[0],minSize);
        System.out.println(a11b11.sum());
        Matrix a12b21 = dacMultiplyRec(aArr[1],bArr[2],minSize);
        System.out.println(a12b21.sum());
//
        Matrix a11b12 = dacMultiplyRec(aArr[0],bArr[1],minSize);
        System.out.println(a11b12.sum());

        Matrix a12b22 = dacMultiplyRec(aArr[1],bArr[3],minSize);
        System.out.println(a12b22.sum());

        Matrix a21b11 = dacMultiplyRec(aArr[2],bArr[0],minSize);
        System.out.println(a21b11.sum());

        Matrix a22b21 = dacMultiplyRec(aArr[3],bArr[2],minSize);
        System.out.println(a22b21.sum());

        Matrix a21b12 = dacMultiplyRec(aArr[2],bArr[1],minSize);
        System.out.println(a21b12.sum());

        Matrix a22b22 = dacMultiplyRec(aArr[3],bArr[3],minSize);
        System.out.println(a22b22.sum());


        Matrix[] mergeArr = new Matrix[4];
        mergeArr[0] = a11b11.add(a12b21);
        mergeArr[1] = a11b12.add(a12b22);
        mergeArr[2] = a21b11.add(a22b21);
        mergeArr[3] = a21b12.add(a22b22);

        // Merge
        return mergeMatrices(mergeArr);
    }

    /**
     * Merges the 4 matrices into a bigger square one
     * @param matrices
     * @return
     */
    private static Matrix mergeMatrices(Matrix[] matrices)
    {
        int size = matrices[0].n;
        int[][] res = new int[size * 2][size * 2];
        int k,l;

        k = l = 0;
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                res[i][j] = matrices[0].matrix[k][l];
                l++;
            }
            l = 0;
            k++;
        }

        k = l = 0;
        for(int i = 0; i < size; i++) {
            for (int j = size; j < res.length; j++) {
                res[i][j] = matrices[1].matrix[k][l];
                l++;
            }
            l = 0;
            k++;
        }

        k = l = 0;
        for(int i = size; i < res.length; i++) {
            for (int j = 0; j < size; j++) {
                res[i][j] = matrices[2].matrix[k][l];
                l++;
            }
            l = 0;
            k++;
        }

        k = l = 0;
        for(int i = size; i < res.length; i++) {
            for (int j = size; j < res.length; j++) {
                res[i][j] = matrices[3].matrix[k][l];
                l++;
            }
            l = 0;
            k++;
        }

        return new Matrix(res,size * 2,size * 2);
    }

    /**
     * Divides matrix to 4 submatrices of equal size
     * | 0 1 |
     * | 2 3 |
     * array indexes of submatrices
     */
    private Matrix[] divideMatrix()
    {
        Matrix[] matArr = new Matrix[4];

        Matrix tmp;
        int k = 0; int l = 0;
        int size = this.n/2;

        // upper left
        tmp = new Matrix(new int[size][size],size,size);
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tmp.matrix[k][l] = this.matrix[i][j];
                l++;
            }
            l = 0;
            k++;
        }
        matArr[0] = tmp;

        // upper right
        k = l = 0;
        tmp = new Matrix(new int[size][size],size,size);
        for(int i = 0; i < size; i++) {
            for (int j = size; j < this.n; j++) {
                tmp.matrix[k][l] = this.matrix[i][j];
                l++;
            }
            l = 0;

            k++;
        }
        matArr[1] = tmp;


        // bottom left
        k = l = 0;
        tmp = new Matrix(new int[size][size],size,size);
        for(int i = size; i < this.n; i++) {
            for (int j = 0; j < size; j++) {
                tmp.matrix[k][l] = this.matrix[i][j];
                l++;
            }
            l = 0;

            k++;
        }
        matArr[2] = tmp;

        // bottom right
        k = l = 0;
        tmp = new Matrix(new int[size][size],size,size);
        for(int i = size; i < this.n; i++) {
            for (int j = size; j < this.n; j++) {
                tmp.matrix[k][l] = this.matrix[i][j];
                l++;
            }
            l = 0;

            k++;
        }
        matArr[3] = tmp;


        return matArr;
    }


    /***
     * Adds the elements of the matrices
     * @param mat
     * @return
     */
    Matrix add(Matrix mat)
    {
        if(this.n != mat.n || this.m != mat.n)
        {
            System.out.println("Wrong dimensions.");
            return null;
        }

        int[][] res = new int[mat.n][mat.n];
        for(int i = 0; i < mat.n ; i++)
            for(int j = 0; j < mat.n; j++)
                res[i][j] = this.matrix[i][j] + mat.matrix[i][j];
        return new Matrix(res,mat.n,mat.n);
    }


    /***
     * Subtracts a matrix from a matrix
     * @param mat
     * @return
     */
    Matrix subtract(Matrix mat)
    {
        if(this.n != mat.n || this.m != mat.n)
        {
            System.out.println("Wrong dimensions.");
            return null;
        }

        int[][] res = new int[mat.n][mat.n];
        for(int i = 0; i < mat.n ; i++)
            for(int j = 0; j < mat.n; j++)
                res[i][j] = this.matrix[i][j] - mat.matrix[i][j];
        return new Matrix(res,mat.n,mat.n);
    }


    /***
     * Sums all the elements in a matrix
     * @return
     */
    int sum()
    {
        int sum = 0;
        for(int i = 0; i < this.matrix.length; i++)
            for(int j = 0; j <this.matrix[i].length; j++)
                sum += this.matrix[i][j];
        return  sum;
    }

    private static Matrix resizeToSquare(Matrix mat, int size)
    {
        if(mat.n == size) return mat;
        int[][] tmp = new int[size][size];
        for(int i = 0; i < mat.n; i++)
            for(int j = 0; j < mat.m; j++)
                tmp[i][j] = mat.matrix[i][j];
        return new Matrix(tmp,size,size);
    }

    /***
     * Builds a matrix using scanner with a format: -first row: n m (n=rows,m=columns), followd with n rows of m numbers
     * @param sc
     * @return
     */
    static Matrix buildMatrix(Scanner sc)
    {
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[][] mat = new int[n][m];

        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                mat[i][j] = sc.nextInt();

        return new Matrix(mat,n,m);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i <matrix.length; i++)
        {
            for(int j = 0; j < matrix[i].length; j++)
                builder.append(matrix[i][j] + " ");
            builder.append("\n");
        }
        return builder.toString();
    }
}
