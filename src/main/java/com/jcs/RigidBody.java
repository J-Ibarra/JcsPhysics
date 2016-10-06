package com.jcs;

import org.joml.Matrix4f;
import org.joml.Matrix4x3f;

public class RigidBody extends Particle {
    /**
     * Holds a transform matrix for converting body space into
     * world space and vice versa. This can be achieved by calling
     * the getPointIn*Space functions.
     */
    public Matrix4x3f transformMatrix = new Matrix4x3f();

    /**
     * Fills the given matrix with a transformation representing
     * the rigid body's position and orientation.
     *
     * @param transform A pointer to the matrix to fill.
     * @note Transforming a vector by this matrix turns it from
     * the body's local space to world space.
     */
    public Matrix4x3f getTransform(Matrix4x3f transform) {
        return transform.set(transformMatrix);
    }

    public Matrix4f getGLTransform() {
        return new Matrix4f(transformMatrix).translate(position);
    }
}
