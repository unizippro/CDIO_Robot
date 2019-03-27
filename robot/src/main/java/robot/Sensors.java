package robot;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.AbstractFilter;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Sensors extends UnicastRemoteObject implements ISensors {

    private EV3IRSensor irSensor;
    private RangeFinder irRangeFinder;


    protected Sensors(EV3IRSensor irSensor) throws RemoteException {
        this.irSensor = irSensor;
        this.irRangeFinder = new RangeFinderRaw(this.irSensor.getDistanceMode());
    }

    @Override
    public double getRange() {
        return this.irRangeFinder.getRange();
    }

    @Override
    public boolean isWithinRange(double range) {
        return this.irRangeFinder.getRange() <= range;
    }


    public static class RangeFinderRaw extends AbstractFilter implements RangeFinder {
        private final float[] buffer;

        public RangeFinderRaw(SampleProvider source) {
            super(source);
            this.buffer = new float[this.sampleSize];
        }

        @Override
        public float getRange() {
            this.fetchSample(this.buffer, 0);

            return this.buffer[0];
        }

        @Override
        public float[] getRanges() {
            float[] sample = new float[this.sampleSize];
            this.fetchSample(sample,0);

            return sample;
        }
    }
}
