//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine;

public class WE_PerlinNoise {
	
	public static double PerlinNoise2D(long seed, double x, double y, double persistence, int number_of_octaves) {
		double total = 0;
		for (int i = 1; i <= number_of_octaves; i++) {
			double
				frequency = Math.pow(2, i),
				amplitude = Math.pow(persistence, i);
			total += CosineInterpolatedNoise2D(seed, x * frequency, y * frequency) * amplitude;
		}
		return total;
	}
	
	public static double CosineInterpolatedNoise2D(long seed, double x, double y) {
		long
			lx = (long) x,
			ly = (long) y;
		double
			fx = Math.abs(x) - Math.abs(lx),
			fy = Math.abs(y) - Math.abs(ly);
		
		long x1 = Math.abs(x) == x ? lx + 1L : lx - 1L;
		long y1 = Math.abs(y) == y ? ly + 1L : ly - 1L;
		
		double
			v1 = SmoothNoise2D(seed, lx, ly),
			v2 = SmoothNoise2D(seed, x1, ly),
			v3 = SmoothNoise2D(seed, lx, y1),
			v4 = SmoothNoise2D(seed, x1, y1);
		
		double
			i1 = CosineInterpolate(v1, v2, fx),
			i2 = CosineInterpolate(v3, v4, fx);
		
		return CosineInterpolate(i1, i2, fy);
	}
	
	public static double SmoothNoise2D(long seed, long x, long y) {
		double
			corners = (NumberNoise2D(seed, x - 1L, y - 1L) + NumberNoise2D(seed, x + 1L, y - 1L) + NumberNoise2D(seed, x - 1L, y + 1L) + NumberNoise2D(seed, x + 1L, y + 1L)) / 16L,
			sides = (NumberNoise2D(seed, x - 1L, y) + NumberNoise2D(seed, x + 1L, y) + NumberNoise2D(seed, x, y - 1L) + NumberNoise2D(seed, x, y + 1L)) / 8L,
			center = NumberNoise2D(seed, x, y) / 4L;
		return corners + sides + center;
	}
	
	public static double CosineInterpolate(double a, double b, double x) {
		double f = (1.0D - Math.cos(x * Math.PI)) * 0.5D;
		return a * (1.0D - f) + b * f;
	}
	
	public static double NumberNoise2D(long seed, long x, long y) {
		long n = x + y * 31L + seed * 11L;
		n = (n << 13) ^ n;
		return 1.0 - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7FFFFFFF) / 1073741824.0;
	}
}