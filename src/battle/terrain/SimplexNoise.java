package battle.terrain;

import java.util.Random;

public class SimplexNoise {

    private final SimplexNoise_octave[] octaves;
    private final float[] frequencys;
    private final float[] amplitudes;
    private final float persistence;

    public SimplexNoise(int largestFeature, float persistence, int seed) {
        this.persistence = persistence;

        int numberOfOctaves = (int) Math.ceil(Math.log10(largestFeature) / Math.log10(2));
        octaves = new SimplexNoise_octave[numberOfOctaves];
        frequencys = new float[numberOfOctaves];
        amplitudes = new float[numberOfOctaves];

        Random rnd = new Random(seed);

        for (int i = 0; i < numberOfOctaves; i++) {
            octaves[i] = new SimplexNoise_octave(rnd.nextInt());

            frequencys[i] = (float) Math.pow(2, i);
            amplitudes[i] = (float) Math.pow(persistence, octaves.length - i);




        }

    }

    public float getNoise(float x, float y) {

        float result = 0;

        for (int i = 0; i < octaves.length; i++) {
            result += (octaves[i].noise(x / (frequencys[i]), y / (frequencys[i])) * (amplitudes[i]));
        }
        return result;

    }

    public float getNoise(float x, float y, float z) {

        float result = 0;

        for (int i = 0; i < octaves.length; i++) {
            float frequency = (float) Math.pow(2, i);
            float amplitude = (float) Math.pow(persistence, octaves.length - i);

            result += octaves[i].noise(x / frequency, y / frequency, z / frequency) * amplitude;
        }


        return result;

    }
}
