package com.jcs;

import org.joml.Intersectionf;
import org.joml.Matrix4x3f;
import org.joml.Vector3f;

/**
 * Represents a primitive to detect collisions against.
 * <p>
 * This class use {@link Intersectionf} to help the collision detector
 * and intersection routines, so they should have
 * access to its data
 */
public class CollisionPrimitive {

    /**
     * The rigid body that is represented by this primitive.
     */
    public RigidBody body = new RigidBody();

    /**
     * The offset of this primitive from the given rigid body.
     */
    private Matrix4x3f offset = new Matrix4x3f();

    /**
     * The resultant transform of the primitive. This is
     * calculated by combining the offset of the primitive
     * with the transform of the rigid body.
     */
    private Matrix4x3f transform = new Matrix4x3f();

    /**
     * Calculates the internals for the primitive.
     */
    public void calculateInternals() {
        body.getTransform(transform).mul(offset);
    }

    /**
     * This is a convenience function to allow access to the
     * axis vectors in the transform for this primitive.
     */
    public Vector3f getAxis(int index) {
        return transform.getColumn(index, new Vector3f());
    }

    /**
     * Returns the resultant transform of the primitive, calculated from
     * the combined offset of the primitive and the transform
     * (orientation + position) of the rigid body to which it is
     * attached.
     */
    public Matrix4x3f getTransform() {
        return transform;
    }

}
