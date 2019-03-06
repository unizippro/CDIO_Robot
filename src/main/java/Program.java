import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;
import lejos.robotics.RangeFinder;

import java.rmi.RemoteException;

public class Program {
    public static void main(String[] args) throws Exception {
        RemoteEV3 ev3 = new RemoteEV3("192.168.2.6");

        RMIRegulatedMotor m1 = null;
        RMIRegulatedMotor m2 = null;
        RMISampleProvider distance = null;

        try {
            m1 = ev3.createRegulatedMotor("A", 'L');
            m2 = ev3.createRegulatedMotor("B", 'L');
            distance = ev3.createSampleProvider("S2", "lejos.hardware.sensor.EV3UltrasonicSensor", null);

            m1.setSpeed((int) m1.getMaxSpeed());
            m2.setSpeed((int) m2.getMaxSpeed());

            RangeFinderCM rangeFinderCM = new RangeFinderCM(distance);

            m1.forward();
            m2.forward();


            System.out.println(rangeFinderCM.getRange());
            Thread.sleep(2000);
//            while (rangeFinderCM.getRange() > 20) {
//            }

            m1.stop(true);
            m2.stop(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m1 != null) m1.close();
            if (m2 != null) m2.close();
            if (distance != null) distance.close();
        }
    }


    public static class RangeFinderCM implements RangeFinder {
        private RMISampleProvider source;

        public RangeFinderCM(RMISampleProvider source) {
            this.source = source;
        }

        @Override
        public float getRange() {
            try {
                return this.source.fetchSample()[0];
            } catch (RemoteException e) {
                e.printStackTrace();

                return -1;
            }
        }

        @Override
        public float[] getRanges() {
            try {
                return this.source.fetchSample();
            } catch (RemoteException e) {
                e.printStackTrace();

                return new float[0];
            }
        }
    }
}