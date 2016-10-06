package com.jcs;

/**
 * Created by Jcs on 5/10/2016.
 */
public class Firework extends Particle {
    /**
     * Fireworks have an integer type, used for firework rules.
     */
    public int type;

    /**
     * The age of a firework determines when it detonates. Age gradually
     * decreases; when it passes zero the firework delivers its payload.
     * Think of age as fuse-left.
     */
    public float age;

    /**
     * Updates the firework by the given duration of time. Returns true if the
     * firework has reached the end of its life and needs to be removed.
     *
     * @param duration the delta time for integrate
     *
     * @return true iff age major at 0 and position.y major at 0
     */
    public boolean update(float duration) {

        // Update our physical state
        integrate(duration);

        // We work backwards from our age to zero.
        age -= duration;

        return (age < 0) || (getPosition().y < 0);
    }
}
