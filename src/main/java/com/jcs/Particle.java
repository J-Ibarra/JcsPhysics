package com.jcs;

import org.joml.Vector3f;

/**
 * A particle is the simplest object that can be simulated in the
 * physics system.
 * <p>
 * It has position data (no orientation data), along with
 * velocity. It can be integrated forward through time, and have
 * linear forces, and impulses applied to it. The particle manages
 * its state and allows access through a set of methods.
 *
 * @author Juan Ibarra
 */
public class Particle {

    /**
     * Holds the inverse of the mass of the particle. It
     * is more useful to hold the inverse mass because
     * integration is simpler, and because in real time
     * simulation it is more useful to have objects with
     * infinite mass (immovable) than zero mass
     * (completely unstable in numerical simulation).
     */
    public float inverseMass = 0f;

    /**
     * Holds the amount of damping applied to linear
     * motion. Damping is required to remove energy added
     * through numerical instability in the integrator.
     */
    public float damping = 0f;

    /**
     * Holds the linear position of the particle in
     * world space.
     */
    public Vector3f position = new Vector3f();

    /**
     * Holds the linear velocity of the particle in
     * world space.
     */
    public Vector3f velocity = new Vector3f();

    /**
     * Holds the accumulated force to be applied at the next
     * simulation iteration only. This value is zeroed at each
     * integration step.
     */
    public Vector3f forceAccum = new Vector3f();

    /**
     * Holds the acceleration of the particle.  This value
     * can be used to set acceleration due to gravity (its primary
     * use), or any other constant acceleration.
     */
    public Vector3f acceleration = new Vector3f();

    /**
     * Integrates the particle forward in time by the given amount.
     * This function uses a Newton-Euler integration method, which is a
     * linear approximation to the correct integral. For this reason it
     * may be inaccurate in some cases.
     *
     * @param duration the delta time for integrate
     */
    public void integrate(float duration) {
        // We don't integrate things with zero mass.
        if (inverseMass <= 0.0f) return;

        assert (duration > 0.0);

        // Update linear position.
        position.add(velocity.mul(duration, new Vector3f()));
        //position.addScaledVector(velocity, duration);

        // Work out the acceleration from the force
        Vector3f resultingAcc = new Vector3f(acceleration);
        resultingAcc.add(forceAccum.mul(inverseMass, new Vector3f()));
        //resultingAcc.addScaledVector(forceAccum, inverseMass);

        // Update linear velocity from the acceleration.
        velocity.add(resultingAcc.mul(duration, new Vector3f()));
        //velocity.addScaledVector(resultingAcc, duration);


        // Impose drag.
        velocity.mul((float) Math.pow(damping, duration));
        //velocity *= real_pow(damping, duration);

        // Clear the forces.
        clearAccumulator();
    }

    /**
     * Sets the inverse mass of the particle.
     * <p>
     * This invalidates internal data for the particle. Either an
     * integration function, or the calculateInternals function should
     * be called before trying to get any settings from the particle.
     *
     * @param inverseMass The new inverse mass of the body. This may be zero, for a body
     *                    with infinite mass (i.e. unmovable).
     */
    public void setInverseMass(float inverseMass) {
        this.inverseMass = inverseMass;
    }

    /**
     * Gets the inverse mass of the particle.
     *
     * @return The current inverse mass of the particle.
     */
    public float getInverseMass() {
        return inverseMass;
    }

    /**
     * Sets the mass of the particle.
     * <p>
     * This invalidates internal data for the particle. Either an
     * integration function, or the calculateInternals function should
     * be called before trying to get any settings from the particle.
     *
     * @param mass The new mass of the body. This may not be zero. Small masses
     *             can produce unstable rigid bodies under simulation.
     */
    public void setMass(float mass) {
        assert (mass != 0);
        setInverseMass(1.0f / mass);
    }

    /**
     * Gets the mass of the particle.
     *
     * @return The current mass of the particle.
     */
    public float getMass() {
        if (inverseMass == 0) {
            return Float.MAX_VALUE;
        } else {
            return (1.0f / inverseMass);
        }
    }

