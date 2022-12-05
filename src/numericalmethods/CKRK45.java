package numericalmethods;

//class for the Cash-Karp Runge-Kutta 4/5 (CKRK45) Numerical Integration Method
public class CKRK45 {

    //global variables
    //arrays representing the butcher tableau for CKRK45
    private static double[][] a = {{},{1./5.},{3./40.,9./40.},{3./10.,-9./10.,6./5.},{-11./54.,5./2.,-70./27.,35./27.},{1631./55296., 17./512., 575./13824., 44275./110592., 253./4096.}};
    private static double[] c = {0., 1./5., 3./10., 3./5., 1., 7./8.};
    private static double[][] b = {{37./378.,0.,250./621.,125./594.,0.,512./1771.},{2825./27648.,0.,18575./48384.,13525./55296.,277./14336.,1./4.}};

    private static final int f = 6; // number of stages, i.e. number of k functions

    //find y_is for a given x using a step size of h
    // and initial conditions y0s and x0
    public static double[] integrate(double x_0, double x_f, double[] y_0, double h, int maxIt, double eps, double S, SetOfODEs f) {
        //TODO: check for null arrays
        int n = y_0.length; // number of ys

        //define variables needed
        double x_i = x_0;

        //y arrays
        double[] y_i;
        y_i = new double[n];
        for(int i = 0 ; i < n; i++) {
            y_i[i] = y_0[i];
        }

        //loop for iterating over x
        while (Math.abs(x_i) < Math.abs(x_f)) {
            //initialize local arrays
            double[][] k = new double[CKRK45.f][n]; //array for storing calculated kis


            //find k_i values for each y_il

            //for a y_il : k_ij = f(x_i + c_j&h, y_i+h*sum^(j-1)_(m=0)(a_jm*k_im))
            try {
                for (int j = 0; j < CKRK45.f; j++) { // f = number of kis

                    double[] yi_temp = new double[n]; //array will be used to store y values used for calculating kis
                    for (int l = 0; l < n; l++) { //iterate over each y
                        yi_temp[l] = y_i[l];
                        for (int m = 0; m < j; m++) { //iterate over each a
                            yi_temp[l] += h * a[j][m] * k[m][l];
                        }
                    }
                    k[j] = f.calculateValue(x_i + c[j] * h, yi_temp);


                }


                //error truncation

                //error arrays
                double [] e_current = new double[n];
                double [] e_target = new double[n];

                //arrays for storing y_z where z = i + 1
                //y5_z and y4_z correspond to the fifth and fourth order results respectively
                double[] y5_z = new double[n];
                double[] y4_z = new double[n];

                double min_e_ratio = 0;
                for (int j = 0; j < n; j++) {
                    y5_z[j] = y_i[j];
                    y4_z[j] = y_i[j];

                    for (int l = 0; l < CKRK45.f; l++) {//iterate over b to calculate new ys (y_z)
                        y5_z[j] += h*b[0][l]*k[l][j];
                        y4_z[j] += h*b[1][l]*k[l][j];
                    }

                    e_current[j] = Math.abs(y4_z[j]-y5_z[j]);
                    e_target[j] = eps*(Math.abs(y_i[j])+Math.abs(y5_z[j]-y_i[j]));
                    double e_ratio = e_target[j]/e_current[j];
                    if (j == 0 || e_ratio < min_e_ratio) min_e_ratio = e_ratio;

                }

                //adapt step size
                if (min_e_ratio >= 1) {

                    //step was succesful, update x with step h
                    x_i += h;
                    // update h for the next step
                    h = S*h*Math.pow(min_e_ratio,0.2);
                    //update ys with 5th order results
                    for (int i = 0; i < n; i++) {
                        y_i[i] = y5_z[i];
                    }

                }
                else{
                    // step failed, update h and try again, x remains the same
                    h = S*h*Math.pow(min_e_ratio,0.25);

                }

                if(Math.abs(x_i+h)>Math.abs(x_f)) h=x_f-x_i;//correct final step if necessary

            }
            catch(IllegalArgumentException e){
                h = h/2.;
            }



        }



        return y_i;

    }



    }

