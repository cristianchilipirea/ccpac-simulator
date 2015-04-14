package utils;

public class Statistics {

	public static double chiSquareTest(double[] x, double[] y) {
		double rez = 0;
		for(int i = 0; i < x.length; i++)
			if(y[i] != 0)
				rez += Math.pow(x[i] - y[i],2) / y[i];
		return Math.sqrt(rez);
	}

	public static double[] crossCorelate(double[] x, double[] y, int maxdelay) {
		double rez[] = new double[maxdelay * 2];
		double mx, my, sx, sy, sxy, denom, r;
		int j;
		if(x.length != y.length)
			return null;
		/* Calculate the mean of the two series x[], y[] */
		mx = 0;
		my = 0;
		for (int i = 0; i < x.length; i++) {
			mx += x[i];
			my += y[i];
		}
		mx /= x.length;
		my /= y.length;

		/* Calculate the denominator */

		for (int delay = -maxdelay; delay < maxdelay; delay++) {
			sx = 0;
			sy = 0;
			for (int i = 0; i < x.length; i++) {
				j = i - delay;
				if (j < 0 || j >= x.length)
					continue;
				sx += (x[i] - mx) * (x[i] - mx);
				sy += (y[j] - my) * (y[j] - my);
			}
			denom = Math.sqrt(sx) * Math.sqrt(sy);

			/* Calculate the correlation series */
			sxy = 0;
			for (int i = 0; i < x.length; i++) {
				j = i - delay;
				if (j < 0 || j >= x.length)
					continue;
				sxy += (x[i] - mx) * (y[j] - my);
			}
			r = sxy / denom;
			rez[delay + maxdelay] = r;
			/* r is the correlation coefficient at "delay" */
		}
		return rez;
	}
}