    /**
     * Returns true if the mass of the particle is not-infinite.
     *
     * @return the inverseMass
     */
    public boolean hasFiniteMass() {
        return inverseMass >= 0.0f;
    }

    /**
     * Sets both the damping of the particle.
     *
     * @param damping the new value of damping
     */
    public void setDamping(float damping) {
        this.damping = damping;
    }

    /**
     * Sets the position of the particle.
     *
     * @param position The new position of the particle.
     */
    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    /**
     * Sets the position of the particle by component.
     *
     * @param x The x coordinate of the new position of the rigid body.
     * @param y The y coordinate of the new position of the rigid body.
     * @param z The z coordinate of the new position of the rigid body.
     */
    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    /**
     * Fills the given vector with the position of the rigid body.
     *
     * @param position A pointer to a vector into which to write
     *                 the position.
     */
    public Vector3f getPosition(Vector3f position) {
        return position.set(this.position);
    }


    /**
     * Gets the position of the particle.
     *
     * @return The position of the particle.
     */
    public Vector3f getPosition() {
        return this.position;
    }

    /**
     * Sets the velocity of the particle.
     *
     * @param velocity The new velocity of the particle.
     */
    public void setVelocity(Vector3f velocity) {
        this.velocity.set(velocity);
    }

    /**
     * Sets the velocity of the particle by component.
     *
     * @param x The x coordinate of the new velocity of the rigid body.
     * @param y The y coordinate of the new velocity of the rigid body.
     * @param z The z coordinate of the new velocity of the rigid body.
     */
    public void setVelocity(float x, float y, float z) {
        this.velocity.set(x, y, z);
    }

    /**
     * Fills the given vector with the velocity of the rigid body.
     *
     * @param velocity A pointer to a vector into which to write
     *                 the velocity. The velocity is given in world local space.
     */
    public Vector3f getVelocity(Vector3f velocity) {
        return velocity.set(this.velocity);
    }


    /**
     * Gets the velocity of the particle.
     *
     * @return The velocity of the particle. The velocity is given in world
     * local space.
     */
    public Vector3f getVelocity() {
        return this.velocity;
    }

    /**
     * Applies the given change in velocity.
     *
     * @param deltaVelocity the delta Velocity to add
     */
    public void addVelocity(Vector3f deltaVelocity) {
        this.velocity.add(deltaVelocity);
    }

    /**
     * Sets the constant acceleration of the particle.
     *
     * @param acceleration The new acceleration of the particle.
     */
    public void setAcceleration(Vector3f acceleration) {
        this.acceleration.set(acceleration);
    }

    /**
     * Sets the constant acceleration of the particle by component.
     *
     * @param x The x coordinate of the new acceleration of the rigid body.
     * @param y The y coordinate of the new acceleration of the rigid body.
     * @param z The z coordinate of the new acceleration of the rigid body.
     */
    public void setAcceleration(float x, float y, float z) {
        this.acceleration.set(x, y, z);
    }

    /**
     * Fills the given vector with the acceleration of the rigid body.
     *
     * @param acceleration A pointer to a vector into which to write
     *                     the acceleration. The acceleration is given in world local space.
     */
    public Vector3f getAcceleration(Vector3f acceleration) {
        return acceleration.set(this.acceleration);
    }


    /**
     * Gets the acceleration of the particle.
     *
     * @return The acceleration of the particle. The acceleration is given in
     * world local space.
     */
    public Vector3f getAcceleration() {
        return this.acceleration;
    }

    /**
     * Gets the current damping value.
     *
     * @return the value of damping
     */
    public float getDamping() {
        return damping;
    }


    /**
     * Clears the forces applied to the particle. This will be
     * called automatically after each integration step.
     */
    public void clearAccumulator() {
        forceAccum.zero();
    }

    /**
     * Adds the given force to the particle, to be applied at the next iteration
     * only.
     *
     * @param force The force to apply.
     */
    public void addForce(Vector3f force) {
        this.forceAccum.add(force);
    }

}