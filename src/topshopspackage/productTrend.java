package topshopspackage;/* Author: Clayton Frandeen
 *  Last Modified: April 24, 2025
 *  File Description: productTrend.java is a file that will be passed a product.
 *  This file will take the product sales by the last 7 weeks, and will create a
 *  regression line, or line of best fit.
 */

public class productTrend {

    //regressionResult will return the intercept, and the slope of the line of best fit
    public static class regressionResult {
        public float beta0;    // Intercept
        public float beta1;    // Slope

        public regressionResult(float beta0, float beta1) {
            this.beta0 = beta0;
            this.beta1 = beta1;
        }
    }

    //calculates beta0 and beta 1 based off the sales from the last seven weeks
    //to create a trend line for the viewed product
    public static regressionResult calculateRegression(product current){
        String sales = current.getSalesBy7();
        int[] salesByWeek = toNum(sales);
        int n = salesByWeek.length;
        float sumX = 0, sumY = 0;


        for (int i = 0; i < n; i++) {
            int x = i + 1;          //days 1 through 7
            int y = salesByWeek[i];  //sales on that day

            sumX += x;
            sumY += y;
        }
        float xMean = sumX / n;
        float yMean = sumY / n;

        float sumOfSquares = 0;
        float sumOfProducts = 0;
        for (int i = 0; i < n; i++) {
            int x = i + 1;
            sumOfProducts = sumOfProducts + ((x - xMean) * (salesByWeek[i] - yMean)); //sums (Xi - Xmean) * (Yi - Ymean)
            sumOfSquares = sumOfSquares + (x - xMean) * (x - xMean);                              //sums (Xi - Xmean)^2
        }

        float beta1 = sumOfProducts / sumOfSquares;
        float beta0 = yMean - beta1 * xMean;

        return new regressionResult(beta0, beta1);
    }

    //Takes the sales by 7 days string and turns into an int array
    // returns sales
    public static int[] toNum(String sales) {
        String[] split = sales.split(" ");
        int[] salesByDay = new int [split.length];

        for(int i = 0; i < split.length; i++) {
            salesByDay[i] = Integer.parseInt(split[i]);
        }
        return  salesByDay;
    }

    public static float calculateGrowthRate(float beta0, float beta1) {
        float startSales = beta1 * 1 + beta0;
        float endSales = beta1 * 8 + beta0;
        return ((endSales - startSales) / endSales) * 100;
    }
}


