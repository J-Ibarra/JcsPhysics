package com.jcs;

/**
 * Created by Jcs on 5/10/2016.
 */
public class Firework extends Particle {
    /**
     * Fireworks have an integer type, used for demos.ballistic.firework rules.
     */
    public int type;

    /**
     * The age of a demos.ballistic.firework determines when it detonates. Age gradually
     * decreases; when it passes zero the demos.ballistic.firework delivers its payload. Think
     * of age as fuse-left.
     */
    public float age;

    /**
     * Updates the demos.ballistic.firework by the given duration of time. Returns true if the
     * demos.ballistic.firework has reached the end of its life and needs to be removed.
     */

    public boolean update(float duration) {

        // Update our physical state
        integrate(duration);

        // We work backwards from our age to zero.
        age -= duration;

        return (age < 0) || (getPosition().y < 0);
    }
}
